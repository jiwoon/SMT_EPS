package com.jimi.smt.eps_server.entity.bo;

/**
 * 工单和板面类型业务层类
 * <br>
 * <b>2018年4月26日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class LineInfo {

	/**
	 * 工单号
	 */
	private String workOrder;
	
	/**
	 * 0：默认 
	 * 1：AB面
	 * 2：A面
	 * 3：B面
	 */
	private Integer boardType;
	
	/**
	 * 核料超时时间，单位分钟
	 */
	private Integer checkTimeout;
	
	/**
	 * 全检超时时间，单位分钟
	 */
	private Integer checkAllTimeout;

	
	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public Integer getBoardType() {
		return boardType;
	}

	public void setBoardType(Integer boardType) {
		this.boardType = boardType;
	}

	public Integer getCheckTimeout() {
		return checkTimeout;
	}

	public void setCheckTimeout(Integer checkTimeout) {
		this.checkTimeout = checkTimeout;
	}

	public Integer getCheckAllTimeout() {
		return checkAllTimeout;
	}

	public void setCheckAllTimeout(Integer checkAllTimeout) {
		this.checkAllTimeout = checkAllTimeout;
	}
	
}
