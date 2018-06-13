package com.jimi.smt.eps_server.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jimi.smt.eps_server.entity.Config;
import com.jimi.smt.eps_server.entity.Display;
import com.jimi.smt.eps_server.entity.ProgramItemVisit;
import com.jimi.smt.eps_server.entity.bo.LineInfo;
import com.jimi.smt.eps_server.service.ConfigService;
import com.jimi.smt.eps_server.service.ProgramService;

/**
 * 检料超时检查定时器类
 * <br>
 * <b>2018年4月26日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Component
public class TimeoutTimer {
	
	@Autowired
	private ProgramService programService;
	@Autowired
	private ConfigService configService;
	
	//键名
	private static final String CHECK_ALL_CYCLE_TIME = "check_all_cycle_time";
	private static final String CHECK_AFTER_CHANGE_TIME = "check_after_change_time";
	//产线数量
	private static final int LINE_SIZE = 8;
	//线锁
	private Object[] lineLocks; 
	//产线当前信息
	private Map<Integer, LineInfo> lineInfos;
	
	
	@PostConstruct
	public void init() {
		lineLocks = new Object[8];
		lineInfos = new HashMap<Integer, LineInfo>();
		for (int i = 0; i < LINE_SIZE; i++) {
			lineLocks[i] = new Object();
			lineInfos.put(i, new LineInfo());
		}
	}
	
	
	/**
	 * 每隔N秒检查是否有超时项目
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void check(){
		//设置超时时间
		setTimeoutTime();
		//设置当前工单和板面类型
		setWorkOrderAndBoardType();
		//遍历所有线别进行检查
		for (int i = 0; i < LINE_SIZE; i++) {
			//锁住对应线别
			synchronized (lineLocks[i]) {
				//判断是否有工单在被监控
				if(lineInfos.get(i).getWorkOrder() == null) {
					continue;
				}
				//查询programID
				String line = getLineString(i);
				LineInfo bo = lineInfos.get(i);
				List<ProgramItemVisit> programItemVisits = programService.getVisits(line, bo.getWorkOrder(), bo.getBoardType());
				if(! programItemVisits.isEmpty()) {
					//超时检测
					checkTimeout(programItemVisits, i);
				}
			}
		}
	}


	/**
	 * 获取线锁
	 */
	public Object getLock(String line) {
		return lineLocks[getLineNO(line)];
	}
	
	
	/**
	 * 根据字符串线号生成数字，例如308->7 ;301->0
	 */
	private static int getLineNO(String line) {
		return Integer.parseInt(line.substring(line.length() - 1, line.length())) - 1;
	}
	
	
	/**
	 * 根据数字线号生成字符串，例如7->308 ;0->301
	 */
	private static String getLineString(int line) {
		return "30" + (line + 1);
	}

	
	/**
	 * 超时检测
	 */
	private void checkTimeout(List<ProgramItemVisit> programItemVisits, int line) {
		for (ProgramItemVisit programItemVisit : programItemVisits) {
			//全检超时检测
			long temp = programItemVisit.getCheckAllTime().getTime() + lineInfos.get(line).getCheckAllTimeout() * 60 * 1000;
			boolean isCheckAllTimeout = new Date().getTime() > temp;
			boolean hasFirstCheckAll = programItemVisit.getFirstCheckAllResult() == 1;
			boolean isNotInCheckAllTimeoutState = programItemVisit.getCheckAllResult() != 3;
			if(isCheckAllTimeout  && hasFirstCheckAll && isNotInCheckAllTimeoutState) {
				for (ProgramItemVisit programItemVisit2 : programItemVisits) {
					programItemVisit2.setCheckAllResult(3);
					programItemVisit2.setLastOperationType(3);
					programItemVisit2.setLastOperationTime(new Date());
					programService.updateVisit(programItemVisit2);
				}
			}
			//核料超时检测
			long temp2 = programItemVisit.getChangeTime().getTime() + lineInfos.get(line).getCheckTimeout() * 60 * 1000;
			boolean isCheckTimeout = new Date().getTime() > temp2;
			boolean notYetCheck = programItemVisit.getCheckTime().getTime() < programItemVisit.getChangeTime().getTime();
			boolean hasChangeButNeedCheck = programItemVisit.getChangeResult() == 4;
			boolean isNotInCheckTimeoutState = programItemVisit.getCheckResult() != 3;
			if(isCheckTimeout && notYetCheck && hasChangeButNeedCheck && isNotInCheckTimeoutState) {
				 programItemVisit.setCheckResult(3);
				 programItemVisit.setLastOperationType(2);
				 programItemVisit.setLastOperationTime(new Date());
				 programService.updateVisit(programItemVisit);
			}
		}
	}


	/**
	 * 根据Config表设置超时时间
	 */
	private void setTimeoutTime() {
		List<Config> configs = configService.list();
		for (Config config : configs) {
			int lineNo = getLineNO(config.getLine());
			switch (config.getName()) {
			case CHECK_ALL_CYCLE_TIME:
				lineInfos.get(lineNo).setCheckAllTimeout(Integer.parseInt(config.getValue()));
				break;
			case CHECK_AFTER_CHANGE_TIME:
				lineInfos.get(lineNo).setCheckTimeout(Integer.parseInt(config.getValue()));
				break;
			default:
				break;
			}
		}
	}


	/**
	 * 根绝Display表设置工单号和板面类型
	 */
	private void setWorkOrderAndBoardType() {
		List<Display> displays = programService.listDisplays();
		for (Display display : displays) {
			int lineNo = getLineNO(display.getLine());
			lineInfos.get(lineNo).setWorkOrder(display.getWorkOrder());
			lineInfos.get(lineNo).setBoardType(display.getBoardType());
		}
	}
	
	
}
