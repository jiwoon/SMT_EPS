package com.jimi.smt.eps.serversocket.pack;

import cc.darhao.jiminal.annotation.Parse;
import cc.darhao.jiminal.annotation.Protocol;
import com.jimi.smt.eps.serversocket.constant.ClientDevice;
import com.jimi.smt.eps.serversocket.constant.ControlledDevice;
import com.jimi.smt.eps.serversocket.constant.Line;
import com.jimi.smt.eps.serversocket.constant.Operation;
import cc.darhao.jiminal.core.BasePackage;

@Protocol(0x43)
public class ControlPackage extends BasePackage {
	
	@Parse({1,1})
	private Line Line;
	@Parse({3,1})
	private Operation Operation;
	@Parse({0,1})
	private ClientDevice ClientDevice;
	@Parse({2,1})
	private ControlledDevice ControlledDevice;

	public Line getLine() {
		return Line;
	}

	public void setLine(Line Line) {
		this.Line = Line;
	}

	public Operation getOperation() {
		return Operation;
	}

	public void setOperation(Operation Operation) {
		this.Operation = Operation;
	}

	public ClientDevice getClientDevice() {
		return ClientDevice;
	}

	public void setClientDevice(ClientDevice ClientDevice) {
		this.ClientDevice = ClientDevice;
	}

	public ControlledDevice getControlledDevice() {
		return ControlledDevice;
	}

	public void setControlledDevice(ControlledDevice ControlledDevice) {
		this.ControlledDevice = ControlledDevice;
	}

}
