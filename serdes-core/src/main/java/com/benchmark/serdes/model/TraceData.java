package com.benchmark.serdes.model;

import java.util.List;
import java.util.Objects;

public class TraceData {
    private String command;
    private String fab;
    private String eqpId;
    private double collectionInterval;
    private String eventTime;
    private String fdcChambId;
    private String txid;
    private List<Parameter> parameterList;
    private Context context;

    public TraceData() {}

    public TraceData(String command, String fab, String eqpId, double collectionInterval,
                     String eventTime, String fdcChambId, String txid,
                     List<Parameter> parameterList, Context context) {
        this.command = command;
        this.fab = fab;
        this.eqpId = eqpId;
        this.collectionInterval = collectionInterval;
        this.eventTime = eventTime;
        this.fdcChambId = fdcChambId;
        this.txid = txid;
        this.parameterList = parameterList;
        this.context = context;
    }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }

    public String getFab() { return fab; }
    public void setFab(String fab) { this.fab = fab; }

    public String getEqpId() { return eqpId; }
    public void setEqpId(String eqpId) { this.eqpId = eqpId; }

    public double getCollectionInterval() { return collectionInterval; }
    public void setCollectionInterval(double collectionInterval) { this.collectionInterval = collectionInterval; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getFdcChambId() { return fdcChambId; }
    public void setFdcChambId(String fdcChambId) { this.fdcChambId = fdcChambId; }

    public String getTxid() { return txid; }
    public void setTxid(String txid) { this.txid = txid; }

    public List<Parameter> getParameterList() { return parameterList; }
    public void setParameterList(List<Parameter> parameterList) { this.parameterList = parameterList; }

    public Context getContext() { return context; }
    public void setContext(Context context) { this.context = context; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceData traceData = (TraceData) o;
        return Double.compare(traceData.collectionInterval, collectionInterval) == 0 &&
               Objects.equals(command, traceData.command) &&
               Objects.equals(fab, traceData.fab) &&
               Objects.equals(eqpId, traceData.eqpId) &&
               Objects.equals(eventTime, traceData.eventTime) &&
               Objects.equals(fdcChambId, traceData.fdcChambId) &&
               Objects.equals(txid, traceData.txid) &&
               Objects.equals(parameterList, traceData.parameterList) &&
               Objects.equals(context, traceData.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, fab, eqpId, collectionInterval, eventTime,
                           fdcChambId, txid, parameterList, context);
    }
}
