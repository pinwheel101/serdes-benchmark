package com.benchmark.serdes.serializer;

import com.benchmark.serdes.avro.TraceData;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class AvroTraceDataSerializer implements Serializer<TraceData> {

    private final DatumWriter<TraceData> datumWriter;
    private final EncoderFactory encoderFactory;

    public AvroTraceDataSerializer() {
        this.datumWriter = new SpecificDatumWriter<>(TraceData.class);
        this.encoderFactory = EncoderFactory.get();
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
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = encoderFactory.binaryEncoder(out, null);
            datumWriter.write(data, encoder);
            encoder.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Error serializing TraceData to Avro", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
