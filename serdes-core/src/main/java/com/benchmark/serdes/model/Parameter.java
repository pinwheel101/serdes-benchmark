package com.benchmark.serdes.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parameter {
    private String name;
    private String alias;
    private String svid;
    private String paramValue;
    private String target;
    private String lsl;
    private String usl;
    private String lcl;
    private String ucl;
    private String faultCode;
    private String specType;
    private String comment;
    private String modelName;
    private String specMId;

    public Parameter() {}

    public Parameter(String name, String alias, String svid, String paramValue,
                     String target, String lsl, String usl, String lcl, String ucl,
                     String faultCode, String specType, String comment,
                     String modelName, String specMId) {
        this.name = name;
        this.alias = alias;
        this.svid = svid;
        this.paramValue = paramValue;
        this.target = target;
        this.lsl = lsl;
        this.usl = usl;
        this.lcl = lcl;
        this.ucl = ucl;
        this.faultCode = faultCode;
        this.specType = specType;
        this.comment = comment;
        this.modelName = modelName;
        this.specMId = specMId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getSvid() { return svid; }
    public void setSvid(String svid) { this.svid = svid; }

    public String getParamValue() { return paramValue; }
    public void setParamValue(String paramValue) { this.paramValue = paramValue; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public String getLsl() { return lsl; }
    public void setLsl(String lsl) { this.lsl = lsl; }

    public String getUsl() { return usl; }
    public void setUsl(String usl) { this.usl = usl; }

    public String getLcl() { return lcl; }
    public void setLcl(String lcl) { this.lcl = lcl; }

    public String getUcl() { return ucl; }
    public void setUcl(String ucl) { this.ucl = ucl; }

    public String getFaultCode() { return faultCode; }
    public void setFaultCode(String faultCode) { this.faultCode = faultCode; }

    public String getSpecType() { return specType; }
    public void setSpecType(String specType) { this.specType = specType; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getSpecMId() { return specMId; }
    public void setSpecMId(String specMId) { this.specMId = specMId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(name, parameter.name) &&
               Objects.equals(alias, parameter.alias) &&
               Objects.equals(svid, parameter.svid) &&
               Objects.equals(paramValue, parameter.paramValue) &&
               Objects.equals(target, parameter.target) &&
               Objects.equals(lsl, parameter.lsl) &&
               Objects.equals(usl, parameter.usl) &&
               Objects.equals(lcl, parameter.lcl) &&
               Objects.equals(ucl, parameter.ucl) &&
               Objects.equals(faultCode, parameter.faultCode) &&
               Objects.equals(specType, parameter.specType) &&
               Objects.equals(comment, parameter.comment) &&
               Objects.equals(modelName, parameter.modelName) &&
               Objects.equals(specMId, parameter.specMId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, alias, svid, paramValue, target, lsl, usl,
                           lcl, ucl, faultCode, specType, comment, modelName, specMId);
    }
}
