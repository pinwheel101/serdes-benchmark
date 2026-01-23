# Kafka Serialization Performance Benchmark

A comprehensive benchmarking suite for comparing JSON, Avro, and Protobuf serialization performance in Kafka environments.

## Technology Stack

| Component | Version |
|-----------|---------|
| JDK | 17 |
| Gradle | 8.14.3 |
| Kafka | 4.1.1 |
| Apicurio Registry | 3.1.6 |
| Avro | 1.11.4 |
| Protobuf | 4.32.1 |
| Jackson | 2.18.2 |
| JMH | 1.37 |

## Project Structure

```
serdes-benchmark/
├── schemas/                    # Schema definitions
│   ├── avro/trace-data.avsc
│   └── protobuf/trace_data.proto
├── serdes-core/               # Core module (POJOs, serializers, generator)
├── serdes-avro/               # Generated Avro classes
├── serdes-protobuf/           # Generated Protobuf classes
├── serdes-benchmark/          # JMH benchmarks
├── docker/                    # Docker development environment
├── k8s/                       # Kubernetes benchmark manifests
├── scripts/                   # Run scripts
└── results/                   # Benchmark output
```

## Benchmark Modes

| Mode | Description | Requirements |
|------|-------------|--------------|
| **Pure Serialization** | Direct serialize/deserialize | None |
| **Kafka** | Produce/consume through Kafka | Kafka |
| **Kafka + Registry** | With Apicurio Schema Registry | Kafka + Apicurio |

## Payload Sizes

| Size | Parameters | Estimated JSON Size |
|------|------------|---------------------|
| P100 | 100 | ~28 KB |
| P200 | 200 | ~50 KB |
| P400 | 400 | ~93 KB |
| P600 | 600 | ~137 KB |
| P800 | 800 | ~180 KB |
| P1000 | 1000 | ~223 KB |

## Quick Start

### Local Development (Docker)

```bash
# Start Kafka and Apicurio
cd docker
docker compose up -d

# Build the project
./gradlew build

# Run pure serialization benchmark
./scripts/run-local.sh pure P100

# Run Kafka benchmark
./scripts/run-local.sh kafka P200

# Run with Schema Registry
./scripts/run-local.sh registry P400
```

### Kubernetes Deployment

```bash
# Build and deploy benchmark job
./scripts/run-k8s.sh pure benchmark latest

# Watch logs
kubectl logs -f job/serdes-benchmark -n benchmark
```

## Benchmark Metrics

- **Throughput**: Operations per second (ops/s)
- **Latency**: p50, p95, p99, p99.9 percentiles
- **Memory**: Allocation rate, bytes per operation
- **Size**: Serialized message size comparison

## Data Structure

The benchmark uses FDC (Fault Detection and Classification) trace data from semiconductor manufacturing:

```json
{
  "command": "TRACE_DATA",
  "fab": "M16",
  "eqpId": "6DGP4212",
  "collectionInterval": 1.0,
  "eventTime": "2026-01-13 12:31:18.000",
  "fdcChambId": "6DGP4212_PM1",
  "txid": "20260113123118000_6DGP4212_PM1",
  "parameterList": [
    {
      "name": "VIR_SCR_TOXIC_GAS_FLOW_LEVEL_DETECT",
      "alias": "VIR_SCR_TOXIC_GAS_FLOW_LEVEL_DETECT",
      "svid": null,
      "paramValue": "0.6000000238",
      "target": "",
      "lsl": "",
      "usl": "",
      "lcl": "",
      "ucl": "",
      "faultCode": "",
      "specType": "",
      "comment": null,
      "modelName": "",
      "specMId": null
    }
  ],
  "context": {
    "batchId": "N/A",
    "batchTyp": "N",
    "slotNo": "",
    "lotId": "TE2ZL69",
    "lotCd": "6E2",
    "operId": "A1956000C",
    "operDesc": "ISO GF NIT DEP",
    "portNo": "",
    "mesRecipeId": "",
    "recipeId": "EX_SIGE_R1_PM1",
    "prodId": "H58GCXAXXXX074N-AAA01-CX001",
    "reticleId": "",
    "batchLotListVal": "",
    "stepId": "6",
    "stepNm": "PURGE",
    "substId": "",
    "statCd": "",
    "unitCd": "",
    "startStepId": "",
    "endStepId": "",
    "putId": "",
    "zone": ""
  }
}
```

## Running Benchmarks

### Full Benchmark Suite

```bash
./gradlew :serdes-benchmark:jmh
```

### Quick Validation

```bash
./gradlew :serdes-benchmark:jmhQuick
```

### Specific Benchmark

```bash
java -jar serdes-benchmark/build/libs/*-jmh.jar \
  -wi 3 -i 5 -f 2 \
  -p payloadSize=P100,P200,P400 \
  -rf json -rff results/jmh-results.json \
  ".*PureSerializationBenchmark.*"
```

## Results

Results are saved to `results/jmh-results.json` in JMH JSON format.

## License

MIT
