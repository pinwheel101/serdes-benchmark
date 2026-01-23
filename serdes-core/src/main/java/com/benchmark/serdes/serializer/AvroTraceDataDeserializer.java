package com.benchmark.serdes.serializer;

import com.benchmark.serdes.avro.TraceData;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class AvroTraceDataDeserializer implements Deserializer<TraceData> {

    private final DatumReader<TraceData> datumReader;
    private final DecoderFactory decoderFactory;

    public AvroTraceDataDeserializer() {
        this.datumReader = new SpecificDatumReader<>(TraceData.class);
        this.decoderFactory = DecoderFactory.get();
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
            BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
            return datumReader.read(null, decoder);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing Avro to TraceData", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
