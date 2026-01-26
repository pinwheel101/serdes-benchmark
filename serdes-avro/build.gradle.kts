plugins {
    java
    id("eu.eventloopsoftware.avro-gradle-plugin")
}

dependencies {
    implementation("org.apache.avro:avro:${property("avroVersion")}")
}

avro {
    sourceDirectory = "${rootProject.projectDir}/schemas/avro"
    stringType = "String"
}

tasks.named<JavaCompile>("compileJava") {
    source(tasks.named("avroGenerateJavaClasses"))
}

sourceSets {
    main {
        resources {
            srcDir("${rootProject.projectDir}/schemas/avro")
        }
    }
}
