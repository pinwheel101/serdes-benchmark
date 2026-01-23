package com.benchmark.serdes.jmh;

import com.benchmark.serdes.generator.PayloadSize;
import com.benchmark.serdes.generator.TraceDataConverter;
import com.benchmark.serdes.generator.TraceDataGenerator;
import com.benchmark.serdes.model.TraceData;
import com.benchmark.serdes.serializer.*;

public class SizeBenchmark {

    public static void main(String[] args) {
        TraceDataGenerator generator = new TraceDataGenerator();

        System.out.println("=".repeat(80));
        System.out.println("SERIALIZED SIZE COMPARISON");
        System.out.println("=".repeat(80));
        System.out.printf("%-10s | %12s | %12s | %12s | %15s | %15s%n",
            "Size", "JSON", "Avro", "Protobuf", "Avro vs JSON", "Proto vs JSON");
        System.out.println("-".repeat(80));

        JsonTraceDataSerializer jsonSerializer = new JsonTraceDataSerializer();
        AvroTraceDataSerializer avroSerializer = new AvroTraceDataSerializer();
        ProtobufTraceDataSerializer protobufSerializer = new ProtobufTraceDataSerializer();

        for (PayloadSize size : PayloadSize.values()) {
            TraceData pojo = generator.generate(size.getParameterCount());
            com.benchmark.serdes.avro.TraceData avroData = TraceDataConverter.toAvro(pojo);
            com.benchmark.serdes.proto.TraceData protobufData = TraceDataConverter.toProtobuf(pojo);

            int jsonBytes = jsonSerializer.serialize(null, pojo).length;
            int avroBytes = avroSerializer.serialize(null, avroData).length;
            int protobufBytes = protobufSerializer.serialize(null, protobufData).length;

            double avroRatio = (double) avroBytes / jsonBytes * 100;
            double protobufRatio = (double) protobufBytes / jsonBytes * 100;

            System.out.printf("%-10s | %,12d | %,12d | %,12d | %14.1f%% | %14.1f%%%n",
                size.name(),
                jsonBytes,
                avroBytes,
                protobufBytes,
                avroRatio,
                protobufRatio);
        }

        System.out.println("=".repeat(80));
    }
}
