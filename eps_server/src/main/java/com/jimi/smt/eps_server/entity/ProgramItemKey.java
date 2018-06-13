package com.jimi.smt.eps_server.entity;

public class ProgramItemKey {
    private String programId;

    private String lineseat;

    private String materialNo;

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId == null ? null : programId.trim();
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
}