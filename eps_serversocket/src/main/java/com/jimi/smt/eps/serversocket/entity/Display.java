package com.jimi.smt.eps.serversocket.entity;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-07-19
 */
public class Display {
    private Integer id;

    private String line;

    private String workOrder;

    private Integer boardType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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