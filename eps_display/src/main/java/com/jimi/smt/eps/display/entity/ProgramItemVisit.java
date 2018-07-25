package com.jimi.smt.eps.display.entity;

import java.util.Date;

public class ProgramItemVisit extends ProgramItemVisitKey {
    private String scanLineseat;

    private String scanMaterialNo;

    private Integer lastOperationType;

    private Integer storeIssueResult;

    private Date storeIssueTime;

    private Integer feedResult;

    private Date feedTime;

    private Integer changeResult;

    private Date changeTime;

    private Integer checkResult;

    private Date checkTime;

    private Integer checkAllResult;

    private Date checkAllTime;

    private Integer firstCheckAllResult;

    private Date firstCheckAllTime;

    private Date lastOperationTime;

    public String getScanLineseat() {
        return scanLineseat;
    }

    public void setScanLineseat(String scanLineseat) {
        this.scanLineseat = scanLineseat == null ? null : scanLineseat.trim();
    }

    public String getScanMaterialNo() {
        return scanMaterialNo;
    }

    public void setScanMaterialNo(String scanMaterialNo) {
        this.scanMaterialNo = scanMaterialNo == null ? null : scanMaterialNo.trim();
    }

    public Integer getLastOperationType() {
        return lastOperationType;
    }

    public void setLastOperationType(Integer lastOperationType) {
        this.lastOperationType = lastOperationType;
    }

    public Integer getStoreIssueResult() {
        return storeIssueResult;
    }

    public void setStoreIssueResult(Integer storeIssueResult) {
        this.storeIssueResult = storeIssueResult;
    }

    public Date getStoreIssueTime() {
        return storeIssueTime;
    }

    public void setStoreIssueTime(Date storeIssueTime) {
        this.storeIssueTime = storeIssueTime;
    }

    public Integer getFeedResult() {
        return feedResult;
    }

    public void setFeedResult(Integer feedResult) {
        this.feedResult = feedResult;
    }

    public Date getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(Date feedTime) {
        this.feedTime = feedTime;
    }

    public Integer getChangeResult() {
        return changeResult;
    }

    public void setChangeResult(Integer changeResult) {
        this.changeResult = changeResult;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCheckAllResult() {
        return checkAllResult;
    }

    public void setCheckAllResult(Integer checkAllResult) {
        this.checkAllResult = checkAllResult;
    }

    public Date getCheckAllTime() {
        return checkAllTime;
    }

    public void setCheckAllTime(Date checkAllTime) {
        this.checkAllTime = checkAllTime;
    }

    public Integer getFirstCheckAllResult() {
        return firstCheckAllResult;
    }

    public void setFirstCheckAllResult(Integer firstCheckAllResult) {
        this.firstCheckAllResult = firstCheckAllResult;
    }

    public Date getFirstCheckAllTime() {
        return firstCheckAllTime;
    }

    public void setFirstCheckAllTime(Date firstCheckAllTime) {
        this.firstCheckAllTime = firstCheckAllTime;
    }

    public Date getLastOperationTime() {
        return lastOperationTime;
    }

    public void setLastOperationTime(Date lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }
}