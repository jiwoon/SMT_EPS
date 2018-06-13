package com.jimi.smt.eps.alarmsocket.bo;

/**
 * SMT错误实体
 * <br>
 * <b>2018年3月24日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class SMTError {

	/**
	 * 例如1代表301，2代表302，以此类推...
	 */
	private int line;
	
	/**
	 * 1：上料
	 * 2：换料
	 * 3：核料
	 * 4：全检
	 * 5：首检
	 */
	private int operation;
	
	/**
	 * 例如01-19, 08-03
	 */
	private String seat;
	
	/**
	 * 例如03.01.0001
	 */
	private String materialNo;

	public SMTError(int line, int operation, String seat, String materialNo) {
		this.line = line;
		this.operation = operation;
		this.seat = seat;
		this.materialNo = materialNo;
	}
	
	
	@Override
	public int hashCode() {
		return (line + seat + materialNo +operation).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return hashCode() == obj.hashCode();
	}
	

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}


	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	
}
