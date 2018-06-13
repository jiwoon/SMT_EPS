package com.jimi.smt.eps.alarmsocket.socket;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.alarmsocket.constant.ClientDevice;
import com.jimi.smt.eps.alarmsocket.constant.ControlResult;
import com.jimi.smt.eps.alarmsocket.constant.ControlledDevice;
import com.jimi.smt.eps.alarmsocket.constant.Line;
import com.jimi.smt.eps.alarmsocket.constant.Operation;
import com.jimi.smt.eps.alarmsocket.entity.Log;
import com.jimi.smt.eps.alarmsocket.entity.Login;
import com.jimi.smt.eps.alarmsocket.entity.LoginExample;
import com.jimi.smt.eps.alarmsocket.entity.State;
import com.jimi.smt.eps.alarmsocket.mapper.LogMapper;
import com.jimi.smt.eps.alarmsocket.mapper.LoginMapper;
import com.jimi.smt.eps.alarmsocket.mapper.StateMapper;
import com.jimi.smt.eps.alarmsocket.pack.ControlPackage;
import com.jimi.smt.eps.alarmsocket.pack.ControlReplyPackage;
import com.jimi.smt.eps.alarmsocket.util.MybatisHelper;
import com.jimi.smt.eps.alarmsocket.util.MybatisHelper.MybatisSession;

import cc.darhao.dautils.api.BytesParser;
import cc.darhao.dautils.api.FieldUtil;
import cc.darhao.jiminal.core.BasePackage;
import cc.darhao.jiminal.core.PackageParser;
import cc.darhao.jiminal.core.SyncCommunicator;

/**
 * 包测试套接字
 * <br>
 * <b>2018年1月6日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ClientSocket {

	private static Logger logger = LogManager.getRootLogger();
	
	private SyncCommunicator communicator;
	
	private static final int TIME_OUT = 100000;
	
	private static final String PACKAGE_PATH = "com.jimi.smt.eps.alarmsocket.pack";
	
	private static final String MYBATIS_CONFIG_PATH = "mybatis/mybatis-config.xml";
	
	private int line;
	
	private String ip;
	
	private int port;
	
	/**
	 * 报警中
	 */
	private boolean alarming;
	
	/**
	 * 接驳台已暂停
	 */
	private boolean converyPaused;
	
	
	public ClientSocket(int line) throws Exception {
		this.line = line;
		MybatisSession<LoginMapper> loginSession = null;
		loginSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, LoginMapper.class);
		LoginExample example = new LoginExample();
		example.createCriteria().andLineEqualTo("30" + (this.line + 1));
		List<Login> logins = loginSession.getMapper().selectByExample(example);
		loginSession.getSession().close();
		if(!logins.isEmpty()) {
			//尝试连接中控
			ip = logins.get(0).getIp();
			port = logins.get(0).getPort();
			System.out.println("ip=" + ip +", port=" + port);
			communicator = new SyncCommunicator(ip, port, PACKAGE_PATH);
			communicator.setTimeout(TIME_OUT);
			communicator.connect();
			logger.info("搜索产线 "+ line +" : " + "已找到中控设备：" + ip);
			reset();
		}else {
			throw new Exception("没有找到对应线号的中控");
		}
	}


	/**
	 * 发送包到中控接驳台
	 * @throws IOException
	 */
	public synchronized void sendCmdToConvery(ClientDevice clientDevice, boolean isAlarm) throws Exception {
		if(converyPaused ^ isAlarm) {
			//开启or关闭接驳台
			ControlPackage controlPackage = new ControlPackage();
			controlPackage.setLine(Line.values()[line + 1]);
			controlPackage.setClientDevice(clientDevice);
			controlPackage.setControlledDevice(ControlledDevice.CONVEYOR);
			if(isAlarm) {
				controlPackage.setOperation(Operation.OFF);
			}else {
				controlPackage.setOperation(Operation.ON);
			}
			ControlReplyPackage replyPackage = (ControlReplyPackage) communicator.send(controlPackage);
			MybatisSession<LogMapper> logSession = null;
			logSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, LogMapper.class);
			//创建日志：收到的包
			Log pLog = createLogByPackage(controlPackage);
			logSession.getMapper().insert(pLog);
			//创建日志：回复的包
			Log rLog = createLogByPackage(replyPackage);
			logSession.getMapper().insert(rLog);
			logSession.commit();
			//判断是否操作成功
			if(! replyPackage.getControlResult().equals(ControlResult.SUCCEED)) {
				throw new Exception("控制接驳台失败");
			}
			converyPaused = isAlarm;
			//记录到state表
			MybatisSession<StateMapper> stateSession = null;
			stateSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, StateMapper.class);
			State state = stateSession.getMapper().selectByPrimaryKey(line);
			state.setConverypaused(converyPaused);
			stateSession.getMapper().updateByPrimaryKey(state);
			stateSession.commit();
		}
	}
	
	
	/**
	 * 发送包到中控报警器
	 * @throws IOException 
	 */
	public synchronized void sendCmdToAlarm(ClientDevice clientDevice, boolean isAlarm) throws Exception {
		if(alarming ^ isAlarm) {
			//打开or关闭报警器
			ControlPackage controlPackage = new ControlPackage();
			controlPackage.setLine(Line.values()[line + 1]);
			controlPackage.setClientDevice(clientDevice);
			controlPackage.setControlledDevice(ControlledDevice.ALARM);
			if(isAlarm) {
				controlPackage.setOperation(Operation.ON);
			}else {
				controlPackage.setOperation(Operation.OFF);
			}
			ControlReplyPackage replyPackage = (ControlReplyPackage) communicator.send(controlPackage);
			MybatisSession<LogMapper> logSession = null;
			logSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, LogMapper.class);
			//创建日志：收到的包
			Log pLog = createLogByPackage(controlPackage);
			logSession.getMapper().insert(pLog);
			//创建日志：回复的包
			Log rLog = createLogByPackage(replyPackage);
			logSession.getMapper().insert(rLog);
			logSession.commit();
			//判断是否操作成功
			if(! replyPackage.getControlResult().equals(ControlResult.SUCCEED)) {
				throw new Exception("控制报警器失败");
			}
			alarming = isAlarm;
			//记录到state表
			MybatisSession<StateMapper> stateSession = null;
			stateSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, StateMapper.class);
			State state = stateSession.getMapper().selectByPrimaryKey(line);
			state.setAlarming(alarming);
			stateSession.getMapper().updateByPrimaryKey(state);
			stateSession.commit();
		}
	}

	
	public void close() {
		communicator.close();
	}
	
	
	public void reconnect() throws Exception {
		close();
		communicator.connect();
		reset();
	}
	
	
	private void reset() throws Exception {
		//强制重置
		alarming = true;
		converyPaused = true;
		sendCmdToAlarm(ClientDevice.SERVER, false);
		sendCmdToConvery(ClientDevice.SERVER, false);
		logger.info("产线 "+ line +"报警器、接驳台已重置");
	}


	/**
	 * 根据包创建日志实体
	 * @param p
	 * @return
	 */
	private Log createLogByPackage(BasePackage p) {
		Log log = new Log();
		FieldUtil.copy(p, log);
		log.setTime(new Date());
		String data = BytesParser.parseBytesToHexString(PackageParser.serialize(p));
		log.setData(data);
		return log;
	}
}


