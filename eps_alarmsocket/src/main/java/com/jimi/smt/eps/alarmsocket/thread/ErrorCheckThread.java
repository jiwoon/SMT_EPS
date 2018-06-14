package com.jimi.smt.eps.alarmsocket.thread;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.alarmsocket.bo.CenterControllerErrorCounter;
import com.jimi.smt.eps.alarmsocket.constant.ClientDevice;
import com.jimi.smt.eps.alarmsocket.constant.ControlledDevice;
import com.jimi.smt.eps.alarmsocket.entity.Config;
import com.jimi.smt.eps.alarmsocket.entity.Program;
import com.jimi.smt.eps.alarmsocket.entity.ProgramItemVisit;
import com.jimi.smt.eps.alarmsocket.mapper.ConfigMapper;
import com.jimi.smt.eps.alarmsocket.mapper.ProgramItemVisitMapper;
import com.jimi.smt.eps.alarmsocket.mapper.ProgramMapper;
import com.jimi.smt.eps.alarmsocket.socket.ClientSocket;
import com.jimi.smt.eps.alarmsocket.util.MybatisHelper;
import com.jimi.smt.eps.alarmsocket.util.MybatisHelper.MybatisSession;

/**
 * 错误检查任务，用于周期性检测各线别的错误列表中是否有错误以及是否有错误可以清除<br>

 * <br>
 * <b>2018年3月24日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ErrorCheckThread extends Thread {

	private static Logger logger = LogManager.getRootLogger();
	
	/**
	 * Mybatis的配置文件路径
	 */
	private static final String MYBATIS_CONFIG_PATH = "mybatis/mybatis-config.xml";
	
	/**
	 * Config配置项
	 */
	private static final String OPERATOR_ERROR_ALARM = "operator_error_alarm";
	
	/**
	 * Config配置项
	 */
	private static final String IPQC_ERROR_ALARM = "ipqc_error_alarm";
	
	/**
	 * “线别-报警设备错误统计”实体
	 */
	private Map<Integer,CenterControllerErrorCounter> lineErrorsCounters;
	
	/**
	 * 所有线别的报警模块列表
	 */
	private Map<Integer,ClientSocket> clientSockets;
	
	/**
	 * 产线数量
	 */
	private static final int lineSize = 8;
	
	/**
	 * 检测周期
	 */
	private static final long cycle = 3000;
	
	
	public ErrorCheckThread() throws Exception {
		//初始化sockets
		init();
	}


	/**
	 * 初始化
	 */
	private void init() {
		clientSockets = new HashMap<Integer,ClientSocket>();
		for(int i = 0; i < lineSize; i++) {
			try {
				clientSockets.put(i, new ClientSocket(i));
			} catch (Exception e) {
				logger.error("搜索产线 "+ i +" : "+e.getMessage());
			}
		}
	}


	/**
	 * 初始化“线别-报警设备错误统计”实体
	 */
	private void initCounters() throws IOException {
		lineErrorsCounters = new HashMap<Integer, CenterControllerErrorCounter>();;
		for(int i = 0; i < lineSize; i++) {
			lineErrorsCounters.put(i, new CenterControllerErrorCounter());
		}
		//设置报警模式
		setAlarmMode();
	}


	/**
	 * 根据Config表设置报警方式
	 * @throws IOException 
	 */
	private void setAlarmMode() throws IOException {
		MybatisSession<ConfigMapper> configSession = null;
		configSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, ConfigMapper.class, "smt");
		List<Config> configs = configSession.getMapper().selectByExample(null);
		for (Config config : configs) {
			int lineNo = getLineNO(config.getLine());
			switch (config.getName()) {
			case IPQC_ERROR_ALARM:
				lineErrorsCounters.get(lineNo).setIpqcErrorAlarm(Integer.parseInt(config.getValue()));
				break;
			case OPERATOR_ERROR_ALARM:
				lineErrorsCounters.get(lineNo).setOperatorErrorAlarm(Integer.parseInt(config.getValue()));
				break;
			default:
				break;
			}
		}
	}


	/**
	 * 根据字符串线号生成数字，例如308->7 ;301->0
	 */
	private int getLineNO(String line) {
		return Integer.parseInt(line.substring(line.length() - 1, line.length())) - 1;
	}


	/**
	 * 扫描错误
	 */
	private void scanErrors() {
		MybatisSession<ProgramMapper> programSession = null;
		MybatisSession<ProgramItemVisitMapper> visitSession = null;
		try {
			programSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, ProgramMapper.class, "smt");
			visitSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, ProgramItemVisitMapper.class, "smt");
			long s = System.currentTimeMillis();
			//查询所有State为1且被客户端选中的Program条目			
			/*ProgramExample programExample = new ProgramExample();
			programExample.createCriteria().andStateEqualTo(1);*/
			List<Program> programs = programSession.getMapper().selectByWorkOrderAndBoardType();
			//查询所有visit
			List<ProgramItemVisit> programItemVisits = visitSession.getMapper().selectByExample(null);
			System.out.println("查询耗时：" + (System.currentTimeMillis() - s) + "ms");
			//遍历Visit
			for (ProgramItemVisit programItemVisit : programItemVisits) {
				//匹配线别
				for (Program program : programs) {
					if(program.getId().equals(programItemVisit.getProgramId())) {
						try {
							//遍历字段
							int line = getLineNO(program.getLine());
							updateLineErrorCounter(line, 0, programItemVisit.getFeedResult());
							updateLineErrorCounter(line, 1, programItemVisit.getChangeResult());
							updateLineErrorCounter(line, 2, programItemVisit.getCheckResult());
							updateLineErrorCounter(line, 3, programItemVisit.getCheckAllResult());
							updateLineErrorCounter(line, 5, programItemVisit.getFirstCheckAllResult());
							break;
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally {
			programSession.getSession().close();
			visitSession.getSession().close();
		}
	}


	/**
	 * 更新错误计数
	 * @param line
	 * @param operaton
	 * 0：上料
	 * 1：换料
	 * 2：核料
	 * 3：全检
	 * 5：首检
	 * @param result
	 */
	private void updateLineErrorCounter(int line, int opertion, int result) {
		//根据配置文件计数
		if(result == 0 || result == 3) {
			CenterControllerErrorCounter counter = lineErrorsCounters.get(line);
			if(opertion > 1) {
				int ipqcMode = counter.getIpqcErrorAlarm();
				count(counter, ipqcMode);
			}else {
				int operatorMode = counter.getOperatorErrorAlarm();
				count(counter, operatorMode);
			}
		}
	}


	/**
	 * 计数
	 */
	private void count(CenterControllerErrorCounter counter, int mode) {
		switch (mode) {
		case 0:
			counter.setConveryErrorCount(counter.getConveryErrorCount() + 1);
			counter.setAlarmErrorCount(counter.getAlarmErrorCount() + 1);
			break;
		case 1:
			counter.setAlarmErrorCount(counter.getAlarmErrorCount() + 1);
			break;
		case 2:
			counter.setConveryErrorCount(counter.getConveryErrorCount() + 1);
			break;
		default:
			break;
		}
	}


	/**
	 * 根据统计结果发送指令
	 */
	private void sendCmdByCounters() {
		for(int i = 0; i < lineSize; i++) {
			if(clientSockets.get(i) == null) {
				continue;
			}
			CenterControllerErrorCounter counter = lineErrorsCounters.get(i);
			if(counter.getAlarmErrorCount() > 0) {
				sendCmd(ClientDevice.SERVER, clientSockets.get(i), true, ControlledDevice.ALARM);
			}else {
				sendCmd(ClientDevice.SERVER, clientSockets.get(i), false, ControlledDevice.ALARM);
			}
			if(counter.getConveryErrorCount() > 0) {
				sendCmd(ClientDevice.SERVER, clientSockets.get(i), true, ControlledDevice.CONVEYOR);
			}else {
				sendCmd(ClientDevice.SERVER, clientSockets.get(i), false, ControlledDevice.CONVEYOR);
			}
		}
	}


	/**
	 * 根据配置，发送报警/解除包
	 * @param clientDevice 发起者
	 * @param socket 发送指令的socket
	 * @param isAlarm 报警/解除报警
	 * @param controlledDevice 被控制设备：报警灯或接驳台
	 */
	private void sendCmd(ClientDevice clientDevice, ClientSocket socket, boolean isAlarm, ControlledDevice controlledDevice) {
		new SendCmdThread(clientDevice, socket, isAlarm, controlledDevice).start();
	}


	@Override
	public void run() {
		//提示已运行
		logger.info("alarmsocket is running now!");
		while(true) {
			//轮询间隔
			try {
				sleep(cycle);
				//初始化“线别-报警设备错误统计”实体
				initCounters();
				//遍历visits进行错误扫描
				scanErrors();
				//根据统计结果发送指令
				sendCmdByCounters();
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		}
	}

}
