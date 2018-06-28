package com.jimi.smt.eps_server.entity.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实时显示报表实体类
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class DisplayReport {

	private List<DisplayReportItem> changes;
	
	private List<DisplayReportItem> feed;
	
	private List<DisplayReportItem> alls;
	
	private List<DisplayReportItem> somes;
	
	
	@SuppressWarnings("deprecation")
	public DisplayReport() {
		changes = new ArrayList<DisplayReportItem>(24);
		feed = new ArrayList<DisplayReportItem>(24);
		alls = new ArrayList<DisplayReportItem>(24);
		somes = new ArrayList<DisplayReportItem>(24);
		int hour = new Date().getHours();
		for(int i = 0 ; i <= 23 ; i++) {
			int t = hour - i;
			 if(t < 0){
				 t = 24 + t;
			 }
			String time = (t <= 9 ? "0" + t : t + "") + ":00";
			changes.add(new DisplayReportItem(time));
			feed.add(new DisplayReportItem(time));
			alls.add(new DisplayReportItem(time));
			somes.add(new DisplayReportItem(time));
		}
	}
	
	public List<DisplayReportItem> getChanges() {
		return changes;
	}

	public void setChanges(List<DisplayReportItem> changes) {
		this.changes = changes;
	}

	public List<DisplayReportItem> getFeed() {
		return feed;
	}

	public void setFeed(List<DisplayReportItem> feed) {
		this.feed = feed;
	}

	public List<DisplayReportItem> getAlls() {
		return alls;
	}

	public void setAlls(List<DisplayReportItem> alls) {
		this.alls = alls;
	}

	public List<DisplayReportItem> getSomes() {
		return somes;
	}

	public void setSomes(List<DisplayReportItem> somes) {
		this.somes = somes;
	}
	
	
	
}
