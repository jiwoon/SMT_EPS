package com.jimi.smt.eps.printer.entity;

import java.util.Date;

public class StockLog {
    private Integer id;

    private String timestamp;

    private String materialNo;

    private Integer quantity;

    private String operator;

    private Date operationTime;

    private String position;

    private String custom;

    private Date productionDate;

    private String targetWorkOrder;

    private String targetLine;

    private Integer targetBoardType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp == null ? null : timestamp.trim();
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo == null ? null : materialNo.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom == null ? null : custom.trim();
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public String getTargetWorkOrder() {
        return targetWorkOrder;
    }

    public void setTargetWorkOrder(String targetWorkOrder) {
        this.targetWorkOrder = targetWorkOrder == null ? null : targetWorkOrder.trim();
    }

    public String getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine == null ? null : targetLine.trim();
    }

    public Integer getTargetBoardType() {
        return targetBoardType;
    }

    public void setTargetBoardType(Integer targetBoardType) {
        this.targetBoardType = targetBoardType;
    }
}