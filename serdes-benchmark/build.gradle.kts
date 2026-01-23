plugins {
    java
    id("me.champeau.jmh")
}

dependencies {
    implementation(project(":serdes-core"))
    implementation(project(":serdes-avro"))
    implementation(project(":serdes-protobuf"))

    // JMH
    jmh("org.openjdk.jmh:jmh-core:${property("jmhVersion")}")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:${property("jmhVersion")}")
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
