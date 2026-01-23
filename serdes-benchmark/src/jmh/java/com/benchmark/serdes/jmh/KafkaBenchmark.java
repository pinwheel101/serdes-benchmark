package com.benchmark.serdes.jmh;

import com.benchmark.serdes.generator.PayloadSize;
import com.benchmark.serdes.generator.TraceDataConverter;
import com.benchmark.serdes.generator.TraceDataGenerator;
import com.benchmark.serdes.model.TraceData;
import com.benchmark.serdes.serializer.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.openjdk.jmh.annotations.*;

import java.time.Duration;
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
public class KafkaBenchmark {

    private static final String BOOTSTRAP_SERVERS = System.getenv().getOrDefault(
        "KAFKA_BOOTSTRAP_SERVERS", "localhost:29092");

    @Param({"P100", "P200", "P400", "P600", "P800", "P1000"})
    private PayloadSize payloadSize;

    // Test data
    private TraceData pojoData;
    private com.benchmark.serdes.avro.TraceData avroData;
    private com.benchmark.serdes.proto.TraceData protobufData;

    // Topics
    private String jsonTopic;
    private String avroTopic;
    private String protobufTopic;

    // JSON Kafka clients
    private KafkaProducer<String, TraceData> jsonProducer;
    private KafkaConsumer<String, TraceData> jsonConsumer;

    // Avro Kafka clients
    private KafkaProducer<String, com.benchmark.serdes.avro.TraceData> avroProducer;
    private KafkaConsumer<String, com.benchmark.serdes.avro.TraceData> avroConsumer;

    // Protobuf Kafka clients
    private KafkaProducer<String, com.benchmark.serdes.proto.TraceData> protobufProducer;
    private KafkaConsumer<String, com.benchmark.serdes.proto.TraceData> protobufConsumer;

    @Setup(Level.Trial)
    public void setup() {
        // Generate test data
        TraceDataGenerator generator = new TraceDataGenerator();
        pojoData = generator.generate(payloadSize.getParameterCount());
        avroData = TraceDataConverter.toAvro(pojoData);
        protobufData = TraceDataConverter.toProtobuf(pojoData);

        // Create unique topics for this benchmark run
        String runId = UUID.randomUUID().toString().substring(0, 8);
        jsonTopic = "benchmark-json-" + payloadSize + "-" + runId;
        avroTopic = "benchmark-avro-" + payloadSize + "-" + runId;
        protobufTopic = "benchmark-protobuf-" + payloadSize + "-" + runId;

        // Initialize producers and consumers
        initJsonClients();
        initAvroClients();
        initProtobufClients();
    }

    private void initJsonClients() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonTraceDataSerializer.class.getName());
        producerProps.put(ProducerConfig.ACKS_CONFIG, "1");
        jsonProducer = new KafkaProducer<>(producerProps);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark-json-" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonTraceDataDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        jsonConsumer = new KafkaConsumer<>(consumerProps);
        jsonConsumer.subscribe(Collections.singletonList(jsonTopic));
    }

    private void initAvroClients() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroTraceDataSerializer.class.getName());
        producerProps.put(ProducerConfig.ACKS_CONFIG, "1");
        avroProducer = new KafkaProducer<>(producerProps);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark-avro-" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroTraceDataDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        avroConsumer = new KafkaConsumer<>(consumerProps);
        avroConsumer.subscribe(Collections.singletonList(avroTopic));
    }

    private void initProtobufClients() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProtobufTraceDataSerializer.class.getName());
        producerProps.put(ProducerConfig.ACKS_CONFIG, "1");
        protobufProducer = new KafkaProducer<>(producerProps);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "benchmark-protobuf-" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ProtobufTraceDataDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        protobufConsumer = new KafkaConsumer<>(consumerProps);
        protobufConsumer.subscribe(Collections.singletonList(protobufTopic));
    }

    @TearDown(Level.Trial)
    public void teardown() {
        if (jsonProducer != null) jsonProducer.close();
        if (jsonConsumer != null) jsonConsumer.close();
        if (avroProducer != null) avroProducer.close();
        if (avroConsumer != null) avroConsumer.close();
        if (protobufProducer != null) protobufProducer.close();
        if (protobufConsumer != null) protobufConsumer.close();
    }

    // ==================== JSON Kafka Benchmarks ====================

    @Benchmark
    public void json_kafka_produce() throws Exception {
        jsonProducer.send(new ProducerRecord<>(jsonTopic, "key", pojoData)).get();
    }

    // ==================== Avro Kafka Benchmarks ====================

    @Benchmark
    public void avro_kafka_produce() throws Exception {
        avroProducer.send(new ProducerRecord<>(avroTopic, "key", avroData)).get();
    }

    // ==================== Protobuf Kafka Benchmarks ====================

    @Benchmark
    public void protobuf_kafka_produce() throws Exception {
        protobufProducer.send(new ProducerRecord<>(protobufTopic, "key", protobufData)).get();
    }
}
