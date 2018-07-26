package com.jimi.smt.eps_server.entity;

import java.text.DecimalFormat;

public class StatusDetail {
	
	private String line;
	
	private int allsSuc;
	
	private int allsFail;
	
	private String allsSucRate;
	
	private String allsFailRate;
	
	private int feedSuc;
	
	private int feedFail;
	
	private String feedSucRate;
	
	private String feedFailRate;
	
	private int changeSuc;
	
	private int changeFail;
	
	private String changeSucRate;
	
	private String changeFailRate;
	
	private int someSuc;
	
	private int someFail;
		
	private String someSucRate;
	
	private String someFailRate;

	public void fill() {
		DecimalFormat df = new DecimalFormat("0.00");
		float frate = (allsSuc + allsFail)!= 0 ? (float)(((1.0 * allsSuc) / (allsSuc + allsFail)) * 100) : 100;
		String rate = String.valueOf(df.format(frate));
		this.setAllsSucRate(rate);
		this.setAllsFailRate(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
		
		frate = (feedSuc + feedFail) != 0 ? (float) (((1.0 * feedSuc) / (feedSuc + feedFail)) * 100) : 100;
		rate = String.valueOf(df.format(frate));
		this.setFeedSucRate(rate);
		this.setFeedFailRate(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
		
		frate = (changeSuc + changeFail) != 0 ? (float) (((1.0 * changeSuc) / (changeSuc + changeFail)) * 100) : 100;
		rate = String.valueOf(df.format(frate));
		this.setChangeSucRate(rate);
		this.setChangeFailRate(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
		
		frate = (someSuc + someFail) != 0 ? (float) (((1.0 * someSuc) / (someSuc + someFail)) * 100) : 100;
		rate = String.valueOf(df.format(frate));
		this.setSomeSucRate(rate);
		this.setSomeFailRate(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
		
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getAllsSuc() {
		return allsSuc;
	}

	public void setAllsSuc(int allsSuc) {
		this.allsSuc = allsSuc;
	}

	public int getAllsFail() {
		return allsFail;
	}

	public void setAllsFail(int allsFail) {
		this.allsFail = allsFail;
	}

	public int getFeedSuc() {
		return feedSuc;
	}

	public void setFeedSuc(int feedSuc) {
		this.feedSuc = feedSuc;
	}

	public int getFeedFail() {
		return feedFail;
	}

	public void setFeedFail(int feedFail) {
		this.feedFail = feedFail;
	}

	public int getChangeSuc() {
		return changeSuc;
	}

	public void setChangeSuc(int changeSuc) {
		this.changeSuc = changeSuc;
	}

	public int getChangeFail() {
		return changeFail;
	}

	public void setChangeFail(int changeFail) {
		this.changeFail = changeFail;
	}

	public int getSomeSuc() {
		return someSuc;
	}

	public void setSomeSuc(int someSuc) {
		this.someSuc = someSuc;
	}

	public int getSomeFail() {
		return someFail;
	}

	public void setSomeFail(int someFail) {
		this.someFail = someFail;
	}
	
	public String getAllsSucRate() {
		return allsSucRate;
	}

	public void setAllsSucRate(String allsSucRate) {
		this.allsSucRate = allsSucRate;
	}

	public String getFeedSucRate() {
		return feedSucRate;
	}

	public void setFeedSucRate(String feedSucRate) {
		this.feedSucRate = feedSucRate;
	}

	public String getChangeSucRate() {
		return changeSucRate;
	}

	public void setChangeSucRate(String changeSucRate) {
		this.changeSucRate = changeSucRate;
	}

	public String getSomeSucRate() {
		return someSucRate;
	}

	public void setSomeSucRate(String someSucRate) {
		this.someSucRate = someSucRate;
	}

	public String getAllsFailRate() {
		return allsFailRate;
	}

	public void setAllsFailRate(String allsFailRate) {
		this.allsFailRate = allsFailRate;
	}

	public String getFeedFailRate() {
		return feedFailRate;
	}

	public void setFeedFailRate(String feedFailRate) {
		this.feedFailRate = feedFailRate;
	}

	public String getChangeFailRate() {
		return changeFailRate;
	}

	public void setChangeFailRate(String changeFailRate) {
		this.changeFailRate = changeFailRate;
	}

	public String getSomeFailRate() {
		return someFailRate;
	}

	public void setSomeFailRate(String someFailRate) {
		this.someFailRate = someFailRate;
	}

	
	
}