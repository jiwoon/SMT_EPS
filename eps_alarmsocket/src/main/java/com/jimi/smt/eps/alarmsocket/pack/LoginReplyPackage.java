package com.jimi.smt.eps.alarmsocket.pack;

import java.util.Date;

import com.jimi.smt.eps.alarmsocket.constant.Line;

import cc.darhao.jiminal.annotation.Parse;
import cc.darhao.jiminal.annotation.Protocol;
import cc.darhao.jiminal.core.BasePackage;

@Protocol(0x4C)
public class LoginReplyPackage extends BasePackage {
	
	@Parse({0,1})
	private Line Line;
	@Parse({1,4})
	private Date timestamp;

	public Line getLine() {
		return Line;
	}

	public void setLine(Line Line) {
		this.Line = Line;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
