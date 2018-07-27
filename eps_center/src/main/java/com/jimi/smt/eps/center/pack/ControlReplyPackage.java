package com.jimi.smt.eps.center.pack;

import com.jimi.smt.eps.center.constant.ClientDevice;
import com.jimi.smt.eps.center.constant.ControlResult;
import com.jimi.smt.eps.center.constant.ErrorCode;

import cc.darhao.jiminal.annotation.Parse;
import cc.darhao.jiminal.annotation.Protocol;
import cc.darhao.jiminal.core.BasePackage;

@Protocol(0x43)
public class ControlReplyPackage extends BasePackage {

	@Parse({ 0, 1 })
	private ClientDevice ClientDevice;
	@Parse({ 1, 1 })
	private ControlResult ControlResult;
	@Parse({ 2, 1 })
	private ErrorCode ErrorCode;

	public ClientDevice getClientDevice() {
		return ClientDevice;
	}

	public void setClientDevice(ClientDevice ClientDevice) {
		this.ClientDevice = ClientDevice;
	}

	public ErrorCode getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(ErrorCode ErrorCode) {
		this.ErrorCode = ErrorCode;
	}

	public ControlResult getControlResult() {
		return ControlResult;
	}

	public void setControlResult(ControlResult ControlResult) {
		this.ControlResult = ControlResult;
	}

}
