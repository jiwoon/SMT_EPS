package com.jimi.smt.eps.serversocket.pack;

import java.util.Date;

import cc.darhao.jiminal.annotation.Parse;
import cc.darhao.jiminal.annotation.Protocol;
import com.jimi.smt.eps.serversocket.constant.Line;
import cc.darhao.jiminal.core.BasePackage;

@Protocol(0x48)
public class HeartPackage extends BasePackage {

	@Parse({0,1})
	private Line Line;
	@Parse({1,4})
	private Date timestamp;
	@Parse({5,2})
	private boolean isAlarmEnabled;
	@Parse({5,1})
	private boolean isConveyorEnabled;
	@Parse({5,0})
	private boolean isInfraredEnabled;

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

	public boolean isAlarmEnabled() {
		return isAlarmEnabled;
	}

	public void setAlarmEnabled(boolean isAlarmEnabled) {
		this.isAlarmEnabled = isAlarmEnabled;
	}

	public boolean isConveyorEnabled() {
		return isConveyorEnabled;
	}

	public void setConveyorEnabled(boolean isConveyorEnabled) {
		this.isConveyorEnabled = isConveyorEnabled;
	}

	public boolean isInfraredEnabled() {
		return isInfraredEnabled;
	}

	public void setInfraredEnabled(boolean isInfraredEnabled) {
		this.isInfraredEnabled = isInfraredEnabled;
	}

}
