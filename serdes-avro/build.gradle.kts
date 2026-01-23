plugins {
    java
    id("eu.eventloopsoftware.avro-gradle-plugin") version "0.0.7"
}

dependencies {
    implementation("org.apache.avro:avro:${property("avroVersion")}")
}

avro {
    sourceDirectory.set("${rootProject.projectDir}/schemas/avro")
    outputDirectory.set(layout.buildDirectory.dir("generated-sources/avro"))
    fieldVisibility.set("PRIVATE")
    stringType.set("String")
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated-sources/avro"))
        }
    }
}

tasks.named("compileJava") {
    dependsOn("avroGenerateJavaClasses")
}
