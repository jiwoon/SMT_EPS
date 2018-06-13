package com.jimi.smt.eps_server.entity;

public class ProgramItem extends ProgramItemKey {
    private Boolean alternative;

    private String specitification;

    private String position;

    private Integer quantity;

    private Integer serialNo;

    public Boolean getAlternative() {
        return alternative;
    }

    public void setAlternative(Boolean alternative) {
        this.alternative = alternative;
    }

    public String getSpecitification() {
        return specitification;
    }

    public void setSpecitification(String specitification) {
        this.specitification = specitification == null ? null : specitification.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }
}