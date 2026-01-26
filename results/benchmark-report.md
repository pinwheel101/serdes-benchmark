# JMH Benchmark Report

Generated: 2026-01-27 00:01:27

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

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 15.13 | 1.58 | 7.55 | 3.95 | 2.41 | 1.55 |
| AVRO | 23.81 | 2.42 | 12.47 | 6.05 | 4.20 | 3.01 |
| PROTOBUF | 76.69 | 7.13 | 41.17 | 20.60 | 13.04 | 6.83 |

### Deserialize Throughput (ops/ms)

> Higher is better

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 8.39 | 0.86 | 4.30 | 2.17 | 1.46 | 1.09 |
| AVRO | 18.01 | 2.11 | 9.80 | 4.89 | 3.00 | 3.06 |
| PROTOBUF | 18.12 | 3.11 | 14.96 | 7.61 | 5.51 | 4.16 |

### Roundtrip Throughput (ops/ms)

> Higher is better

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 5.27 | 0.55 | 2.63 | 1.33 | 0.91 | 0.68 |
| AVRO | 10.43 | 1.08 | 5.21 | 2.69 | 1.96 | 1.48 |
| PROTOBUF | 23.27 | 2.05 | 8.64 | 5.53 | 3.56 | 2.75 |

## Throughput by Payload Size

### Serialize - Performance Ratio

> Ratio compared to JSON (JSON = 1.0x)

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x |
| AVRO | 1.57x | 1.53x | 1.65x | 1.53x | 1.74x | 1.94x |
| PROTOBUF | 5.07x | 4.51x | 5.45x | 5.22x | 5.40x | 4.41x |

### Deserialize - Performance Ratio

> Ratio compared to JSON (JSON = 1.0x)

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x |
| AVRO | 2.15x | 2.44x | 2.28x | 2.26x | 2.06x | 2.82x |
| PROTOBUF | 2.16x | 3.61x | 3.48x | 3.51x | 3.78x | 3.82x |

### Roundtrip - Performance Ratio

> Ratio compared to JSON (JSON = 1.0x)

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x |
| AVRO | 1.98x | 1.97x | 1.98x | 2.02x | 2.16x | 2.18x |
| PROTOBUF | 4.42x | 3.74x | 3.29x | 4.16x | 3.92x | 4.05x |

## Detailed Results by Payload Size

### P100

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 15.13 | 8.39 | 5.27 |
| AVRO | 23.81 | 18.01 | 10.43 |
| PROTOBUF | 76.69 | 18.12 | 23.27 |

**Best performers:**
- Serialize: **PROTOBUF** (76.69 ops/ms)
- Deserialize: **PROTOBUF** (18.12 ops/ms)
- Roundtrip: **PROTOBUF** (23.27 ops/ms)

### P1000

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 1.58 | 0.86 | 0.55 |
| AVRO | 2.42 | 2.11 | 1.08 |
| PROTOBUF | 7.13 | 3.11 | 2.05 |

**Best performers:**
- Serialize: **PROTOBUF** (7.13 ops/ms)
- Deserialize: **PROTOBUF** (3.11 ops/ms)
- Roundtrip: **PROTOBUF** (2.05 ops/ms)

### P200

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 7.55 | 4.30 | 2.63 |
| AVRO | 12.47 | 9.80 | 5.21 |
| PROTOBUF | 41.17 | 14.96 | 8.64 |

**Best performers:**
- Serialize: **PROTOBUF** (41.17 ops/ms)
- Deserialize: **PROTOBUF** (14.96 ops/ms)
- Roundtrip: **PROTOBUF** (8.64 ops/ms)

### P400

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 3.95 | 2.17 | 1.33 |
| AVRO | 6.05 | 4.89 | 2.69 |
| PROTOBUF | 20.60 | 7.61 | 5.53 |

**Best performers:**
- Serialize: **PROTOBUF** (20.60 ops/ms)
- Deserialize: **PROTOBUF** (7.61 ops/ms)
- Roundtrip: **PROTOBUF** (5.53 ops/ms)

