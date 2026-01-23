import com.google.protobuf.gradle.*

plugins {
    java
    id("com.google.protobuf")
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:${property("protobufVersion")}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${property("protobufVersion")}"
    }
}

sourceSets {
    main {
        proto {
            srcDir("${rootProject.projectDir}/schemas/protobuf")
        }
    }
}
