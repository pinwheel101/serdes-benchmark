plugins {
    java
    id("io.github.martinsjavacode.avro-gradle-plugin") version "1.2.0"
}

dependencies {
    implementation("org.apache.avro:avro:${property("avroVersion")}")
}

avro {
    sourceDir = "${rootProject.projectDir}/schemas/avro"
    outputDir = "${project.layout.buildDirectory.get()}/generated-sources/avro"
    fieldVisibility = "PRIVATE"
    stringType = "String"
    optionalGetters = false
}

sourceSets {
    main {
        java {
            srcDir("${project.layout.buildDirectory.get()}/generated-sources/avro")
        }
    }
}

tasks.named("compileJava") {
    dependsOn("generateAvroClasses")
}
