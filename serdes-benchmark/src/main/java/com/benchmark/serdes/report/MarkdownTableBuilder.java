package com.benchmark.serdes.report;

import java.util.ArrayList;
import java.util.List;

/**
 * 마크다운 테이블을 생성하는 빌더 클래스
 */
public class MarkdownTableBuilder {

    private final List<String> headers = new ArrayList<>();
    private final List<Alignment> alignments = new ArrayList<>();
    private final List<List<String>> rows = new ArrayList<>();

    public enum Alignment {
        LEFT, CENTER, RIGHT
    }

    public MarkdownTableBuilder addHeader(String header) {
        headers.add(header);
        alignments.add(Alignment.LEFT);
        return this;
    }

    public MarkdownTableBuilder addHeader(String header, Alignment alignment) {
        headers.add(header);
        alignments.add(alignment);
        return this;
    }

    public MarkdownTableBuilder addHeaders(String... headerNames) {
        for (String header : headerNames) {
            addHeader(header);
        }
        return this;
    }

    public MarkdownTableBuilder addHeaders(Alignment alignment, String... headerNames) {
        for (String header : headerNames) {
            addHeader(header, alignment);
        }
        return this;
    }

    public MarkdownTableBuilder addRow(String... cells) {
        List<String> row = new ArrayList<>();
        for (String cell : cells) {
            row.add(cell);
        }
        rows.add(row);
        return this;
    }

    public MarkdownTableBuilder addRow(List<String> cells) {
        rows.add(new ArrayList<>(cells));
        return this;
    }

    public String build() {
        if (headers.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        // 헤더 행
        sb.append("|");
        for (String header : headers) {
            sb.append(" ").append(header).append(" |");
        }
        sb.append("\n");

        // 구분선
        sb.append("|");
        for (int i = 0; i < headers.size(); i++) {
            Alignment align = alignments.get(i);
            String separator = switch (align) {
                case LEFT -> ":---";
                case CENTER -> ":---:";
                case RIGHT -> "---:";
            };
            sb.append(separator).append("|");
        }
        sb.append("\n");

        // 데이터 행
        for (List<String> row : rows) {
            sb.append("|");
            for (int i = 0; i < headers.size(); i++) {
                String cell = i < row.size() ? row.get(i) : "";
                sb.append(" ").append(cell).append(" |");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public void clear() {
        headers.clear();
        alignments.clear();
        rows.clear();
    }
}
