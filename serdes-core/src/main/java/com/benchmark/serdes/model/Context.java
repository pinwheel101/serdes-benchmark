package com.benchmark.serdes.model;

import java.util.Objects;

public class Context {
    private String batchId;
    private String batchTyp;
    private String slotNo;
    private String lotId;
    private String lotCd;
    private String operId;
    private String operDesc;
    private String portNo;
    private String mesRecipeId;
    private String recipeId;
    private String prodId;
    private String reticleId;
    private String batchLotListVal;
    private String stepId;
    private String stepNm;
    private String substId;
    private String statCd;
    private String unitCd;
    private String startStepId;
    private String endStepId;
    private String putId;
    private String zone;

    public Context() {}

    public Context(String batchId, String batchTyp, String slotNo, String lotId,
                   String lotCd, String operId, String operDesc, String portNo,
                   String mesRecipeId, String recipeId, String prodId, String reticleId,
                   String batchLotListVal, String stepId, String stepNm, String substId,
                   String statCd, String unitCd, String startStepId, String endStepId,
                   String putId, String zone) {
        this.batchId = batchId;
        this.batchTyp = batchTyp;
        this.slotNo = slotNo;
        this.lotId = lotId;
        this.lotCd = lotCd;
        this.operId = operId;
        this.operDesc = operDesc;
        this.portNo = portNo;
        this.mesRecipeId = mesRecipeId;
        this.recipeId = recipeId;
        this.prodId = prodId;
        this.reticleId = reticleId;
        this.batchLotListVal = batchLotListVal;
        this.stepId = stepId;
        this.stepNm = stepNm;
        this.substId = substId;
        this.statCd = statCd;
        this.unitCd = unitCd;
        this.startStepId = startStepId;
        this.endStepId = endStepId;
        this.putId = putId;
        this.zone = zone;
    }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getBatchTyp() { return batchTyp; }
    public void setBatchTyp(String batchTyp) { this.batchTyp = batchTyp; }

    public String getSlotNo() { return slotNo; }
    public void setSlotNo(String slotNo) { this.slotNo = slotNo; }

    public String getLotId() { return lotId; }
    public void setLotId(String lotId) { this.lotId = lotId; }

    public String getLotCd() { return lotCd; }
    public void setLotCd(String lotCd) { this.lotCd = lotCd; }

    public String getOperId() { return operId; }
    public void setOperId(String operId) { this.operId = operId; }

    public String getOperDesc() { return operDesc; }
    public void setOperDesc(String operDesc) { this.operDesc = operDesc; }

    public String getPortNo() { return portNo; }
    public void setPortNo(String portNo) { this.portNo = portNo; }

    public String getMesRecipeId() { return mesRecipeId; }
    public void setMesRecipeId(String mesRecipeId) { this.mesRecipeId = mesRecipeId; }

    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    public String getProdId() { return prodId; }
    public void setProdId(String prodId) { this.prodId = prodId; }

    public String getReticleId() { return reticleId; }
    public void setReticleId(String reticleId) { this.reticleId = reticleId; }

    public String getBatchLotListVal() { return batchLotListVal; }
    public void setBatchLotListVal(String batchLotListVal) { this.batchLotListVal = batchLotListVal; }

    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }

    public String getStepNm() { return stepNm; }
    public void setStepNm(String stepNm) { this.stepNm = stepNm; }

    public String getSubstId() { return substId; }
    public void setSubstId(String substId) { this.substId = substId; }

    public String getStatCd() { return statCd; }
    public void setStatCd(String statCd) { this.statCd = statCd; }

    public String getUnitCd() { return unitCd; }
    public void setUnitCd(String unitCd) { this.unitCd = unitCd; }

    public String getStartStepId() { return startStepId; }
    public void setStartStepId(String startStepId) { this.startStepId = startStepId; }

    public String getEndStepId() { return endStepId; }
    public void setEndStepId(String endStepId) { this.endStepId = endStepId; }

    public String getPutId() { return putId; }
    public void setPutId(String putId) { this.putId = putId; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(batchId, context.batchId) &&
               Objects.equals(batchTyp, context.batchTyp) &&
               Objects.equals(slotNo, context.slotNo) &&
               Objects.equals(lotId, context.lotId) &&
               Objects.equals(lotCd, context.lotCd) &&
               Objects.equals(operId, context.operId) &&
               Objects.equals(operDesc, context.operDesc) &&
               Objects.equals(portNo, context.portNo) &&
               Objects.equals(mesRecipeId, context.mesRecipeId) &&
               Objects.equals(recipeId, context.recipeId) &&
               Objects.equals(prodId, context.prodId) &&
               Objects.equals(reticleId, context.reticleId) &&
               Objects.equals(batchLotListVal, context.batchLotListVal) &&
               Objects.equals(stepId, context.stepId) &&
               Objects.equals(stepNm, context.stepNm) &&
               Objects.equals(substId, context.substId) &&
               Objects.equals(statCd, context.statCd) &&
               Objects.equals(unitCd, context.unitCd) &&
               Objects.equals(startStepId, context.startStepId) &&
               Objects.equals(endStepId, context.endStepId) &&
               Objects.equals(putId, context.putId) &&
               Objects.equals(zone, context.zone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchId, batchTyp, slotNo, lotId, lotCd, operId,
                           operDesc, portNo, mesRecipeId, recipeId, prodId, reticleId,
                           batchLotListVal, stepId, stepNm, substId, statCd, unitCd,
                           startStepId, endStepId, putId, zone);
    }
}
