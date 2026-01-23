#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "=========================================="
echo "Kafka Serialization Benchmark - Local Run"
echo "=========================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Error: Docker is not running"
    exit 1
fi

# Parse arguments
BENCHMARK_TYPE="${1:-pure}"
PAYLOAD_SIZE="${2:-P100}"

case "$BENCHMARK_TYPE" in
    pure)
        BENCHMARK_PATTERN=".*PureSerializationBenchmark.*"
        echo "Running: Pure Serialization Benchmark"
        ;;
    kafka)
        BENCHMARK_PATTERN=".*KafkaBenchmark.*"
        echo "Running: Kafka Benchmark"
        echo "Starting Kafka..."
        docker compose -f docker/docker-compose.yml up -d kafka
        sleep 10
        ;;
    registry)
        BENCHMARK_PATTERN=".*KafkaRegistryBenchmark.*"
        echo "Running: Kafka + Registry Benchmark"
        echo "Starting Kafka and Apicurio..."
        docker compose -f docker/docker-compose.yml up -d kafka apicurio
        sleep 15
        ;;
    all)
        BENCHMARK_PATTERN=".*Benchmark.*"
        echo "Running: All Benchmarks"
        echo "Starting Kafka and Apicurio..."
        docker compose -f docker/docker-compose.yml up -d kafka apicurio
        sleep 15
        ;;
    *)
        echo "Usage: $0 [pure|kafka|registry|all] [P100|P200|P400|P600|P800|P1000]"
        exit 1
        ;;
esac

echo "Payload Size: $PAYLOAD_SIZE"
echo "=========================================="

# Build the project
echo "Building project..."
./gradlew :serdes-benchmark:jmhJar --no-daemon

# Run the benchmark
echo "Running benchmark..."
java -Xms4g -Xmx4g -XX:+UseG1GC \
    -jar serdes-benchmark/build/libs/*-jmh.jar \
    -wi 2 \
    -i 3 \
    -f 1 \
    -p payloadSize="$PAYLOAD_SIZE" \
    -rf json \
    -rff results/jmh-results.json \
    -prof gc \
    "$BENCHMARK_PATTERN"

echo "=========================================="
echo "Results saved to: results/jmh-results.json"
echo "=========================================="
