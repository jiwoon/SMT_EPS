package com.jimi.smt.eps.alarmsocket.entity;

import java.util.Date;

public class Log {
    private Long id;

    private String protocol;

    private Short serialNo;

    private String data;

    private String senderIp;

    private String receiverIp;

    private Date time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol == null ? null : protocol.trim();
    }

    public Short getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Short serialNo) {
        this.serialNo = serialNo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public String getSenderIp() {
        return senderIp;
    }

    public void setSenderIp(String senderIp) {
        this.senderIp = senderIp == null ? null : senderIp.trim();
    }

    public String getReceiverIp() {
        return receiverIp;
    }

    public void setReceiverIp(String receiverIp) {
        this.receiverIp = receiverIp == null ? null : receiverIp.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}