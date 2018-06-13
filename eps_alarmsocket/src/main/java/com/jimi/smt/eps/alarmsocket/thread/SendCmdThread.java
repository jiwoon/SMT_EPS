package com.jimi.smt.eps.alarmsocket.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.alarmsocket.constant.ClientDevice;
import com.jimi.smt.eps.alarmsocket.constant.ControlledDevice;
import com.jimi.smt.eps.alarmsocket.socket.ClientSocket;

/**
 * 命令发送子线程
 * <br>
 * <b>2018年3月22日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class SendCmdThread extends Thread{

	private static Logger logger = LogManager.getRootLogger();
	
	private ClientSocket socket;
	
	private boolean isAlarm;
	
	private ClientDevice clientDevice;
	
	private ControlledDevice controlledDevice;
	
	
	public SendCmdThread(ClientDevice clientDevice, ClientSocket socket, boolean isAlarm, ControlledDevice controlledDevice) {
		this.socket = socket;
		this.isAlarm = isAlarm;
		this.clientDevice = clientDevice;
		this.controlledDevice = controlledDevice;
	}
	
	
	@Override
	public void run() {
		try {
			//发送命令
			if(controlledDevice == ControlledDevice.ALARM) {
				socket.sendCmdToAlarm(clientDevice, isAlarm);
			}else if(controlledDevice == ControlledDevice.CONVEYOR) {
				socket.sendCmdToConvery(clientDevice, isAlarm);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			try {
				socket.reconnect();
				run();
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		}
	}
	
}
