package com.jimi.smt.eps.display.entity;

public class ProgramItemVisitKey {
    private String lineseat;

    private String materialNo;

    private String programId;

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

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId == null ? null : programId.trim();
    }
}