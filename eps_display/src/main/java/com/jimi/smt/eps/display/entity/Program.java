package com.jimi.smt.eps.display.entity;

import java.util.Date;

public class Program {
    private String id;

    private String fileName;

    private String client;

    private String machineName;

    private String version;

    private String machineConfig;

    private String programNo;

    private String line;

    private String effectiveDate;

    private String pcbNo;

    private String bom;

    private String programName;

    private String auditor;

    private Date createTime;

    private String workOrder;

    private Integer boardType;

    private Integer state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client == null ? null : client.trim();
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName == null ? null : machineName.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getMachineConfig() {
        return machineConfig;
    }

    public void setMachineConfig(String machineConfig) {
        this.machineConfig = machineConfig == null ? null : machineConfig.trim();
    }

    public String getProgramNo() {
        return programNo;
    }

    public void setProgramNo(String programNo) {
        this.programNo = programNo == null ? null : programNo.trim();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line == null ? null : line.trim();
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate == null ? null : effectiveDate.trim();
    }

    public String getPcbNo() {
        return pcbNo;
    }

    public void setPcbNo(String pcbNo) {
        this.pcbNo = pcbNo == null ? null : pcbNo.trim();
    }

    public String getBom() {
        return bom;
    }

    public void setBom(String bom) {
        this.bom = bom == null ? null : bom.trim();
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName == null ? null : programName.trim();
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor == null ? null : auditor.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder == null ? null : workOrder.trim();
    }

    public Integer getBoardType() {
        return boardType;
    }

    public void setBoardType(Integer boardType) {
        this.boardType = boardType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}