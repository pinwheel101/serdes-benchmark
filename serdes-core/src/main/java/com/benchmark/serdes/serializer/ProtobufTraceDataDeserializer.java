package com.benchmark.serdes.serializer;

import com.benchmark.serdes.proto.TraceData;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ProtobufTraceDataDeserializer implements Deserializer<TraceData> {

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
            return TraceData.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            throw new SerializationException("Error deserializing Protobuf to TraceData", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
