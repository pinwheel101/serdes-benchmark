package com.benchmark.serdes.serializer;

import com.benchmark.serdes.model.TraceData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonTraceDataSerializer implements Serializer<TraceData> {

    private final ObjectMapper objectMapper;

    public JsonTraceDataSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonTraceDataSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public byte[] serialize(String topic, TraceData data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing TraceData to JSON", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
