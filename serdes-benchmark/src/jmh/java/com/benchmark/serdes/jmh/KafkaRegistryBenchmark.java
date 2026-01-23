package com.benchmark.serdes.jmh;

import com.benchmark.serdes.generator.PayloadSize;
import com.benchmark.serdes.generator.TraceDataConverter;
import com.benchmark.serdes.generator.TraceDataGenerator;
import com.benchmark.serdes.model.TraceData;
import io.apicurio.registry.serde.SerdeConfig;
import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import io.apicurio.registry.serde.avro.AvroKafkaSerializer;
import io.apicurio.registry.serde.protobuf.ProtobufKafkaDeserializer;
import io.apicurio.registry.serde.protobuf.ProtobufKafkaSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.openjdk.jmh.annotations.*;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 2, jvmArgs = {"-Xms4g", "-Xmx4g", "-XX:+UseG1GC"})
public class KafkaRegistryBenchmark {

    private static final String BOOTSTRAP_SERVERS = System.getenv().getOrDefault(
        "KAFKA_BOOTSTRAP_SERVERS", "localhost:29092");
    private static final String REGISTRY_URL = System.getenv().getOrDefault(
        "APICURIO_REGISTRY_URL", "http://localhost:8080/apis/registry/v3");

    @Param({"P100", "P200", "P400", "P600", "P800", "P1000"})
    private PayloadSize payloadSize;

    // Test data
    private com.benchmark.serdes.avro.TraceData avroData;
    private com.benchmark.serdes.proto.TraceData protobufData;

    // Topics
    private String avroTopic;
    private String protobufTopic;

    // Avro Kafka clients with Registry
    private KafkaProducer<String, com.benchmark.serdes.avro.TraceData> avroProducer;
    private KafkaConsumer<String, com.benchmark.serdes.avro.TraceData> avroConsumer;

    // Protobuf Kafka clients with Registry
    private KafkaProducer<String, com.benchmark.serdes.proto.TraceData> protobufProducer;
    private KafkaConsumer<String, com.benchmark.serdes.proto.TraceData> protobufConsumer;

    @Setup(Level.Trial)
    public void setup() {
        // Generate test data
        TraceDataGenerator generator = new TraceDataGenerator();
        TraceData pojoData = generator.generate(payloadSize.getParameterCount());
        avroData = TraceDataConverter.toAvro(pojoData);
        protobufData = TraceDataConverter.toProtobuf(pojoData);

        // Create unique topics for this benchmark run
        String runId = UUID.randomUUID().toString().substring(0, 8);
        avroTopic = "benchmark-registry-avro-" + payloadSize + "-" + runId;
        protobufTopic = "benchmark-registry-protobuf-" + payloadSize + "-" + runId;

        // Initialize producers and consumers with Schema Registry
        initAvroClients();
        initProtobufClients();
    }

    private void initAvroClients() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroKafkaSerializer.class.getName());
        producerProps.put(ProducerConfig.ACKS_CONFIG, "1");
        producerProps.put(SerdeConfig.REGISTRY_URL, REGISTRY_URL);
        producerProps.put(SerdeConfig.AUTO_REGISTER_ARTIFACT, "true");
        avroProducer = new KafkaProducer<>(producerProps);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark-registry-avro-" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroKafkaDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(SerdeConfig.REGISTRY_URL, REGISTRY_URL);
        avroConsumer = new KafkaConsumer<>(consumerProps);
        avroConsumer.subscribe(Collections.singletonList(avroTopic));
    }

    private void initProtobufClients() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProtobufKafkaSerializer.class.getName());
        producerProps.put(ProducerConfig.ACKS_CONFIG, "1");
        producerProps.put(SerdeConfig.REGISTRY_URL, REGISTRY_URL);
        producerProps.put(SerdeConfig.AUTO_REGISTER_ARTIFACT, "true");
        protobufProducer = new KafkaProducer<>(producerProps);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark-registry-protobuf-" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ProtobufKafkaDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(SerdeConfig.REGISTRY_URL, REGISTRY_URL);
        protobufConsumer = new KafkaConsumer<>(consumerProps);
        protobufConsumer.subscribe(Collections.singletonList(protobufTopic));
    }

    @TearDown(Level.Trial)
    public void teardown() {
        if (avroProducer != null) avroProducer.close();
        if (avroConsumer != null) avroConsumer.close();
        if (protobufProducer != null) protobufProducer.close();
        if (protobufConsumer != null) protobufConsumer.close();
    }

    // ==================== Avro with Registry Benchmarks ====================

    @Benchmark
    public void avro_registry_produce() throws Exception {
        avroProducer.send(new ProducerRecord<>(avroTopic, "key", avroData)).get();
    }

    // ==================== Protobuf with Registry Benchmarks ====================

    @Benchmark
    public void protobuf_registry_produce() throws Exception {
        protobufProducer.send(new ProducerRecord<>(protobufTopic, "key", protobufData)).get();
    }
}