### P600

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 2.41 | 1.46 | 0.91 |
| AVRO | 4.20 | 3.00 | 1.96 |
| PROTOBUF | 13.04 | 5.51 | 3.56 |

**Best performers:**
- Serialize: **PROTOBUF** (13.04 ops/ms)
- Deserialize: **PROTOBUF** (5.51 ops/ms)
- Roundtrip: **PROTOBUF** (3.56 ops/ms)

### P800

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 1.55 | 1.09 | 0.68 |
| AVRO | 3.01 | 3.06 | 1.48 |
| PROTOBUF | 6.83 | 4.16 | 2.75 |

**Best performers:**
- Serialize: **PROTOBUF** (6.83 ops/ms)
- Deserialize: **PROTOBUF** (4.16 ops/ms)
- Roundtrip: **PROTOBUF** (2.75 ops/ms)

## Latency Percentiles

> Lower is better (ms/op)

### P100

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.060 | 0.078 | 0.127 | 0.188 |
| JSON | deserialize | 0.122 | 0.188 | 0.337 | 1.130 |
| JSON | roundtrip | 0.355 | 0.592 | 1.584 | 9.153 |
| AVRO | serialize | 0.038 | 0.050 | 0.099 | 0.163 |
| AVRO | deserialize | 0.048 | 0.075 | 0.152 | 0.572 |
| AVRO | roundtrip | 0.093 | 0.125 | 0.192 | 0.269 |
| PROTOBUF | serialize | 0.012 | 0.014 | 0.032 | 0.069 |
| PROTOBUF | deserialize | 0.027 | 0.044 | 0.061 | 0.097 |
| PROTOBUF | roundtrip | 0.053 | 0.060 | 0.081 | 0.111 |

### P1000

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.585 | 0.663 | 0.993 | 1.383 |
| JSON | deserialize | 1.585 | 2.393 | 5.191 | 55.375 |
| JSON | roundtrip | 1.821 | 2.015 | 2.707 | 3.386 |
| AVRO | serialize | 0.482 | 0.784 | 1.734 | 4.410 |
| AVRO | deserialize | 0.492 | 0.787 | 1.247 | 2.049 |
| AVRO | roundtrip | 0.953 | 1.157 | 1.846 | 3.026 |
| PROTOBUF | serialize | 0.115 | 0.148 | 0.227 | 0.335 |
| PROTOBUF | deserialize | 0.282 | 0.438 | 0.606 | 0.841 |
| PROTOBUF | roundtrip | 0.523 | 0.564 | 0.726 | 0.874 |

### P200

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.127 | 0.144 | 0.226 | 0.284 |
| JSON | deserialize | 0.230 | 0.304 | 0.482 | 0.776 |
| JSON | roundtrip | 0.367 | 0.596 | 1.183 | 3.802 |
| AVRO | serialize | 0.104 | 0.176 | 0.400 | 1.456 |
| AVRO | deserialize | 0.096 | 0.109 | 0.165 | 0.213 |
| AVRO | roundtrip | 0.191 | 0.328 | 0.596 | 2.573 |
| PROTOBUF | serialize | 0.023 | 0.026 | 0.045 | 0.071 |
| PROTOBUF | deserialize | 0.052 | 0.087 | 0.113 | 0.150 |
| PROTOBUF | roundtrip | 0.105 | 0.115 | 0.149 | 0.191 |

### P400

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.245 | 0.276 | 0.423 | 0.777 |
| JSON | deserialize | 0.453 | 0.514 | 0.771 | 1.179 |
| JSON | roundtrip | 0.739 | 1.085 | 1.726 | 2.725 |
| AVRO | serialize | 0.255 | 0.319 | 0.903 | 4.649 |
| AVRO | deserialize | 0.152 | 0.218 | 0.304 | 0.400 |
| AVRO | roundtrip | 0.346 | 0.428 | 0.642 | 1.426 |
| PROTOBUF | serialize | 0.047 | 0.055 | 0.088 | 0.158 |
| PROTOBUF | deserialize | 0.104 | 0.176 | 0.253 | 0.328 |
| PROTOBUF | roundtrip | 0.208 | 0.227 | 0.298 | 0.399 |

