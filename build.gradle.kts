plugins {
    java
    id("com.google.protobuf") version "0.9.4" apply false
    id("me.champeau.jmh") version "0.7.2" apply false
}

allprojects {
    group = property("group") as String
    version = property("version") as String
}

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(property("jdkVersion") as String))
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all", "-parameters"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
