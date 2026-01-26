#!/usr/bin/env python3
"""
JMH 벤치마크 결과를 마크다운 테이블로 변환하는 스크립트
"""

import json
import sys
from collections import defaultdict
from pathlib import Path


def load_results(json_path: str) -> list:
    with open(json_path, 'r') as f:
        return json.load(f)


def parse_benchmark_name(full_name: str) -> tuple[str, str]:
    """벤치마크 이름에서 포맷과 작업 타입 추출"""
    # com.benchmark.serdes.jmh.PureSerializationBenchmark.avro_deserialize -> avro, deserialize
    method_name = full_name.split('.')[-1]
    parts = method_name.split('_')
    if len(parts) >= 2:
        return parts[0], '_'.join(parts[1:])
    return method_name, ''


def generate_throughput_table(results: list) -> str:
    """Throughput (thrpt) 모드 테이블 생성"""
    # {payload_size: {format: {operation: score}}}
    data = defaultdict(lambda: defaultdict(dict))

    for item in results:
        if item['mode'] != 'thrpt':
            continue

        format_name, operation = parse_benchmark_name(item['benchmark'])
        payload_size = item['params']['payloadSize']
        score = item['primaryMetric']['score']
        unit = item['primaryMetric']['scoreUnit']

        data[payload_size][format_name][operation] = score

    if not data:
        return "No throughput data found.\n"

    # 테이블 생성
    lines = []
    lines.append("## Throughput (ops/ms) - Higher is better\n")

    for payload_size in sorted(data.keys()):
        formats = data[payload_size]

        # 작업 타입 수집
        operations = set()
        for ops in formats.values():
            operations.update(ops.keys())
        operations = sorted(operations)

        # 헤더
        lines.append(f"### {payload_size}\n")
        header = "| Format | " + " | ".join(op.replace('_', ' ').title() for op in operations) + " |"
        separator = "|--------|" + "|".join("-" * max(10, len(op) + 2) for op in operations) + "|"
        lines.append(header)
        lines.append(separator)

        # 데이터 행
        for format_name in sorted(formats.keys()):
            row = f"| {format_name.upper()} |"
            for op in operations:
                score = formats[format_name].get(op, 0)
                row += f" {score:.2f} |"
            lines.append(row)

        lines.append("")

    return "\n".join(lines)


def generate_latency_table(results: list) -> str:
    """Sample 모드 (레이턴시) 테이블 생성"""
    # {payload_size: {format: {operation: {percentile: value}}}}
    data = defaultdict(lambda: defaultdict(lambda: defaultdict(dict)))

    for item in results:
        if item['mode'] != 'sample':
            continue

        format_name, operation = parse_benchmark_name(item['benchmark'])
        payload_size = item['params']['payloadSize']
        percentiles = item['primaryMetric'].get('scorePercentiles', {})

        data[payload_size][format_name][operation] = {
            'p50': percentiles.get('50.0', 0),
            'p99': percentiles.get('99.0', 0),
            'p999': percentiles.get('99.9', 0),
        }

    if not data:
        return "No latency data found.\n"

    lines = []
    lines.append("## Latency (ms/op) - Lower is better\n")

    for payload_size in sorted(data.keys()):
        formats = data[payload_size]

        lines.append(f"### {payload_size}\n")
        lines.append("| Format | Operation | P50 | P99 | P99.9 |")
        lines.append("|--------|-----------|-----|-----|-------|")

        for format_name in sorted(formats.keys()):
            for operation in sorted(formats[format_name].keys()):
                stats = formats[format_name][operation]
                lines.append(
                    f"| {format_name.upper()} | {operation} | "
                    f"{stats['p50']:.3f} | {stats['p99']:.3f} | {stats['p999']:.3f} |"
                )

        lines.append("")

    return "\n".join(lines)


def generate_memory_table(results: list) -> str:
    """메모리 할당률 테이블 생성"""
    # {payload_size: {format: {operation: alloc_rate_norm}}}
    data = defaultdict(lambda: defaultdict(dict))

    for item in results:
        if item['mode'] != 'thrpt':
            continue

        format_name, operation = parse_benchmark_name(item['benchmark'])
        payload_size = item['params']['payloadSize']

        alloc_norm = item.get('secondaryMetrics', {}).get('gc.alloc.rate.norm', {})
        if alloc_norm:
            data[payload_size][format_name][operation] = alloc_norm.get('score', 0)

    if not data:
        return "No memory data found.\n"

    lines = []
    lines.append("## Memory Allocation (B/op) - Lower is better\n")

    for payload_size in sorted(data.keys()):
        formats = data[payload_size]

        operations = set()
        for ops in formats.values():
            operations.update(ops.keys())
        operations = sorted(operations)

        lines.append(f"### {payload_size}\n")
        header = "| Format | " + " | ".join(op.replace('_', ' ').title() for op in operations) + " |"
        separator = "|--------|" + "|".join("-" * max(12, len(op) + 2) for op in operations) + "|"
        lines.append(header)
        lines.append(separator)

        for format_name in sorted(formats.keys()):
            row = f"| {format_name.upper()} |"
            for op in operations:
                score = formats[format_name].get(op, 0)
                row += f" {score:,.0f} |"
            lines.append(row)

        lines.append("")

    return "\n".join(lines)


def generate_summary_table(results: list) -> str:
    """요약 테이블 생성 (모든 페이로드 크기 비교)"""
    # {format: {operation: {payload_size: score}}}
    data = defaultdict(lambda: defaultdict(dict))

    for item in results:
        if item['mode'] != 'thrpt':
            continue

        format_name, operation = parse_benchmark_name(item['benchmark'])
        payload_size = item['params']['payloadSize']
        score = item['primaryMetric']['score']

        data[format_name][operation][payload_size] = score

    if not data:
        return "No data found.\n"

    lines = []
    lines.append("## Summary: Throughput by Payload Size (ops/ms)\n")

    # 모든 페이로드 크기 수집
    all_sizes = set()
    for fmt in data.values():
        for ops in fmt.values():
            all_sizes.update(ops.keys())
    all_sizes = sorted(all_sizes)

    for operation in ['serialize', 'deserialize', 'roundtrip']:
        lines.append(f"### {operation.title()}\n")
        header = "| Format | " + " | ".join(all_sizes) + " |"
        separator = "|--------|" + "|".join("-" * 8 for _ in all_sizes) + "|"
        lines.append(header)
        lines.append(separator)

        for format_name in sorted(data.keys()):
            if operation not in data[format_name]:
                continue
            row = f"| {format_name.upper()} |"
            for size in all_sizes:
                score = data[format_name][operation].get(size, 0)
                row += f" {score:.1f} |"
            lines.append(row)

        lines.append("")

    return "\n".join(lines)


def main():
    if len(sys.argv) < 2:
        json_path = Path(__file__).parent.parent / "results" / "jmh-results.json"
    else:
        json_path = Path(sys.argv[1])

    if not json_path.exists():
        print(f"Error: File not found: {json_path}", file=sys.stderr)
        sys.exit(1)

    results = load_results(json_path)

    print("# JMH Benchmark Results\n")
    print(generate_throughput_table(results))
    print(generate_latency_table(results))
    print(generate_memory_table(results))
    print(generate_summary_table(results))


if __name__ == "__main__":
    main()
