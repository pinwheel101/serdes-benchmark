import org.apache.spark.sql.{Column, DataFrame, SparkSession, functions => F}
import org.apache.spark.sql.types.{ArrayType, BinaryType, StructType}
import org.slf4j.{Logger, LoggerFactory}


object DataVerificationJob {
  val LOG: Logger = LoggerFactory.getLogger(FdcDataVerificationDriver.getClass)

  private val SEPARATOR = "=" * 70

  def main(args: Array[String]): Unit = {
    if (args.length < 3) {
      System.err.println(
        """Usage: FdcDataVerificationDriver <sparkMaster> <oldPath> <newPath> [excludeColumns] [format] [sortArrays]
          |  sparkMaster    : Spark master URL (yarn, local[*])
          |  oldPath        : 리팩토링 이전 출력 경로
          |  newPath        : 리팩토링 이후 출력 경로
          |  excludeColumns : 비교 제외 컬럼 (쉼표 구분, 기본: idx)
          |  format         : 데이터 포맷 (기본: orc)
          |  sortArrays     : 배열 컬럼 정렬 후 비교 (기본: false, Step 5 등 collect_list 사용 시 true)""".stripMargin)
      sys.exit(1)
    }

    val sparkMaster = args(0)
    val oldPath = args(1)
    val newPath = args(2)
    val excludeColumns =
      if (args.length > 3 && args(3).nonEmpty) args(3).split(",").map(_.trim).toSet
      else Set("idx")
    val format = if (args.length > 4) args(4) else "orc"
    val sortArrays = args.length > 5 && args(5).equalsIgnoreCase("true")

    val spark = SparkSession.builder()
      .appName(s"FdcDataVerification_${System.currentTimeMillis()}")
      .master(sparkMaster)
      .config("spark.sql.adaptive.enabled", "true")
      .getOrCreate()

    try {
      LOG.info(SEPARATOR)
      LOG.info("Data Verification Start")
      LOG.info(SEPARATOR)
      LOG.info(s"Old path    : $oldPath")
      LOG.info(s"New path    : $newPath")
      LOG.info(s"Format      : $format")
      LOG.info(s"Exclude     : ${excludeColumns.mkString(", ")}")
      LOG.info(s"Sort arrays : $sortArrays")
      LOG.info(SEPARATOR)

      val oldRaw = spark.read.format(format).load(oldPath)
      val newRaw = spark.read.format(format).load(newPath)

      val oldDf = dropColumns(oldRaw, excludeColumns)
      val newDf = dropColumns(newRaw, excludeColumns)

      // Phase 1: Schema
      LOG.info("[Phase 1] Schema Comparison")
      if (!verifySchema(oldDf.schema, newDf.schema)) {
        LOG.error("FAIL: Schema mismatch. Aborting content comparison.")
        logResult(passed = false)
        return
      }

      // 컬럼 순서 정렬 (비교 안정성)
      val sortedCols = oldDf.columns.sorted
      val oldOrdered = oldDf.select(sortedCols.map(F.col): _*)
      val newOrdered = newDf.select(sortedCols.map(F.col): _*)

      // Phase 2: Row Count
      LOG.info("[Phase 2] Row Count Comparison")
      val oldCount = oldOrdered.count()
      val newCount = newOrdered.count()
      LOG.info(s"  Old : $oldCount rows")
      LOG.info(s"  New : $newCount rows")
      val countMatch = oldCount == newCount
      if (countMatch) {
        LOG.info("  MATCH")
      } else {
        LOG.warn(s"  MISMATCH (diff = ${oldCount - newCount})")
      }

      // Phase 3: Content
      LOG.info("[Phase 3] Content Comparison")

      // 전처리: 배열 정렬 → 바이너리 해시
      val oldPrepped = prepareForComparison(oldOrdered, sortArrays).cache()
      val newPrepped = prepareForComparison(newOrdered, sortArrays).cache()

      // 양방향 except
      val onlyInOld = oldPrepped.except(newPrepped)
      val onlyInNew = newPrepped.except(oldPrepped)

      val onlyInOldCount = onlyInOld.count()
      val onlyInNewCount = onlyInNew.count()

      LOG.info(s"  Rows only in OLD : $onlyInOldCount")
      LOG.info(s"  Rows only in NEW : $onlyInNewCount")

      val contentMatch = onlyInOldCount == 0 && onlyInNewCount == 0
      val passed = countMatch && contentMatch

      if (passed) {
        logResult(passed = true)
      } else {
        // 차이 샘플 출력
        if (onlyInOldCount > 0) {
          LOG.warn("--- Rows only in OLD (sample, max 20) ---")
          onlyInOld.show(20, truncate = 80)
        }
        if (onlyInNewCount > 0) {
          LOG.warn("--- Rows only in NEW (sample, max 20) ---")
          onlyInNew.show(20, truncate = 80)
        }

        // Phase 4: Column-level diagnostics (단일 agg 쿼리로 수행)
        LOG.info("[Phase 4] Column-Level Diagnostics")
        logColumnDiagnostics(oldOrdered, newOrdered, sortedCols)

        logResult(passed = false)
      }

      oldPrepped.unpersist()
      newPrepped.unpersist()

    } finally {
      spark.stop()
    }
  }

  /** 비교 전 전처리: 배열 정렬 + 바이너리 해시 */
  private def prepareForComparison(df: DataFrame, sortArrays: Boolean): DataFrame = {
    val sorted = if (sortArrays) sortCorrelatedArrays(df) else df
    hashBinaryColumns(sorted)
  }

