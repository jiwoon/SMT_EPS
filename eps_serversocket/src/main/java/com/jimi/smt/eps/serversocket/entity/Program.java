package com.jimi.smt.eps.serversocket.entity;

import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-07-19
 */
public class Program {
    private String id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 产品客户
     */
    private String client;

    /**
     * 机器名
     */
    private String machineName;

    /**
     * 版本
     */
    private String version;

    /**
     * 机器配置
     */
    private String machineConfig;

    /**
     * 程序编号
     */
    private String programNo;

    /**
     * 线别
     */
    private String line;

    /**
     * 生效日期
     */
    private String effectiveDate;

    /**
     * PCB编号
     */
    private String pcbNo;

    /**
     * BOM文件
     */
    private String bom;

    /**
     * 程序名
     */
    private String programName;

    /**
     * 审核者
     */
    private String auditor;

    /**
     * 创建日期，该日期为此数据进入数据库的日期
     */
    private Date createTime;

    /**
     * 工单
     */
    private String workOrder;

    /**
     * 0：默认 1：AB面 2：A面 3:B面
     */
    private Integer boardType;

    private Integer state;

    /**
     * 几联板
     */
    private Integer structure;

    /**
     * 计划生产
     */
    private Integer planProduct;

    /**
     * 已经生产
     */
    private Integer alreadyProduct;

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

    public Integer getStructure() {
        return structure;
    }

    public void setStructure(Integer structure) {
        this.structure = structure;
    }

    public Integer getPlanProduct() {
        return planProduct;
    }

    public void setPlanProduct(Integer planProduct) {
        this.planProduct = planProduct;
    }

    public Integer getAlreadyProduct() {
        return alreadyProduct;
    }

    public void setAlreadyProduct(Integer alreadyProduct) {
        this.alreadyProduct = alreadyProduct;
    }
}