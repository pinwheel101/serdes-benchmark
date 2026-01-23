package com.benchmark.serdes.jmh;

import com.benchmark.serdes.generator.PayloadSize;
import com.benchmark.serdes.generator.TraceDataConverter;
import com.benchmark.serdes.generator.TraceDataGenerator;
import com.benchmark.serdes.model.TraceData;
import com.benchmark.serdes.serializer.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgs = {"-Xms4g", "-Xmx4g", "-XX:+UseG1GC"})
public class PureSerializationBenchmark {

    @Param({"P100", "P200", "P400", "P600", "P800", "P1000"})
    private PayloadSize payloadSize;

    // POJOs
    private TraceData pojoData;
    private byte[] jsonBytes;
    private byte[] avroBytes;
    private byte[] protobufBytes;

    // Avro and Protobuf objects
    private com.benchmark.serdes.avro.TraceData avroData;
    private com.benchmark.serdes.proto.TraceData protobufData;

    // Serializers
    private JsonTraceDataSerializer jsonSerializer;
    private JsonTraceDataDeserializer jsonDeserializer;
    private AvroTraceDataSerializer avroSerializer;
    private AvroTraceDataDeserializer avroDeserializer;
    private ProtobufTraceDataSerializer protobufSerializer;
    private ProtobufTraceDataDeserializer protobufDeserializer;

    @Setup(Level.Trial)
    public void setup() {
        // Generate test data
        TraceDataGenerator generator = new TraceDataGenerator();
        pojoData = generator.generate(payloadSize.getParameterCount());

        // Convert to Avro and Protobuf
        avroData = TraceDataConverter.toAvro(pojoData);
        protobufData = TraceDataConverter.toProtobuf(pojoData);

        // Initialize serializers
        jsonSerializer = new JsonTraceDataSerializer();
        jsonDeserializer = new JsonTraceDataDeserializer();
        avroSerializer = new AvroTraceDataSerializer();
        avroDeserializer = new AvroTraceDataDeserializer();
        protobufSerializer = new ProtobufTraceDataSerializer();
        protobufDeserializer = new ProtobufTraceDataDeserializer();

        // Pre-serialize for deserialization benchmarks
        jsonBytes = jsonSerializer.serialize(null, pojoData);
        avroBytes = avroSerializer.serialize(null, avroData);
        protobufBytes = protobufSerializer.serialize(null, protobufData);
    }

    // ==================== JSON Benchmarks ====================

    @Benchmark
    public byte[] json_serialize() {
        return jsonSerializer.serialize(null, pojoData);
    }

    @Benchmark
    public TraceData json_deserialize() {
        return jsonDeserializer.deserialize(null, jsonBytes);
    }

    @Benchmark
    public TraceData json_roundtrip() {
        byte[] bytes = jsonSerializer.serialize(null, pojoData);
        return jsonDeserializer.deserialize(null, bytes);
    }

    // ==================== Avro Benchmarks ====================

    @Benchmark
    public byte[] avro_serialize() {
        return avroSerializer.serialize(null, avroData);
    }

    @Benchmark
    public com.benchmark.serdes.avro.TraceData avro_deserialize() {
        return avroDeserializer.deserialize(null, avroBytes);
    }

    @Benchmark
    public com.benchmark.serdes.avro.TraceData avro_roundtrip() {
        byte[] bytes = avroSerializer.serialize(null, avroData);
        return avroDeserializer.deserialize(null, bytes);
    }

    // ==================== Protobuf Benchmarks ====================

    @Benchmark
    public byte[] protobuf_serialize() {
        return protobufSerializer.serialize(null, protobufData);
    }

    @Benchmark
    public com.benchmark.serdes.proto.TraceData protobuf_deserialize() {
        return protobufDeserializer.deserialize(null, protobufBytes);
    }

    @Benchmark
    public com.benchmark.serdes.proto.TraceData protobuf_roundtrip() {
        byte[] bytes = protobufSerializer.serialize(null, protobufData);
        return protobufDeserializer.deserialize(null, bytes);
    }

    // ==================== Size measurement (not a benchmark, utility) ====================

    public int getJsonSize() { return jsonBytes.length; }
    public int getAvroSize() { return avroBytes.length; }
    public int getProtobufSize() { return protobufBytes.length; }
}
