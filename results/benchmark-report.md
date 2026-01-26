# JMH Benchmark Report

Generated: 2026-01-26 23:48:03

## Table of Contents

1. [Summary](#summary)
2. [Throughput by Payload Size](#throughput-by-payload-size)
3. [Detailed Results by Payload Size](#detailed-results-by-payload-size)
4. [Latency Percentiles](#latency-percentiles)
5. [Memory Allocation](#memory-allocation)

---

## Summary

### Serialize Throughput (ops/ms)

> Higher is better

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 15.70 | 7.72 |
| AVRO | 27.20 | 13.35 |
| PROTOBUF | 77.57 | 39.71 |

### Deserialize Throughput (ops/ms)

> Higher is better

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 8.82 | 4.11 |
| AVRO | 20.50 | 10.14 |
| PROTOBUF | 30.73 | 17.05 |

### Roundtrip Throughput (ops/ms)

> Higher is better

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 5.39 | 2.85 |
| AVRO | 11.05 | 5.55 |
| PROTOBUF | 21.53 | 10.41 |

## Throughput by Payload Size

### Serialize - Performance Ratio

> Ratio compared to JSON (JSON = 1.0x)

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 1.00x | 1.00x |
| AVRO | 1.73x | 1.73x |
| PROTOBUF | 4.94x | 5.14x |

### Deserialize - Performance Ratio

> Ratio compared to JSON (JSON = 1.0x)

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 1.00x | 1.00x |
| AVRO | 2.32x | 2.47x |
| PROTOBUF | 3.48x | 4.15x |

### Roundtrip - Performance Ratio

> Ratio compared to JSON (JSON = 1.0x)

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 1.00x | 1.00x |
| AVRO | 2.05x | 1.94x |
| PROTOBUF | 4.00x | 3.65x |

## Detailed Results by Payload Size

### P100

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 15.70 | 8.82 | 5.39 |
| AVRO | 27.20 | 20.50 | 11.05 |
| PROTOBUF | 77.57 | 30.73 | 21.53 |

**Best performers:**
- Serialize: **PROTOBUF** (77.57 ops/ms)
- Deserialize: **PROTOBUF** (30.73 ops/ms)
- Roundtrip: **PROTOBUF** (21.53 ops/ms)

### P200

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 7.72 | 4.11 | 2.85 |
| AVRO | 13.35 | 10.14 | 5.55 |
| PROTOBUF | 39.71 | 17.05 | 10.41 |

**Best performers:**
- Serialize: **PROTOBUF** (39.71 ops/ms)
- Deserialize: **PROTOBUF** (17.05 ops/ms)
- Roundtrip: **PROTOBUF** (10.41 ops/ms)

## Latency Percentiles

> Lower is better (ms/op)

### P100

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.062 | 0.076 | 0.133 | 0.194 |
| JSON | deserialize | 0.110 | 0.132 | 0.225 | 0.326 |
| JSON | roundtrip | 0.182 | 0.213 | 0.324 | 0.449 |
| AVRO | serialize | 0.031 | 0.044 | 0.089 | 0.139 |
| AVRO | deserialize | 0.046 | 0.054 | 0.110 | 0.172 |
| AVRO | roundtrip | 0.088 | 0.104 | 0.181 | 0.268 |
| PROTOBUF | serialize | 0.012 | 0.013 | 0.038 | 0.075 |
| PROTOBUF | deserialize | 0.030 | 0.051 | 0.104 | 0.195 |
| PROTOBUF | roundtrip | 0.054 | 0.067 | 0.118 | 0.238 |

### P200

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.125 | 0.144 | 0.229 | 0.303 |
| JSON | deserialize | 0.228 | 0.285 | 0.403 | 0.794 |
| JSON | roundtrip | 0.364 | 0.436 | 0.665 | 0.903 |
| AVRO | serialize | 0.077 | 0.094 | 0.159 | 0.219 |
| AVRO | deserialize | 0.098 | 0.125 | 0.193 | 0.256 |
| AVRO | roundtrip | 0.180 | 0.224 | 0.327 | 0.445 |
| PROTOBUF | serialize | 0.023 | 0.028 | 0.063 | 0.118 |
| PROTOBUF | deserialize | 0.052 | 0.087 | 0.133 | 0.192 |
| PROTOBUF | roundtrip | 0.103 | 0.120 | 0.186 | 0.260 |

## Memory Allocation

> Lower is better (B/op)

### P100

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 35,594 | 56,521 | 92,115 |
| AVRO | 23,904 | 27,856 | 51,761 |
| PROTOBUF | 6,576 | 39,768 | 46,344 |

### P200

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 69,768 | 110,521 | 180,301 |
| AVRO | 47,272 | 54,249 | 101,521 |
| PROTOBUF | 12,976 | 76,968 | 89,945 |

### Memory Efficiency Ratio

> Ratio compared to JSON (lower is better, JSON = 1.0x)

#### Serialize

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 1.00x | 1.00x |
| AVRO | 0.67x | 0.68x |
| PROTOBUF | 0.18x | 0.19x |

#### Deserialize

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 1.00x | 1.00x |
| AVRO | 0.49x | 0.49x |
| PROTOBUF | 0.70x | 0.70x |

#### Roundtrip

| Format | P100 | P200 |
|:---|---:|---:|
| JSON | 1.00x | 1.00x |
| AVRO | 0.56x | 0.56x |
| PROTOBUF | 0.50x | 0.50x |

