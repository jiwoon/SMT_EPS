package com.jimi.smt.eps.alarmsocket.entity;

public class State {
    private Integer line;

    private Boolean alarming;

    private Boolean converypaused;

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Boolean getAlarming() {
        return alarming;
    }

    public void setAlarming(Boolean alarming) {
        this.alarming = alarming;
    }

    public Boolean getConverypaused() {
        return converypaused;
    }

    public void setConverypaused(Boolean converypaused) {
        this.converypaused = converypaused;
    }
}