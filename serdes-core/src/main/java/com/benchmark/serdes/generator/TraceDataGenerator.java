package com.benchmark.serdes.generator;

import com.benchmark.serdes.model.Context;
import com.benchmark.serdes.model.Parameter;
import com.benchmark.serdes.model.TraceData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TraceDataGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String[] PARAM_NAMES = {
        "VIR_SCR_TOXIC_GAS_FLOW_LEVEL_DETECT",
        "CHAMBER_PRESSURE_ACTUAL",
        "RF_POWER_FORWARD",
        "RF_POWER_REFLECTED",
        "SUBSTRATE_TEMPERATURE",
        "GAS_FLOW_RATE_N2",
        "GAS_FLOW_RATE_O2",
        "GAS_FLOW_RATE_AR",
        "VACUUM_PRESSURE",
        "HEATER_TEMPERATURE",
        "COOLING_WATER_FLOW",
        "EXHAUST_PRESSURE",
        "CHAMBER_HUMIDITY",
        "PROCESS_TIME_ELAPSED",
        "WAFER_ROTATION_SPEED"
    };

    private final Random random;

    public TraceDataGenerator() {
        this.random = new Random(42L); // Fixed seed for reproducibility
    }

    public TraceDataGenerator(long seed) {
        this.random = new Random(seed);
    }

    public TraceData generate(int parameterCount) {
        String eventTime = LocalDateTime.now().format(DATE_FORMAT);
        String eqpId = "6DGP" + (4200 + random.nextInt(100));
        String txid = eventTime.replace("-", "").replace(":", "").replace(" ", "").replace(".", "")
                      + "_" + eqpId + "_PM1";

        List<Parameter> parameters = generateParameters(parameterCount);
        Context context = generateContext();

        return new TraceData(
            "TRACE_DATA",
            "M16",
            eqpId,
            1.0,
            eventTime,
            eqpId + "_PM1",
            txid,
            parameters,
            context
        );
    }

    private List<Parameter> generateParameters(int count) {
        List<Parameter> parameters = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            parameters.add(generateParameter(i));
        }
        return parameters;
    }

    private Parameter generateParameter(int index) {
        String baseName = PARAM_NAMES[index % PARAM_NAMES.length];
        String name = baseName + "_" + (index / PARAM_NAMES.length);
        double value = random.nextDouble() * 100;

        return new Parameter(
            name,
            name,
            null,
            String.format("%.10f", value),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            null,
            "",
            null
        );
    }

    private Context generateContext() {
        return new Context(
            "N/A",
            "N",
            "",
            "TE2ZL" + random.nextInt(100),
            "6E2",
            "A1956000C",
            "ISO GF NIT DEP",
            "",
            "EX_SIGE_R1_PM1",
            "EX_SIGE_R1_PM1",
            "H58GCXAXXXX074N-AAA01-CX001",
            "",
            "",
            String.valueOf(random.nextInt(10)),
            "PURGE",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        );
    }
}
