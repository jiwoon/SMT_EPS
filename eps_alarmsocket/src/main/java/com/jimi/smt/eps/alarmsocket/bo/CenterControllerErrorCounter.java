package com.jimi.smt.eps.alarmsocket.bo;

/**
 * 中控错误计数体
 * <br>
 * 用于记录某一条产线报警灯、接驳台收到的错误指令数量以及报警方式，使用Map与线别映射
 * <br>
 * <b>2018年3月24日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class CenterControllerErrorCounter {

	/**
	 * IPQC操作（核料、全检、首检）出错时，系统将执行的操作。0为报警+停接驳台；1为仅报警；2为仅停接驳台；3为无动作
	 */
	private int ipqcErrorAlarm;
	
	/**
	 * 操作员操作（上料、换料）出错时，系统将执行的操作。0为报警+停接驳台；1为仅报警；2为仅停接驳台；3为无动作
	 */
	private int operatorErrorAlarm;
	
	/**
	 * 报警灯收到报警指令的数量
	 */
	private int alarmErrorCount;
	
	/**
	 * 接驳台收到停机指令的数量
	 */
	private int converyErrorCount;

	
	public int getAlarmErrorCount() {
		return alarmErrorCount;
	}

	public void setAlarmErrorCount(int alarmErrorCount) {
		this.alarmErrorCount = alarmErrorCount;
	}

	public int getConveryErrorCount() {
		return converyErrorCount;
	}

	public void setConveryErrorCount(int converyErrorCount) {
		this.converyErrorCount = converyErrorCount;
	}

	public int getIpqcErrorAlarm() {
		return ipqcErrorAlarm;
	}

	public void setIpqcErrorAlarm(int ipqcErrorAlarm) {
		this.ipqcErrorAlarm = ipqcErrorAlarm;
	}

	public int getOperatorErrorAlarm() {
		return operatorErrorAlarm;
	}

	public void setOperatorErrorAlarm(int operatorErrorAlarm) {
		this.operatorErrorAlarm = operatorErrorAlarm;
	}
	
	
}
