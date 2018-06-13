package com.jimi.smt.eps_server.entity;

public class MaterialInfo {
    private Integer id;

    private String materialNo;

    private Integer perifdOfValidity;

    private Integer enable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo == null ? null : materialNo.trim();
    }

    public Integer getPerifdOfValidity() {
        return perifdOfValidity;
    }

    public void setPerifdOfValidity(Integer perifdOfValidity) {
        this.perifdOfValidity = perifdOfValidity;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}