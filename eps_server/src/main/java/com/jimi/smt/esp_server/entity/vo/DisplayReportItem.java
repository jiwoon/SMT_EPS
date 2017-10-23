package com.jimi.smt.esp_server.entity.vo;

/**
 * 实时显示报表子项目
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class DisplayReportItem {

	private String time;
	
	private Integer suc;
	
	private Integer fail;
	
	private Integer total;

	
	@Override
	public String toString() {
		return "{time:"+time+",suc:"+suc+",fail:"+fail+",total:"+total+"}";
	}
	
	public DisplayReportItem(String time) {
		this.time = time;
		this.fail = this.suc = this.total = 0;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getSuc() {
		return suc;
	}

	public void setSuc(Integer suc) {
		this.suc = suc;
	}

	public Integer getFail() {
		return fail;
	}

	public void setFail(Integer fail) {
		this.fail = fail;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
