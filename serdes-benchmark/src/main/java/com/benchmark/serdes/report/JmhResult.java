package com.benchmark.serdes.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * JMH 벤치마크 결과를 표현하는 모델 클래스
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JmhResult {

    @JsonProperty("benchmark")
    private String benchmark;

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("params")
    private Map<String, String> params;

    @JsonProperty("primaryMetric")
    private PrimaryMetric primaryMetric;

    @JsonProperty("secondaryMetrics")
    private Map<String, SecondaryMetric> secondaryMetrics;

    public String getBenchmark() {
        return benchmark;
    }

    public String getMode() {
        return mode;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public PrimaryMetric getPrimaryMetric() {
        return primaryMetric;
    }

    public Map<String, SecondaryMetric> getSecondaryMetrics() {
        return secondaryMetrics;
    }

    public String getPayloadSize() {
        return params != null ? params.get("payloadSize") : null;
    }

    /**
     * 벤치마크 이름에서 포맷 추출 (avro, json, protobuf)
     */
    public String getFormat() {
        if (benchmark == null) return null;
        String methodName = benchmark.substring(benchmark.lastIndexOf('.') + 1);
        int underscoreIdx = methodName.indexOf('_');
        return underscoreIdx > 0 ? methodName.substring(0, underscoreIdx) : methodName;
    }

    /**
     * 벤치마크 이름에서 작업 타입 추출 (serialize, deserialize, roundtrip)
     */
    public String getOperation() {
        if (benchmark == null) return null;
        String methodName = benchmark.substring(benchmark.lastIndexOf('.') + 1);
        int underscoreIdx = methodName.indexOf('_');
        return underscoreIdx > 0 ? methodName.substring(underscoreIdx + 1) : "";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PrimaryMetric {
        @JsonProperty("score")
        private double score;

        @JsonProperty("scoreError")
        private double scoreError;

        @JsonProperty("scoreUnit")
        private String scoreUnit;

        @JsonProperty("scorePercentiles")
        private Map<String, Double> scorePercentiles;

        public double getScore() {
            return score;
        }

        public double getScoreError() {
            return scoreError;
        }

        public String getScoreUnit() {
            return scoreUnit;
        }

        public Map<String, Double> getScorePercentiles() {
            return scorePercentiles;
        }

        public double getPercentile(String key) {
            return scorePercentiles != null ? scorePercentiles.getOrDefault(key, 0.0) : 0.0;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SecondaryMetric {
        @JsonProperty("score")
        private double score;

        @JsonProperty("scoreUnit")
        private String scoreUnit;

        public double getScore() {
            return score;
        }

        public String getScoreUnit() {
            return scoreUnit;
        }
    }
}
