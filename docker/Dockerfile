FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew gradlew.bat ./
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

# Copy source code
COPY schemas/ schemas/
COPY serdes-core/ serdes-core/
COPY serdes-avro/ serdes-avro/
COPY serdes-protobuf/ serdes-protobuf/
COPY serdes-benchmark/ serdes-benchmark/

# Build the project
RUN chmod +x gradlew && ./gradlew :serdes-benchmark:jmhJar --no-daemon

# Runtime image
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy JMH benchmark JAR
COPY --from=builder /app/serdes-benchmark/build/libs/*-jmh.jar benchmark.jar

# Create results directory
RUN mkdir -p /app/results

# Default JMH arguments
ENV JMH_ARGS="-rf json -rff /app/results/jmh-results.json"

# Entry point for running benchmarks
ENTRYPOINT ["java", "-jar", "benchmark.jar"]
CMD ["-wi", "3", "-i", "5", "-f", "2", "-rf", "json", "-rff", "/app/results/jmh-results.json"]
