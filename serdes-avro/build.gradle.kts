plugins {
    java
    id("com.github.davidmc24.gradle.plugin.avro")
}

dependencies {
    implementation("org.apache.avro:avro:${property("avroVersion")}")
}

avro {
    isCreateSetters.set(true)
    isCreateOptionalGetters.set(false)
    isGettersReturnOptional.set(false)
    isOptionalGettersForNullableFieldsOnly.set(false)
    fieldVisibility.set("PRIVATE")
    outputCharacterEncoding.set("UTF-8")
    stringType.set("String")
}

sourceSets {
    main {
        resources {
            srcDir("${rootProject.projectDir}/schemas/avro")
        }
    }
}
