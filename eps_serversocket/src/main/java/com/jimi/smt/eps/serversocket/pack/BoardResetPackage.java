package com.jimi.smt.eps.serversocket.pack;

import cc.darhao.jiminal.annotation.Parse;
import cc.darhao.jiminal.annotation.Protocol;
import com.jimi.smt.eps.serversocket.constant.BoardResetReson;
import com.jimi.smt.eps.serversocket.constant.ClientDevice;
import com.jimi.smt.eps.serversocket.constant.Line;
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
