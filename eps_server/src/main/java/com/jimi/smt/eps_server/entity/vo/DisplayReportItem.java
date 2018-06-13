package com.jimi.smt.eps_server.entity.vo;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

/**
 * 实时显示报表子项目
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class DisplayReportItem {

	private String time;
	
	private Integer suc;
	
	private Integer fail;
	
	private Integer total;
	
	private Set<String> operators;

	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
//		return "{time:"+time+",suc:"+suc+",fail:"+fail+",total:"+total+"}";
	}
	
	public DisplayReportItem(String time) {
		this.time = time;
		this.fail = this.suc = this.total = 0;
		this.operators = new HashSet<String>();
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

	public Set<String> getOperators() {
		return operators;
	}

	public void setOperators(Set<String> operators) {
		this.operators = operators;
	}

}
