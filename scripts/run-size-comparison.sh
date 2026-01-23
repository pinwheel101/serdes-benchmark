#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "=========================================="
echo "Serialized Size Comparison"
echo "=========================================="

# Build the project
echo "Building project..."
./gradlew :serdes-benchmark:classes --no-daemon

# Run size comparison
echo ""
java -cp "serdes-benchmark/build/classes/java/jmh:serdes-core/build/classes/java/main:serdes-avro/build/classes/java/main:serdes-protobuf/build/classes/java/main:$(./gradlew -q :serdes-benchmark:printClasspath 2>/dev/null || echo '')" \
    com.benchmark.serdes.jmh.SizeBenchmark 2>/dev/null || \
    ./gradlew :serdes-benchmark:run --args="size" --no-daemon 2>/dev/null || \
    echo "Run './gradlew :serdes-benchmark:jmhJar' first, then use the JAR directly"
