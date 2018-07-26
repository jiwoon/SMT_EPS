package com.jimi.smt.eps_server.entity.vo;

import java.text.DecimalFormat;
import java.util.List;

import com.jimi.smt.eps_server.entity.StatusDetail;

public class StatusDetailsOfDayVO {
	
private String line;
	
	private int allsSucOfDay;
	
	private int allsFailOfDay;
	
	private String allsSucRateOfDay;
	
	private String allsFailRateOfDay;
	
	private int feedSucOfDay;
	
	private int feedFailOfDay;
	
	private String feedSucRateOfDay;
	
	private String feedFailRateOfDay;
	
	private int changeSucOfDay;
	
	private int changeFailOfDay;
	
	private String changeSucRateOfDay;
	
	private String changeFailRateOfDay;
	
	private int someSucOfDay;
	
	private int someFailOfDay;
		
	private String someSucRateOfDay;
	
	private String someFailRateOfDay;
	
	public void fill(List<StatusDetail> statusDetails) {
		if (statusDetails != null) {
			for (StatusDetail statusDetail : statusDetails) {
				this.allsSucOfDay += statusDetail.getAllsSuc();
				this.allsFailOfDay += statusDetail.getAllsFail();
				this.feedSucOfDay += statusDetail.getFeedSuc();
				this.feedFailOfDay += statusDetail.getFeedFail();
				this.changeSucOfDay += statusDetail.getChangeSuc();
				this.changeFailOfDay += statusDetail.getChangeFail();
				this.someSucOfDay += statusDetail.getSomeSuc();
				this.someFailOfDay += statusDetail.getSomeFail();
			}
			
			DecimalFormat df = new DecimalFormat("0.00");
			float frate = (allsSucOfDay + allsFailOfDay)!= 0 ? (float)(((1.0 * allsSucOfDay) / (allsSucOfDay + allsFailOfDay)) * 100) : 100;
			String rate = String.valueOf(df.format(frate));
			this.setallsSucRateOfDay(rate);
			this.setAllsFailRateOfDay(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
			
			frate = (feedSucOfDay + feedFailOfDay) != 0 ? (float) (((1.0 * feedSucOfDay) / (feedSucOfDay + feedFailOfDay)) * 100) : 100;
			rate = String.valueOf(df.format(frate));
			this.setFeedSucRateOfDay(rate);
			this.setFeedFailRateOfDay(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
			
			frate = (changeSucOfDay + changeFailOfDay) != 0 ? (float) (((1.0 * changeSucOfDay) / (changeSucOfDay + changeFailOfDay)) * 100) : 100;
			rate = String.valueOf(df.format(frate));
			this.setChangeSucRateOfDay(rate);
			this.setChangeFailRateOfDay(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
			
			frate = (someSucOfDay + someFailOfDay) != 0 ? (float) (((1.0 * someSucOfDay) / (someSucOfDay + someFailOfDay)) * 100) : 100;
			rate = String.valueOf(df.format(frate));
			this.setSomeSucRateOfDay(rate);
			this.setSomeFailRateOfDay(String.valueOf(df.format((100 - Float.parseFloat(rate)))));
			
		}
		
	}
	

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getallsSucOfDay() {
		return allsSucOfDay;
	}

	public void setallsSucOfDay(int allsSucOfDay) {
		this.allsSucOfDay = allsSucOfDay;
	}

	public int getAllsFailOfDay() {
		return allsFailOfDay;
	}

	public void setAllsFailOfDay(int allsFailOfDay) {
		this.allsFailOfDay = allsFailOfDay;
	}

	public String getallsSucRateOfDay() {
		return allsSucRateOfDay;
	}

	public void setallsSucRateOfDay(String allsSucRateOfDay) {
		this.allsSucRateOfDay = allsSucRateOfDay;
	}

	public String getAllsFailRateOfDay() {
		return allsFailRateOfDay;
	}

	public void setAllsFailRateOfDay(String allsFailRateOfDay) {
		this.allsFailRateOfDay = allsFailRateOfDay;
	}

	public int getFeedSucOfDay() {
		return feedSucOfDay;
	}

	public void setFeedSucOfDay(int feedSucOfDay) {
		this.feedSucOfDay = feedSucOfDay;
	}

	public int getFeedFailOfDay() {
		return feedFailOfDay;
	}

	public void setFeedFailOfDay(int feedFailOfDay) {
		this.feedFailOfDay = feedFailOfDay;
	}

	public String getFeedSucRateOfDay() {
		return feedSucRateOfDay;
	}

	public void setFeedSucRateOfDay(String feedSucRateOfDay) {
		this.feedSucRateOfDay = feedSucRateOfDay;
	}

	public String getFeedFailRateOfDay() {
		return feedFailRateOfDay;
	}

	public void setFeedFailRateOfDay(String feedFailRateOfDay) {
		this.feedFailRateOfDay = feedFailRateOfDay;
	}

	public int getChangeSucOfDay() {
		return changeSucOfDay;
	}

	public void setChangeSucOfDay(int changeSucOfDay) {
		this.changeSucOfDay = changeSucOfDay;
	}

	public int getChangeFailOfDay() {
		return changeFailOfDay;
	}

	public void setChangeFailOfDay(int changeFailOfDay) {
		this.changeFailOfDay = changeFailOfDay;
	}

	public String getChangeSucRateOfDay() {
		return changeSucRateOfDay;
	}

	public void setChangeSucRateOfDay(String changeSucRateOfDay) {
		this.changeSucRateOfDay = changeSucRateOfDay;
	}

	public String getChangeFailRateOfDay() {
		return changeFailRateOfDay;
	}

	public void setChangeFailRateOfDay(String changeFailRateOfDay) {
		this.changeFailRateOfDay = changeFailRateOfDay;
	}

	public int getSomeSucOfDay() {
		return someSucOfDay;
	}

	public void setSomeSucOfDay(int someSucOfDay) {
		this.someSucOfDay = someSucOfDay;
	}

	public int getSomeFailOfDay() {
		return someFailOfDay;
	}

	public void setSomeFailOfDay(int someFailOfDay) {
		this.someFailOfDay = someFailOfDay;
	}

	public String getSomeSucRateOfDay() {
		return someSucRateOfDay;
	}

	public void setSomeSucRateOfDay(String someSucRateOfDay) {
		this.someSucRateOfDay = someSucRateOfDay;
	}

	public String getSomeFailRateOfDay() {
		return someFailRateOfDay;
	}

	public void setSomeFailRateOfDay(String someFailRateOfDay) {
		this.someFailRateOfDay = someFailRateOfDay;
	}


}
