package com.jimi.smt.eps_server.entity.vo;

import com.jimi.smt.eps_server.util.ExcelHelper.Excel;

/**
 * 操作报表
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class OperationReport{

	@Excel(col=0, head="线号")
	private String line;
	@Excel(col=1, head="工单号")
	private String workOrderNo;
	@Excel(col=2, head="槽位")
	private String lineseat;
	@Excel(col=3, head="物料编号")
	private String materialNo;
	@Excel(col=4, head="物料描述")
	private String materialDescription;
	@Excel(col=5, head="物料规格")
	private String materialSpecitification;
	@Excel(col=6, head="操作者")
	private String operator;
	@Excel(col=7, head="操作结果")
	private String result;
	@Excel(col=8, head="操作时间")
	private String time;
	
	
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getLineseat() {
		return lineseat;
	}
	public void setLineseat(String lineseat) {
		this.lineseat = lineseat;
	}
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	
}
