plugins {
    java
}

dependencies {
    // Project modules for generated classes
    implementation(project(":serdes-avro"))
    implementation(project(":serdes-protobuf"))

    // Kafka client
    implementation("org.apache.kafka:kafka-clients:${property("kafkaVersion")}")

    // Jackson (JSON)
    implementation("com.fasterxml.jackson.core:jackson-databind:${property("jacksonVersion")}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${property("jacksonVersion")}")

    // Avro
    implementation("org.apache.avro:avro:${property("avroVersion")}")

    // Protobuf
    implementation("com.google.protobuf:protobuf-java:${property("protobufVersion")}")

    // Apicurio Registry SerDe
    // implementation("io.apicurio:apicurio-registry-serdes-avro-serde:${property("apicurioVersion")}")
    // implementation("io.apicurio:apicurio-registry-serdes-protobuf-serde:${property("apicurioVersion")}")
    // implementation("io.apicurio:apicurio-registry-serdes-jsonschema-serde:${property("apicurioVersion")}")

    implementation("io.apicurio:apicurio-registry-avro-serde-kafka:${property("apicurioVersion")}")
    implementation("io.apicurio:apicurio-registry-protobuf-serde-kafka:${property("apicurioVersion")}")
    implementation("io.apicurio:apicurio-registry-jsonschema-serde-kafka:${property("apicurioVersion")}")

    // Logging
    implementation("org.slf4j:slf4j-api:${property("slf4jVersion")}")
    runtimeOnly("ch.qos.logback:logback-classic:${property("logbackVersion")}")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:${property("junitVersion")}")
}
