package com.benchmark.serdes.report;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.benchmark.serdes.report.MarkdownTableBuilder.Alignment;

/**
 * JMH 벤치마크 결과를 마크다운 리포트로 변환하는 클래스
 */
public class BenchmarkReportGenerator {

    private static final List<String> OPERATIONS = List.of("serialize", "deserialize", "roundtrip");
    private static final List<String> FORMATS = List.of("json", "avro", "protobuf");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<JmhResult> results;

    public void loadResults(String jsonPath) throws IOException {
        File file = new File(jsonPath);
        results = objectMapper.readValue(file, new TypeReference<>() {});
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();

        // 헤더
        report.append("# JMH Benchmark Report\n\n");
        report.append("Generated: ").append(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");

        // 페이로드 크기 목록
        Set<String> payloadSizes = results.stream()
                .map(JmhResult::getPayloadSize)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        // 목차
        report.append("## Table of Contents\n\n");
        report.append("1. [Summary](#summary)\n");
        report.append("2. [Throughput by Payload Size](#throughput-by-payload-size)\n");
        report.append("3. [Detailed Results by Payload Size](#detailed-results-by-payload-size)\n");
        report.append("4. [Latency Percentiles](#latency-percentiles)\n");
        report.append("5. [Memory Allocation](#memory-allocation)\n\n");

        report.append("---\n\n");

        // 1. 요약 섹션
        report.append(generateSummarySection(payloadSizes));

        // 2. PayloadSize별 Throughput 비교
        report.append(generateThroughputComparisonSection(payloadSizes));

        // 3. PayloadSize별 상세 결과
        report.append(generateDetailedResultsSection(payloadSizes));

        // 4. Latency Percentiles
        report.append(generateLatencySection(payloadSizes));

        // 5. Memory Allocation
        report.append(generateMemorySection(payloadSizes));

        return report.toString();
    }

    private String generateSummarySection(Set<String> payloadSizes) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Summary\n\n");

        // Throughput 결과만 필터링
        Map<String, Map<String, Map<String, Double>>> data = groupThroughputData();

        for (String operation : OPERATIONS) {
            sb.append("### ").append(capitalize(operation)).append(" Throughput (ops/ms)\n\n");
            sb.append("> Higher is better\n\n");

            MarkdownTableBuilder table = new MarkdownTableBuilder();
            table.addHeader("Format");
            for (String size : payloadSizes) {
                table.addHeader(size, Alignment.RIGHT);
            }

            for (String format : FORMATS) {
                List<String> row = new ArrayList<>();
                row.add(format.toUpperCase());
                for (String size : payloadSizes) {
                    Double score = getScore(data, size, format, operation);
                    row.add(score != null ? String.format("%.2f", score) : "-");
                }
                table.addRow(row);
            }

            sb.append(table.build()).append("\n");
        }

        return sb.toString();
    }

    private String generateThroughputComparisonSection(Set<String> payloadSizes) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Throughput by Payload Size\n\n");

        Map<String, Map<String, Map<String, Double>>> data = groupThroughputData();

        // 각 작업별로 포맷 간 성능 비율 비교
        for (String operation : OPERATIONS) {
            sb.append("### ").append(capitalize(operation)).append(" - Performance Ratio\n\n");
            sb.append("> Ratio compared to JSON (JSON = 1.0x)\n\n");

            MarkdownTableBuilder table = new MarkdownTableBuilder();
            table.addHeader("Format");
            for (String size : payloadSizes) {
                table.addHeader(size, Alignment.RIGHT);
            }

            for (String format : FORMATS) {
                List<String> row = new ArrayList<>();
                row.add(format.toUpperCase());
                for (String size : payloadSizes) {
                    Double score = getScore(data, size, format, operation);
                    Double jsonScore = getScore(data, size, "json", operation);
                    if (score != null && jsonScore != null && jsonScore > 0) {
                        double ratio = score / jsonScore;
                        row.add(String.format("%.2fx", ratio));
                    } else {
                        row.add("-");
                    }
                }
                table.addRow(row);
            }

            sb.append(table.build()).append("\n");
        }