  /** 지정된 컬럼들을 DataFrame에서 제거 */
  private def dropColumns(df: DataFrame, cols: Set[String]): DataFrame =
    cols.foldLeft(df) { (d, c) =>
      if (d.columns.contains(c)) d.drop(c) else d
    }

  /** 스키마(컬럼명 + 타입) 비교. 일치하면 true */
  private def verifySchema(oldSchema: StructType, newSchema: StructType): Boolean = {
    val oldFields = oldSchema.fields.map(f => (f.name, f.dataType)).toSet
    val newFields = newSchema.fields.map(f => (f.name, f.dataType)).toSet

    val onlyInOld = oldFields -- newFields
    val onlyInNew = newFields -- oldFields

    if (onlyInOld.nonEmpty)
      LOG.warn(s"  Columns only in OLD : ${onlyInOld.map { case (n, t) => s"$n($t)" }.mkString(", ")}")
    if (onlyInNew.nonEmpty)
      LOG.warn(s"  Columns only in NEW : ${onlyInNew.map { case (n, t) => s"$n($t)" }.mkString(", ")}")

    val ok = onlyInOld.isEmpty && onlyInNew.isEmpty
    LOG.info(s"  Schema : ${if (ok) "MATCH" else "MISMATCH"}")
    ok
  }

  /**
   * 위치 대응(correlated) 배열 컬럼들을 정렬.
   *
   * collect_list는 원소 순서를 보장하지 않으므로,
   * 비교 전에 정규화가 필요하다.
   *
   * 처리 흐름:
   *   1) ArrayType 컬럼 탐지
   *   2) arrays_zip으로 struct 배열 생성 (위치 대응 유지)
   *   3) array_sort로 struct 내부 필드 기준 정렬
   *   4) transform으로 각 필드를 다시 개별 배열로 추출
   *
   * 예시 (Step 5):
   *   정렬 전: param_name=["B","A"], param_value=["2.0","1.0"]
   *   zip:     [{B,2.0}, {A,1.0}]
   *   sort:    [{A,1.0}, {B,2.0}]
   *   추출 후: param_name=["A","B"], param_value=["1.0","2.0"]
   */
  private def sortCorrelatedArrays(df: DataFrame): DataFrame = {
    val arrayCols = df.schema.fields
      .filter(_.dataType.isInstanceOf[ArrayType])
      .map(_.name)
      .sorted // 알파벳 순으로 정렬 → struct 필드 순서 결정론적

    if (arrayCols.isEmpty) return df

    LOG.info(s"  Sorting correlated arrays: ${arrayCols.mkString(", ")}")

    val nonArrayCols = df.columns.filterNot(arrayCols.contains)

    // arrays_zip → array_sort
    val zippedSorted = F.array_sort(F.arrays_zip(arrayCols.map(F.col): _*))

    val withSorted = df.withColumn("__sorted_zip", zippedSorted)

    // 비배열 컬럼 유지 + 각 배열 컬럼을 정렬된 struct에서 추출
    val selectExprs: Array[Column] =
      nonArrayCols.map(F.col) ++
        arrayCols.map { col =>
          F.transform(F.col("__sorted_zip"), (elem: Column) => elem.getField(col)).as(col)
        }

    withSorted.select(selectExprs: _*)
  }

  /** BinaryType 컬럼을 MD5 해시(hex string)로 변환. except 성능 향상 */
  private def hashBinaryColumns(df: DataFrame): DataFrame = {
    val binaryCols = df.schema.fields.filter(_.dataType == BinaryType).map(_.name)
    if (binaryCols.nonEmpty) {
      LOG.info(s"  Hashing binary columns: ${binaryCols.mkString(", ")}")
    }
    binaryCols.foldLeft(df) { (d, col) =>
      d.withColumn(col, F.md5(F.col(col)))
    }
  }

  /** 컬럼별 null 수, distinct 수를 단일 agg로 비교 */
  private def logColumnDiagnostics(
    oldDf: DataFrame,
    newDf: DataFrame,
    columns: Array[String]
  ): Unit = {
    val aggExprs = columns.flatMap { col =>
      Seq(
        F.sum(F.when(F.col(col).isNull, 1).otherwise(0)).as(s"${col}__nulls"),
        F.countDistinct(F.col(col)).as(s"${col}__distinct")
      )
    }

    val oldStats = oldDf.agg(aggExprs.head, aggExprs.tail: _*).collect()(0)
    val newStats = newDf.agg(aggExprs.head, aggExprs.tail: _*).collect()(0)

    var hasDiff = false
    columns.foreach { col =>
      val oldNulls = oldStats.getAs[Long](s"${col}__nulls")
      val newNulls = newStats.getAs[Long](s"${col}__nulls")
      val oldDistinct = oldStats.getAs[Long](s"${col}__distinct")
      val newDistinct = newStats.getAs[Long](s"${col}__distinct")

      if (oldNulls != newNulls || oldDistinct != newDistinct) {
        LOG.warn(f"  %-25s nulls(old=$oldNulls%,d, new=$newNulls%,d) distinct(old=$oldDistinct%,d, new=$newDistinct%,d)", col)
        hasDiff = true
      }
    }
    if (!hasDiff) {
      LOG.info("  All columns have identical null/distinct counts")
    }
  }

  private def logResult(passed: Boolean): Unit = {
    LOG.info(SEPARATOR)
    if (passed) {
      LOG.info("RESULT: PASS - Datasets are identical")
    } else {
      LOG.warn("RESULT: FAIL - Differences detected")
    }
    LOG.info(SEPARATOR)
  }
}