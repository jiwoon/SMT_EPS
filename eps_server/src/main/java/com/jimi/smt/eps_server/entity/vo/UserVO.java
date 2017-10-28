package com.jimi.smt.eps_server.entity.vo;

import com.jimi.smt.eps_server.entity.User;

/**
 * 用户表示层类
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class UserVO extends User {
	
	private String typeName;
	
	private String createTimeString;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCreateTimeString() {
		return createTimeString;
	}

	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
	}

	
	
	
}
