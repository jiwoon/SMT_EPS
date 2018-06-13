package com.jimi.smt.eps_server.entity.vo;

import com.jimi.smt.eps_server.entity.StockLog;

public class StockLogVO extends StockLog {

	private String operationTimeString;

	public String getOperationTimeString() {
		return operationTimeString;
	}

	public void setOperationTimeString(String operationTimeString) {
		this.operationTimeString = operationTimeString;
	}
	
}
