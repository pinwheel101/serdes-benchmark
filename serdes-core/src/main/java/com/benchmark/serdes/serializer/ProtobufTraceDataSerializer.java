package com.benchmark.serdes.serializer;

import com.benchmark.serdes.proto.TraceData;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ProtobufTraceDataSerializer implements Serializer<TraceData> {

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
            return data.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("Error serializing TraceData to Protobuf", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
