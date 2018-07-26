package com.jimi.smt.eps.display.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public final class ResultData {
	SimpleStringProperty lineseat = new SimpleStringProperty();
	SimpleIntegerProperty storeIssueResult = new SimpleIntegerProperty();
	SimpleIntegerProperty feedResult = new SimpleIntegerProperty();
	SimpleIntegerProperty changeResult = new SimpleIntegerProperty();
	SimpleIntegerProperty checkResult = new SimpleIntegerProperty();
	SimpleIntegerProperty checkAllResult = new SimpleIntegerProperty();
	SimpleIntegerProperty firstCheckAllResult = new SimpleIntegerProperty();
	
	public String getLineseat() {
		return lineseat.get();
	}
	public void setLineseat(String lineseat) {
		this.lineseat.set(lineseat);
	}
	public Integer getStoreIssueResult() {
		return storeIssueResult.get();
	}
	public void setStoreIssueResult(Integer storeIssueResult) {
		this.storeIssueResult.set(storeIssueResult);
	}
	public Integer getFeedResult() {
		return feedResult.get();
	}
	public void setFeedResult(Integer feedResult) {
		this.feedResult.set(feedResult);
	}
	public Integer getChangeResult() {
		return changeResult.get();
	}
	public void setChangeResult(Integer changeResult) {
		this.changeResult.set(changeResult);
	}
	public Integer getCheckResult() {
		return checkResult.get();
	}
	public void setCheckResult(Integer checkResult) {
		this.checkResult.set(checkResult);
	}
	public Integer getCheckAllResult() {
		return checkAllResult.get();
	}
	public void setCheckAllResult(Integer checkAllResult) {
		this.checkAllResult.set(checkAllResult);
	}
	public Integer getFirstCheckAllResult() {
		return firstCheckAllResult.get();
	}
	public void setFirstCheckAllResult(Integer firstCheckAllResult) {
		this.firstCheckAllResult.set(firstCheckAllResult);
	}
	
	
	
}
