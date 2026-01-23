package com.benchmark.serdes.generator;

import com.benchmark.serdes.model.Context;
import com.benchmark.serdes.model.Parameter;
import com.benchmark.serdes.model.TraceData;

import java.util.List;
import java.util.stream.Collectors;

public class TraceDataConverter {

    // Convert POJO to Avro
    public static com.benchmark.serdes.avro.TraceData toAvro(TraceData pojo) {
        return com.benchmark.serdes.avro.TraceData.newBuilder()
            .setCommand(pojo.getCommand())
            .setFab(pojo.getFab())
            .setEqpId(pojo.getEqpId())
            .setCollectionInterval(pojo.getCollectionInterval())
            .setEventTime(pojo.getEventTime())
            .setFdcChambId(pojo.getFdcChambId())
            .setTxid(pojo.getTxid())
            .setParameterList(toAvroParameters(pojo.getParameterList()))
            .setContext(toAvroContext(pojo.getContext()))
            .build();
    }

    private static List<com.benchmark.serdes.avro.Parameter> toAvroParameters(List<Parameter> pojos) {
        return pojos.stream()
            .map(TraceDataConverter::toAvroParameter)
            .collect(Collectors.toList());
    }

    private static com.benchmark.serdes.avro.Parameter toAvroParameter(Parameter pojo) {
        return com.benchmark.serdes.avro.Parameter.newBuilder()
            .setName(pojo.getName())
            .setAlias(pojo.getAlias())
            .setSvid(pojo.getSvid())
            .setParamValue(pojo.getParamValue())
            .setTarget(pojo.getTarget())
            .setLsl(pojo.getLsl())
            .setUsl(pojo.getUsl())
            .setLcl(pojo.getLcl())
            .setUcl(pojo.getUcl())
            .setFaultCode(pojo.getFaultCode())
            .setSpecType(pojo.getSpecType())
            .setComment(pojo.getComment())
            .setModelName(pojo.getModelName())
            .setSpecMId(pojo.getSpecMId())
            .build();
    }

    private static com.benchmark.serdes.avro.Context toAvroContext(Context pojo) {
        return com.benchmark.serdes.avro.Context.newBuilder()
            .setBatchId(pojo.getBatchId())
            .setBatchTyp(pojo.getBatchTyp())
            .setSlotNo(pojo.getSlotNo())
            .setLotId(pojo.getLotId())
            .setLotCd(pojo.getLotCd())
            .setOperId(pojo.getOperId())
            .setOperDesc(pojo.getOperDesc())
            .setPortNo(pojo.getPortNo())
            .setMesRecipeId(pojo.getMesRecipeId())
            .setRecipeId(pojo.getRecipeId())
            .setProdId(pojo.getProdId())
            .setReticleId(pojo.getReticleId())
            .setBatchLotListVal(pojo.getBatchLotListVal())
            .setStepId(pojo.getStepId())
            .setStepNm(pojo.getStepNm())
            .setSubstId(pojo.getSubstId())
            .setStatCd(pojo.getStatCd())
            .setUnitCd(pojo.getUnitCd())
            .setStartStepId(pojo.getStartStepId())
            .setEndStepId(pojo.getEndStepId())
            .setPutId(pojo.getPutId())
            .setZone(pojo.getZone())
            .build();
    }

    // Convert POJO to Protobuf
    public static com.benchmark.serdes.proto.TraceData toProtobuf(TraceData pojo) {
        return com.benchmark.serdes.proto.TraceData.newBuilder()
            .setCommand(pojo.getCommand())
            .setFab(pojo.getFab())
            .setEqpId(pojo.getEqpId())
            .setCollectionInterval(pojo.getCollectionInterval())
            .setEventTime(pojo.getEventTime())
            .setFdcChambId(pojo.getFdcChambId())
            .setTxid(pojo.getTxid())
            .addAllParameterList(toProtobufParameters(pojo.getParameterList()))
            .setContext(toProtobufContext(pojo.getContext()))
            .build();
    }

    private static List<com.benchmark.serdes.proto.Parameter> toProtobufParameters(List<Parameter> pojos) {
        return pojos.stream()
            .map(TraceDataConverter::toProtobufParameter)
            .collect(Collectors.toList());
    }

    private static com.benchmark.serdes.proto.Parameter toProtobufParameter(Parameter pojo) {
        com.benchmark.serdes.proto.Parameter.Builder builder = com.benchmark.serdes.proto.Parameter.newBuilder()
            .setName(pojo.getName())
            .setAlias(pojo.getAlias())
            .setParamValue(pojo.getParamValue())
            .setTarget(pojo.getTarget())
            .setLsl(pojo.getLsl())
            .setUsl(pojo.getUsl())
            .setLcl(pojo.getLcl())
            .setUcl(pojo.getUcl())
            .setFaultCode(pojo.getFaultCode())
            .setSpecType(pojo.getSpecType())
            .setModelName(pojo.getModelName());

        if (pojo.getSvid() != null) {
            builder.setSvid(pojo.getSvid());
        }
        if (pojo.getComment() != null) {
            builder.setComment(pojo.getComment());
        }
        if (pojo.getSpecMId() != null) {
            builder.setSpecMId(pojo.getSpecMId());
        }

        return builder.build();
    }

    private static com.benchmark.serdes.proto.Context toProtobufContext(Context pojo) {
        return com.benchmark.serdes.proto.Context.newBuilder()
            .setBatchId(pojo.getBatchId())
            .setBatchTyp(pojo.getBatchTyp())
            .setSlotNo(pojo.getSlotNo())
            .setLotId(pojo.getLotId())
            .setLotCd(pojo.getLotCd())
            .setOperId(pojo.getOperId())
            .setOperDesc(pojo.getOperDesc())
            .setPortNo(pojo.getPortNo())
            .setMesRecipeId(pojo.getMesRecipeId())
            .setRecipeId(pojo.getRecipeId())
            .setProdId(pojo.getProdId())
            .setReticleId(pojo.getReticleId())
            .setBatchLotListVal(pojo.getBatchLotListVal())
            .setStepId(pojo.getStepId())
            .setStepNm(pojo.getStepNm())
            .setSubstId(pojo.getSubstId())
            .setStatCd(pojo.getStatCd())
            .setUnitCd(pojo.getUnitCd())
            .setStartStepId(pojo.getStartStepId())
            .setEndStepId(pojo.getEndStepId())
            .setPutId(pojo.getPutId())
            .setZone(pojo.getZone())
            .build();
    }
}
