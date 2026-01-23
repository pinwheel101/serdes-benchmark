package com.benchmark.serdes.serializer;

import com.benchmark.serdes.model.TraceData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class JsonTraceDataDeserializer implements Deserializer<TraceData> {

    private final ObjectMapper objectMapper;

    public JsonTraceDataDeserializer() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonTraceDataDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public TraceData deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(data, TraceData.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing JSON to TraceData", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
