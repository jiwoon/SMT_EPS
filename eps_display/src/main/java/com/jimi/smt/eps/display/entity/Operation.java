package com.jimi.smt.eps.display.entity;

import java.util.Date;

public class Operation {
    private Long id;

    private String operator;

    private Date time;

    private Integer type;

    private String result;

    private String lineseat;

    private String materialNo;

    private String oldMaterialNo;

    private String scanlineseat;

    private String remark;

    private String programId;

    private String line;

    private String workOrder;

    private Integer boardType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result == null ? null : result.trim();
    }

    public String getLineseat() {
        return lineseat;
    }

    public void setLineseat(String lineseat) {
        this.lineseat = lineseat == null ? null : lineseat.trim();
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo == null ? null : materialNo.trim();
    }

    public String getOldMaterialNo() {
        return oldMaterialNo;
    }

    public void setOldMaterialNo(String oldMaterialNo) {
        this.oldMaterialNo = oldMaterialNo == null ? null : oldMaterialNo.trim();
    }

    public String getScanlineseat() {
        return scanlineseat;
    }

    public void setScanlineseat(String scanlineseat) {
        this.scanlineseat = scanlineseat == null ? null : scanlineseat.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId == null ? null : programId.trim();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line == null ? null : line.trim();
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
}