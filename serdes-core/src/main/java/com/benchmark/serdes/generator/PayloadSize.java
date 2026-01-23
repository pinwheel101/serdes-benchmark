package com.benchmark.serdes.generator;

public enum PayloadSize {
    P100(100),
    P200(200),
    P400(400),
    P600(600),
    P800(800),
    P1000(1000);

    private final int parameterCount;

    PayloadSize(int parameterCount) {
        this.parameterCount = parameterCount;
    }

    public int getParameterCount() {
        return parameterCount;
    }
}
