package com.jimi.smt.eps.alarmsocket.bo;

import java.util.HashSet;
import java.util.Set;

/**
 * 存储某一产线所有错误的集合
 * <br>
 * <b>2018年3月24日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class LineErrorsSet {

	private int line;
	
	private Set<SMTError> errors;
	
	public LineErrorsSet(int line) {
		this.line = line;
		errors = new HashSet<SMTError>();
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public Set<SMTError> getErrors() {
		return errors;
	}

	public void setErrors(Set<SMTError> errors) {
		this.errors = errors;
	}
	
}
