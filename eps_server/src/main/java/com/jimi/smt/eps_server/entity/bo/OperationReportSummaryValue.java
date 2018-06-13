package com.jimi.smt.eps_server.entity.bo;

/**
 * 操作报表概要键
 * <br>
 * <b>2018年5月4日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class OperationReportSummaryValue {

	protected int passCount;
	
	protected int failCount;

	
	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	
}
