#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

cd "$PROJECT_DIR"

echo "=========================================="
echo "Kafka Serialization Benchmark - K8s Run"
echo "=========================================="

# Parse arguments
BENCHMARK_TYPE="${1:-pure}"
NAMESPACE="${2:-benchmark}"
IMAGE_TAG="${3:-latest}"

case "$BENCHMARK_TYPE" in
    pure)
        BENCHMARK_PATTERN=".*PureSerializationBenchmark.*"
        ;;
    kafka)
        BENCHMARK_PATTERN=".*KafkaBenchmark.*"
        ;;
    registry)
        BENCHMARK_PATTERN=".*KafkaRegistryBenchmark.*"
        ;;
    all)
        BENCHMARK_PATTERN=".*Benchmark.*"
        ;;
    *)
        echo "Usage: $0 [pure|kafka|registry|all] [namespace] [image-tag]"
        exit 1
        ;;
esac

echo "Benchmark Type: $BENCHMARK_TYPE"
echo "Namespace: $NAMESPACE"
echo "Image Tag: $IMAGE_TAG"
echo "=========================================="

# Build Docker image
echo "Building Docker image..."
docker build -t serdes-benchmark:$IMAGE_TAG -f docker/Dockerfile .

# Check if we need to push to a registry
if [ -n "$DOCKER_REGISTRY" ]; then
    echo "Pushing image to registry..."
    docker tag serdes-benchmark:$IMAGE_TAG $DOCKER_REGISTRY/serdes-benchmark:$IMAGE_TAG
    docker push $DOCKER_REGISTRY/serdes-benchmark:$IMAGE_TAG
fi

# Create namespace if it doesn't exist
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

# Apply ConfigMap
kubectl apply -f k8s/benchmark-configmap.yaml -n $NAMESPACE

# Generate Job manifest with the right benchmark pattern
JOB_NAME="serdes-benchmark-$(date +%Y%m%d-%H%M%S)"

cat <<EOF | kubectl apply -f -
apiVersion: batch/v1
kind: Job
metadata:
  name: $JOB_NAME
  namespace: $NAMESPACE
spec:
  ttlSecondsAfterFinished: 3600
  backoffLimit: 0
  template:
    spec:
      restartPolicy: Never
      containers:
        - name: benchmark
          image: serdes-benchmark:$IMAGE_TAG
          resources:
            requests:
              memory: "6Gi"
              cpu: "2"
            limits:
              memory: "8Gi"
              cpu: "4"
          envFrom:
            - configMapRef:
                name: benchmark-config
          args:
            - "-wi"
            - "3"
            - "-i"
            - "5"
            - "-f"
            - "2"
            - "-rf"
            - "json"
            - "-rff"
            - "/app/results/jmh-results.json"
            - "-prof"
            - "gc"
            - "$BENCHMARK_PATTERN"
          volumeMounts:
            - name: results
              mountPath: /app/results
      volumes:
        - name: results
          emptyDir: {}
EOF

echo "=========================================="
echo "Job created: $JOB_NAME"
echo "Watch progress: kubectl logs -f job/$JOB_NAME -n $NAMESPACE"
echo "=========================================="
