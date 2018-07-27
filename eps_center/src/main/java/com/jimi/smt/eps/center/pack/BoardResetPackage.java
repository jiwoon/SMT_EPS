package com.jimi.smt.eps.center.pack;

import com.jimi.smt.eps.center.constant.BoardResetReson;
import com.jimi.smt.eps.center.constant.ClientDevice;
import com.jimi.smt.eps.center.constant.Line;

import cc.darhao.jiminal.annotation.Parse;
import cc.darhao.jiminal.annotation.Protocol;
import cc.darhao.jiminal.core.BasePackage;

@Protocol(0x52)
public class BoardResetPackage extends BasePackage {
	
	@Parse({0,1})
	private ClientDevice ClientDevice;
	@Parse({1,1})
	private Line Line;
	@Parse({2,1})
	private BoardResetReson BoardResetReson;
	
	public ClientDevice getClientDevice() {
		return ClientDevice;
	}
	public void setClientDevice(ClientDevice ClientDevice) {
		this.ClientDevice = ClientDevice;
	}
	public Line getLine() {
		return Line;
	}
	public void setLine(Line Line) {
		this.Line = Line;
	}
	public BoardResetReson getBoardResetReson() {
		return BoardResetReson;
	}
	public void setBoardResetReson(BoardResetReson BoardResetReson) {
		this.BoardResetReson = BoardResetReson;
	}


}
