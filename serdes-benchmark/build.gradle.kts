plugins {
    java
    application
    id("me.champeau.jmh")
}

application {
    mainClass.set("com.benchmark.serdes.report.BenchmarkReportGenerator")
}

dependencies {
    // Report generation
    implementation("com.fasterxml.jackson.core:jackson-databind:${property("jacksonVersion")}")

    implementation(project(":serdes-core"))
    implementation(project(":serdes-avro"))
    implementation(project(":serdes-protobuf"))

    // JMH
    jmh("org.openjdk.jmh:jmh-core:${property("jmhVersion")}")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:${property("jmhVersion")}")

    // JMH needs these dependencies for Kafka benchmarks
    jmh(project(":serdes-core"))
    jmh(project(":serdes-avro"))
    jmh(project(":serdes-protobuf"))
    jmh("org.apache.kafka:kafka-clients:${property("kafkaVersion")}")
    jmh("com.fasterxml.jackson.core:jackson-databind:${property("jacksonVersion")}")
    jmh("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${property("jacksonVersion")}")
    jmh("org.apache.avro:avro:${property("avroVersion")}")
    jmh("com.google.protobuf:protobuf-java:${property("protobufVersion")}")
    jmh("io.apicurio:apicurio-registry-avro-serde-kafka:${property("apicurioVersion")}")
    jmh("io.apicurio:apicurio-registry-protobuf-serde-kafka:${property("apicurioVersion")}")
    jmh("io.apicurio:apicurio-registry-jsonschema-serde-kafka:${property("apicurioVersion")}")
}

jmh {
    warmupIterations.set(3)
    iterations.set(5)
    fork.set(2)
    threads.set(1)

    // Benchmark modes
    benchmarkMode.set(listOf("thrpt", "sample"))

    // Time units
    timeUnit.set("ms")

    // GC profiling
    profilers.set(listOf("gc"))

    // Output
    resultsFile.set(project.file("${project.rootDir}/results/jmh-results.json"))
    resultFormat.set("JSON")

    // JVM args
    jvmArgs.set(listOf(
        "-Xms4g",
        "-Xmx4g",
        "-XX:+UseG1GC",
        "-XX:+AlwaysPreTouch"
    ))
}

// Quick benchmark task for validation
tasks.register<JavaExec>("jmhQuick") {
    group = "benchmark"
    description = "Run quick benchmark for validation"
    classpath = sourceSets["jmh"].runtimeClasspath
    mainClass.set("org.openjdk.jmh.Main")
    args = listOf(
        "-wi", "1",
        "-i", "2",
        "-f", "1",
        "-r", "1s",
        ".*Benchmark.*"
    )
}

// Generate benchmark report from JMH results
tasks.register<JavaExec>("generateReport") {
    group = "benchmark"
    description = "Generate markdown report from JMH results"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.benchmark.serdes.report.BenchmarkReportGenerator")

    val inputFile = project.rootDir.resolve("results/jmh-results.json")
    val outputFile = project.rootDir.resolve("results/benchmark-report.md")

    args = listOf(inputFile.absolutePath, outputFile.absolutePath)

    doFirst {
        if (!inputFile.exists()) {
            throw GradleException("JMH results file not found: ${inputFile.absolutePath}\nRun benchmarks first with: ./scripts/run-local.sh")
        }
    }
}