        return sb.toString();
    }

    private String generateDetailedResultsSection(Set<String> payloadSizes) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Detailed Results by Payload Size\n\n");

        Map<String, Map<String, Map<String, Double>>> data = groupThroughputData();

        for (String size : payloadSizes) {
            sb.append("### ").append(size).append("\n\n");

            MarkdownTableBuilder table = new MarkdownTableBuilder();
            table.addHeader("Format");
            for (String op : OPERATIONS) {
                table.addHeader(capitalize(op), Alignment.RIGHT);
            }

            for (String format : FORMATS) {
                List<String> row = new ArrayList<>();
                row.add(format.toUpperCase());
                for (String op : OPERATIONS) {
                    Double score = getScore(data, size, format, op);
                    row.add(score != null ? String.format("%.2f", score) : "-");
                }
                table.addRow(row);
            }

            sb.append(table.build()).append("\n");

            // 해당 페이로드 크기에서 최고 성능 표시
            sb.append("**Best performers:**\n");
            for (String op : OPERATIONS) {
                String best = findBestFormat(data, size, op);
                if (best != null) {
                    Double score = getScore(data, size, best, op);
                    sb.append(String.format("- %s: **%s** (%.2f ops/ms)\n", capitalize(op), best.toUpperCase(), score));
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String generateLatencySection(Set<String> payloadSizes) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Latency Percentiles\n\n");
        sb.append("> Lower is better (ms/op)\n\n");

        // Sample 모드 결과만 필터링
        List<JmhResult> sampleResults = results.stream()
                .filter(r -> "sample".equals(r.getMode()))
                .toList();

        for (String size : payloadSizes) {
            sb.append("### ").append(size).append("\n\n");

            MarkdownTableBuilder table = new MarkdownTableBuilder();
            table.addHeader("Format");
            table.addHeader("Operation");
            table.addHeader("P50", Alignment.RIGHT);
            table.addHeader("P90", Alignment.RIGHT);
            table.addHeader("P99", Alignment.RIGHT);
            table.addHeader("P99.9", Alignment.RIGHT);

            for (String format : FORMATS) {
                for (String op : OPERATIONS) {
                    Optional<JmhResult> result = sampleResults.stream()
                            .filter(r -> size.equals(r.getPayloadSize()))
                            .filter(r -> format.equals(r.getFormat()))
                            .filter(r -> op.equals(r.getOperation()))
                            .findFirst();

                    if (result.isPresent()) {
                        JmhResult.PrimaryMetric metric = result.get().getPrimaryMetric();
                        table.addRow(
                                format.toUpperCase(),
                                op,
                                String.format("%.3f", metric.getPercentile("50.0")),
                                String.format("%.3f", metric.getPercentile("90.0")),
                                String.format("%.3f", metric.getPercentile("99.0")),
                                String.format("%.3f", metric.getPercentile("99.9"))
                        );
                    }
                }
            }

            sb.append(table.build()).append("\n");
        }

        return sb.toString();
    }

    private String generateMemorySection(Set<String> payloadSizes) {
        StringBuilder sb = new StringBuilder();
        sb.append("## Memory Allocation\n\n");
        sb.append("> Lower is better (B/op)\n\n");

        // Throughput 결과에서 메모리 정보 추출
        List<JmhResult> thrptResults = results.stream()
                .filter(r -> "thrpt".equals(r.getMode()))
                .toList();

        for (String size : payloadSizes) {
            sb.append("### ").append(size).append("\n\n");

            MarkdownTableBuilder table = new MarkdownTableBuilder();
            table.addHeader("Format");
            for (String op : OPERATIONS) {
                table.addHeader(capitalize(op), Alignment.RIGHT);
            }

            for (String format : FORMATS) {
                List<String> row = new ArrayList<>();
                row.add(format.toUpperCase());

                for (String op : OPERATIONS) {
                    Optional<JmhResult> result = thrptResults.stream()
                            .filter(r -> size.equals(r.getPayloadSize()))
                            .filter(r -> format.equals(r.getFormat()))
                            .filter(r -> op.equals(r.getOperation()))
                            .findFirst();

                    if (result.isPresent() && result.get().getSecondaryMetrics() != null) {
                        JmhResult.SecondaryMetric allocNorm = result.get().getSecondaryMetrics()
                                .get("gc.alloc.rate.norm");
                        if (allocNorm != null) {
                            row.add(String.format("%,.0f", allocNorm.getScore()));
                        } else {
                            row.add("-");
                        }
                    } else {
                        row.add("-");
                    }
                }
                table.addRow(row);
            }

            sb.append(table.build()).append("\n");
        }

        // 메모리 사용량 비교 (JSON 대비)
        sb.append("### Memory Efficiency Ratio\n\n");
        sb.append("> Ratio compared to JSON (lower is better, JSON = 1.0x)\n\n");

        for (String operation : OPERATIONS) {
            sb.append("#### ").append(capitalize(operation)).append("\n\n");

            MarkdownTableBuilder table = new MarkdownTableBuilder();
            table.addHeader("Format");
            for (String size : payloadSizes) {
                table.addHeader(size, Alignment.RIGHT);
            }

            for (String format : FORMATS) {
                List<String> row = new ArrayList<>();
                row.add(format.toUpperCase());

                for (String size : payloadSizes) {
                    Double formatAlloc = getAllocNorm(thrptResults, size, format, operation);
                    Double jsonAlloc = getAllocNorm(thrptResults, size, "json", operation);

                    if (formatAlloc != null && jsonAlloc != null && jsonAlloc > 0) {
                        double ratio = formatAlloc / jsonAlloc;
                        row.add(String.format("%.2fx", ratio));
                    } else {
                        row.add("-");
                    }
                }
                table.addRow(row);
            }

            sb.append(table.build()).append("\n");
        }

        return sb.toString();
    }

    private Map<String, Map<String, Map<String, Double>>> groupThroughputData() {
        // {payloadSize: {format: {operation: score}}}
        Map<String, Map<String, Map<String, Double>>> data = new TreeMap<>();

        results.stream()
                .filter(r -> "thrpt".equals(r.getMode()))
                .forEach(r -> {
                    String size = r.getPayloadSize();
                    String format = r.getFormat();
                    String op = r.getOperation();
                    if (size != null && format != null && op != null) {
                        data.computeIfAbsent(size, k -> new HashMap<>())
                                .computeIfAbsent(format, k -> new HashMap<>())
                                .put(op, r.getPrimaryMetric().getScore());
                    }
                });

        return data;
    }

    private Double getScore(Map<String, Map<String, Map<String, Double>>> data,
                            String size, String format, String operation) {
        return data.getOrDefault(size, Map.of())
                .getOrDefault(format, Map.of())
                .get(operation);
    }

    private String findBestFormat(Map<String, Map<String, Map<String, Double>>> data,
                                  String size, String operation) {
        String best = null;
        double bestScore = 0;

        for (String format : FORMATS) {
            Double score = getScore(data, size, format, operation);
            if (score != null && score > bestScore) {
                bestScore = score;
                best = format;
            }
        }

        return best;
    }

    private Double getAllocNorm(List<JmhResult> thrptResults, String size, String format, String operation) {
        return thrptResults.stream()
                .filter(r -> size.equals(r.getPayloadSize()))
                .filter(r -> format.equals(r.getFormat()))
                .filter(r -> operation.equals(r.getOperation()))
                .filter(r -> r.getSecondaryMetrics() != null)
                .map(r -> r.getSecondaryMetrics().get("gc.alloc.rate.norm"))
                .filter(Objects::nonNull)
                .map(JmhResult.SecondaryMetric::getScore)
                .findFirst()
                .orElse(null);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void saveReport(String outputPath) throws IOException {
        String report = generateReport();
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.print(report);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java BenchmarkReportGenerator <jmh-results.json> [output.md]");
            System.err.println("  If output.md is not specified, prints to stdout");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = args.length > 1 ? args[1] : null;

        BenchmarkReportGenerator generator = new BenchmarkReportGenerator();
        try {
            generator.loadResults(inputPath);

            if (outputPath != null) {
                generator.saveReport(outputPath);
                System.out.println("Report saved to: " + outputPath);
            } else {
                System.out.println(generator.generateReport());
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
