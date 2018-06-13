package com.jimi.smt.eps_server.entity.bo;

import com.jimi.smt.eps_server.entity.ProgramItem;

/**
 * 在线编辑工单的操作记录业务层对象
 * <br>
 * <b>2018年3月12日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class EditProgramItemBO extends ProgramItem {

	/**
	 * 原来的站位
	 */
	private String targetLineseat;
	
	/**
	 * 原来的料号
	 */
	private String targetMaterialNo;
	
	/**
	 *0表示增加；1表示修改；2表示删除 <br>
	 */
	private int operation;
	

	public String getTargetLineseat() {
		return targetLineseat;
	}

	public void setTargetLineseat(String targetLineseat) {
		this.targetLineseat = targetLineseat;
	}

	public String getTargetMaterialNo() {
		return targetMaterialNo;
	}

	public void setTargetMaterialNo(String targetMaterialNo) {
		this.targetMaterialNo = targetMaterialNo;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}
	
}
