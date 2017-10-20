package com.jimi.smt.esp_server.entity.vo;

import com.jimi.smt.esp_server.entity.Operation;

/**
 * 客户报表
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
materialDescription
物料描述
materialSpecitification
物料规格
operationType
操作类型
orderNo
订单号
workOrderNo
工单号
 */
public class ClientReport extends Operation {

	private String line;
	
	private String materialDescription;
	
	private String materialSpecitification;
	
	private String operationType;
	
	private String orderNo;
	
	private String workOrderNo;

	public String getMaterialDescription() {
		return materialDescription;
	}

	public void setMaterialDescription(String materialDescription) {
		this.materialDescription = materialDescription;
	}

	public String getMaterialSpecitification() {
		return materialSpecitification;
	}

	public void setMaterialSpecitification(String materialSpecitification) {
		this.materialSpecitification = materialSpecitification;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	
}