### P600

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.397 | 0.459 | 0.760 | 1.191 |
| JSON | deserialize | 0.680 | 0.770 | 1.118 | 1.524 |
| JSON | roundtrip | 1.059 | 1.227 | 1.968 | 2.324 |
| AVRO | serialize | 0.249 | 0.403 | 0.560 | 1.056 |
| AVRO | deserialize | 0.265 | 0.330 | 0.502 | 0.671 |
| AVRO | roundtrip | 0.554 | 0.717 | 1.001 | 1.362 |
| PROTOBUF | serialize | 0.073 | 0.098 | 0.153 | 0.270 |
| PROTOBUF | deserialize | 0.153 | 0.255 | 0.311 | 0.382 |
| PROTOBUF | roundtrip | 0.308 | 0.334 | 0.429 | 0.519 |

### P800

| Format | Operation | P50 | P90 | P99 | P99.9 |
|:---|:---|---:|---:|---:|---:|
| JSON | serialize | 0.518 | 0.599 | 0.862 | 1.001 |
| JSON | deserialize | 0.933 | 1.130 | 1.930 | 3.313 |
| JSON | roundtrip | 1.425 | 1.581 | 2.208 | 3.055 |
| AVRO | serialize | 0.310 | 0.441 | 0.813 | 2.808 |
| AVRO | deserialize | 0.381 | 0.483 | 0.721 | 1.104 |
| AVRO | roundtrip | 0.700 | 0.907 | 1.278 | 1.982 |
| PROTOBUF | serialize | 0.094 | 0.155 | 0.250 | 0.482 |
| PROTOBUF | deserialize | 0.207 | 0.342 | 0.428 | 0.518 |
| PROTOBUF | roundtrip | 0.418 | 0.448 | 0.587 | 0.708 |

## Memory Allocation

> Lower is better (B/op)

### P100

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 35,594 | 56,521 | 92,115 |
| AVRO | 23,904 | 27,856 | 51,761 |
| PROTOBUF | 6,576 | 39,768 | 46,344 |

### P1000

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 343,447 | 541,071 | 884,554 |
| AVRO | 332,842 | 264,795 | 597,637 |
| PROTOBUF | 64,769 | 377,851 | 442,625 |

### P200

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 69,767 | 110,521 | 180,314 |
| AVRO | 47,272 | 54,249 | 101,521 |
| PROTOBUF | 12,976 | 76,968 | 89,945 |

### P400

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 138,154 | 218,857 | 357,083 |
| AVRO | 94,129 | 106,873 | 201,002 |
| PROTOBUF | 25,904 | 152,785 | 178,689 |

### P600

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 206,570 | 326,804 | 533,440 |
| AVRO | 173,697 | 159,498 | 333,195 |
| PROTOBUF | 38,856 | 228,273 | 267,130 |

### P800

| Format | Serialize | Deserialize | Roundtrip |
|:---|---:|---:|---:|
| JSON | 275,124 | 431,494 | 706,613 |
| AVRO | 187,890 | 212,170 | 400,060 |
| PROTOBUF | 51,841 | 305,225 | 357,070 |

### Memory Efficiency Ratio

> Ratio compared to JSON (lower is better, JSON = 1.0x)

#### Serialize

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x |
| AVRO | 0.67x | 0.97x | 0.68x | 0.68x | 0.84x | 0.68x |
| PROTOBUF | 0.18x | 0.19x | 0.19x | 0.19x | 0.19x | 0.19x |

#### Deserialize

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x |
| AVRO | 0.49x | 0.49x | 0.49x | 0.49x | 0.49x | 0.49x |
| PROTOBUF | 0.70x | 0.70x | 0.70x | 0.70x | 0.70x | 0.71x |

#### Roundtrip

| Format | P100 | P1000 | P200 | P400 | P600 | P800 |
|:---|---:|---:|---:|---:|---:|---:|
| JSON | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x | 1.00x |
| AVRO | 0.56x | 0.68x | 0.56x | 0.56x | 0.62x | 0.57x |
| PROTOBUF | 0.50x | 0.50x | 0.50x | 0.50x | 0.50x | 0.51x |

