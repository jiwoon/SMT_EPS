package com.jimi.smt.eps_server.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jimi.smt.eps_server.entity.Operation;
import com.jimi.smt.eps_server.entity.OperationExample;
import com.jimi.smt.eps_server.entity.Page;
import com.jimi.smt.eps_server.entity.Program;
import com.jimi.smt.eps_server.entity.ProgramExample;
import com.jimi.smt.eps_server.entity.StockLogExample;
import com.jimi.smt.eps_server.entity.bo.OperationReportSummaryKey;
import com.jimi.smt.eps_server.entity.bo.OperationReportSummaryValue;
import com.jimi.smt.eps_server.entity.filler.OperationToClientReportFiller;
import com.jimi.smt.eps_server.entity.filler.OperationToOperationReportFiller;
import com.jimi.smt.eps_server.entity.filler.StockLogToStockLogVOFiller;
import com.jimi.smt.eps_server.entity.vo.ClientReport;
import com.jimi.smt.eps_server.entity.vo.DisplayReport;
import com.jimi.smt.eps_server.entity.vo.DisplayReportItem;
import com.jimi.smt.eps_server.entity.vo.OperationReport;
import com.jimi.smt.eps_server.entity.vo.OperationReportSummary;
import com.jimi.smt.eps_server.entity.vo.StockLogVO;
import com.jimi.smt.eps_server.mapper.OperationMapper;
import com.jimi.smt.eps_server.mapper.ProgramMapper;
import com.jimi.smt.eps_server.mapper.StockLogMapper;
import com.jimi.smt.eps_server.service.OperationService;
import com.jimi.smt.eps_server.util.ExcelSpringHelper;

@Service
public class OperationServiceImpl implements OperationService {

	@Autowired
	private OperationMapper operationMapper;
	@Autowired
	private ProgramMapper programMapper;
	@Autowired
	private StockLogMapper stockLogMapper;
	@Autowired
	private OperationToClientReportFiller operationToClientReportFiller;
	@Autowired
	private OperationToOperationReportFiller operationToOperationReportFiller;
	@Autowired
	private StockLogToStockLogVOFiller stockLogToStockLogVOFiller;
	
	@Override
	public List<ClientReport> listClientReport(String client, String programNo, String line, String orderNo,
			String workOrderNo, String startTime, String endTime) throws ParseException {
		operationToClientReportFiller.init();
		
		List<ClientReport> clientReports = new ArrayList<ClientReport>();
		
		OperationExample operationExample = new OperationExample();
        OperationExample.Criteria operationCriteria = operationExample.createCriteria();
        
        //筛选PASS
        operationCriteria.andResultEqualTo("PASS");
        
        //筛选时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(startTime != null && !startTime.equals("")) {
        	operationCriteria.andTimeGreaterThanOrEqualTo(simpleDateFormat.parse(startTime));
        }
        if(endTime != null && !endTime.equals("")) {
        	operationCriteria.andTimeLessThanOrEqualTo(simpleDateFormat.parse(endTime));
        }

        //筛选工单号
		if(workOrderNo != null && !workOrderNo.equals("")) {
			operationCriteria.andWorkOrderEqualTo(workOrderNo);
		}
		//筛选线别
		if(line != null && !line.equals("")) {
			operationCriteria.andLineEqualTo(line);
		}
        
        //时间降序
        operationExample.setOrderByClause("time desc");
        
        List<Operation> operations = operationMapper.selectByExample(operationExample);
        
        
        
        ProgramExample programExample = new ProgramExample();
        ProgramExample.Criteria programCriteria = programExample.createCriteria();
        //筛选客户
		if(client != null && !client.equals("")) {
			programCriteria.andClientEqualTo(client);
		}
		//筛选程序表编号
		if(programNo != null && !programNo.equals("")) {
			programCriteria.andProgramNoEqualTo(programNo);
		}
		//筛选订单号
		if(orderNo != null && !orderNo.equals("")) {
			programCriteria.andWorkOrderEqualTo(orderNo);
		}
		
		List<Program> programs = programMapper.selectByExample(programExample);
		//匹配
		for (Operation operation : operations) {
			for (Program program : programs) {
				if(program.getId().equals(operation.getProgramId())) {
					//把操作日志转化为客户报告
					clientReports.add(operationToClientReportFiller.fill(operation));
					break;
				}
			}
		}
		
		return clientReports;
	}

	//测试分页查询客户报表
	@Override
	public List<ClientReport> listClientReportByPage(String client, String programNo, String line, String orderNo,
			String workOrderNo, String startTime, String endTime, Page page) throws ParseException {
		operationToClientReportFiller.init();
		
		List<ClientReport> clientReports = new ArrayList<ClientReport>();
		
		OperationExample operationExample = new OperationExample();
        OperationExample.Criteria operationCriteria = operationExample.createCriteria();
        
        //筛选PASS
        operationCriteria.andResultEqualTo("PASS");
        
        //筛选时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(startTime != null && !startTime.equals("")) {
        	operationCriteria.andTimeGreaterThanOrEqualTo(simpleDateFormat.parse(startTime));
        }
        if(endTime != null && !endTime.equals("")) {
        	operationCriteria.andTimeLessThanOrEqualTo(simpleDateFormat.parse(endTime));
        }

        //筛选工单号
		if(workOrderNo != null && !workOrderNo.equals("")) {
			operationCriteria.andWorkOrderEqualTo(workOrderNo);
		}
		//筛选线别
		if(line != null && !line.equals("")) {
			operationCriteria.andLineEqualTo(line);
		}
        
        //时间降序
        operationExample.setOrderByClause("time desc");
        
        //获取总条数
        page.setTotallyData(operationMapper.countByExample(operationExample));
        //设置取值位置和条数
        operationExample.setLimitStart(page.getFirstIndex());
        operationExample.setLimitSize(page.getPageSize());
        
        List<Operation> operations = operationMapper.selectByExample(operationExample);
        
        ProgramExample programExample = new ProgramExample();
        ProgramExample.Criteria programCriteria = programExample.createCriteria();
        //筛选客户
		if(client != null && !client.equals("")) {
			programCriteria.andClientEqualTo(client);
		}
		//筛选程序表编号
		if(programNo != null && !programNo.equals("")) {
			programCriteria.andProgramNoEqualTo(programNo);
		}
		//筛选订单号
		if(orderNo != null && !orderNo.equals("")) {
			programCriteria.andWorkOrderEqualTo(orderNo);
		}
		
		List<Program> programs = programMapper.selectByExample(programExample);
		//匹配
		for (Operation operation : operations) {
			for (Program program : programs) {
				if(program.getId().equals(operation.getProgramId())) {
					//把操作日志转化为客户报告
					clientReports.add(operationToClientReportFiller.fill(operation));
					break;
				}
			}
		}
		
		return clientReports;
	}
	
	@Override
	public ResponseEntity<byte[]> downloadClientReport(String client, String programNo, String line, String orderNo,
			String workOrderNo, String startTime, String endTime) throws ParseException, IOException {
		ExcelSpringHelper helper = ExcelSpringHelper.create();
		//获取数据
		List<ClientReport> clientReports = listClientReport(client, programNo, line, orderNo, workOrderNo, startTime, endTime);
		helper.fill(clientReports);
		return helper.getDownloadEntity("客户报表.xls", true);
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public DisplayReport listDisplayReport(String line) {
		DisplayReport displayReport = new DisplayReport();
		//日期筛选（过去24小时）
		OperationExample operationExample = new OperationExample();
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime yesterday = today.plusDays(-1);
		Date t = Date.from(today.atZone(ZoneId.systemDefault()).toInstant());
		Date y = Date.from(yesterday.atZone(ZoneId.systemDefault()).toInstant());
		operationExample.createCriteria()
			.andTimeGreaterThanOrEqualTo(y)
			.andTimeLessThanOrEqualTo(t)
			.andLineEqualTo(line);
		List<Operation> operations = operationMapper.selectByExample(operationExample);
		//遍历
		for (Operation operation : operations) {
			DisplayReportItem item = null;
			int hour = new Date().getHours() - operation.getTime().getHours();
			if(hour < 0){
				hour = 24 + hour;
			}
			switch (operation.getType()) {
			case 0:
				item = displayReport.getFeed().get(hour);
				break;
			case 1:
				item = displayReport.getChanges().get(hour);
				break;
			case 2:
				item = displayReport.getSomes().get(hour);
				break;
			case 3:
				item = displayReport.getAlls().get(hour);
				break;
			default:
				continue;
			}
			if(operation.getResult().equals("PASS")) {
				item.setSuc(item.getSuc()+1);
			}else {
				item.setFail(item.getFail()+1);
			}
			item.setTotal(item.getTotal()+1);
			//记录操作者
			item.getOperators().add(operation.getOperator());
		}
		
		return displayReport;
	}

	
	@Override
	public List<OperationReport> listOperationReport(String operator, String client, String line, String workOrderNo, String startTime,
			String endTime, Integer type) throws ParseException {
		operationToOperationReportFiller.init();
		
		List<OperationReport> operationReports = new ArrayList<OperationReport>();
		
		OperationExample operationExample = new OperationExample();
        OperationExample.Criteria operationCriteria = operationExample.createCriteria();
        
        //筛选时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(startTime != null && !startTime.equals("")) {
        	operationCriteria.andTimeGreaterThanOrEqualTo(simpleDateFormat.parse(startTime));
        }
        if(endTime != null && !endTime.equals("")) {
        	operationCriteria.andTimeLessThanOrEqualTo(simpleDateFormat.parse(endTime));
        }
        
        //筛选工单
  		if(workOrderNo != null && !workOrderNo.equals("")) {
  			operationCriteria.andWorkOrderEqualTo(workOrderNo);
  		}
  		//筛选线别
  		if(line != null && !line.equals("")) {
  			operationCriteria.andLineEqualTo(line);
  		}
        
  		//筛选操作员
  		if(operator != null && !operator.equals("")) {
  			operationCriteria.andOperatorEqualTo(operator);
  		}
  		
  		//过滤类型
        operationCriteria.andTypeEqualTo(type);
  		
        //时间降序
        operationExample.setOrderByClause("time desc");
        
        List<Operation> operations = operationMapper.selectByExample(operationExample);
        
        ProgramExample programExample = new ProgramExample();
        ProgramExample.Criteria programCriteria = programExample.createCriteria();
        //筛选客户
		if(client != null && !client.equals("")) {
			programCriteria.andClientEqualTo(client);
		}
		
		List<Program> programs = programMapper.selectByExample(programExample);
		//匹配
		for (Operation operation : operations) {
			for (Program program : programs) {
				if(program.getId().equals(operation.getProgramId())) {
					//把操作日志转化为操作报告
					operationReports.add(operationToOperationReportFiller.fill(operation));
					break;
				}
			}
		}
		
		return operationReports;
	}
	
	
	@Override
	public ResponseEntity<byte[]> downloadOperationReport(String operator, String client, String line, String workOrderNo,
			String startTime, String endTime, Integer type) throws ParseException, IOException {
		List<OperationReport> operationReports = listOperationReport(operator, client, line, workOrderNo, startTime, endTime, type);
		ExcelSpringHelper helper = ExcelSpringHelper.create();
		//解析操作类型
		String title = null;
		switch (type) {
		case 0:
			title ="SMT上料报表";
			break;
		case 1:
			title ="SMT换料报表";
			break;
		case 2:
			title ="SMT抽检报表";
			break;
		case 3:
			title ="SMT全检报表";
			break;
		case 4:
			title ="SMT仓库发料报表";
			break;
		default:
			break;
		}
		helper.fill(operationReports, title);
		return helper.getDownloadEntity(title + ".xls", true);
	}

	
	@Override
	public List<OperationReportSummary> listOperationReportSummary(String line, String workOrderNo, String startTime,
			String endTime, Integer type) throws ParseException {
		//筛选数据
		List<OperationReport> operationReports = listOperationReport(null, null, line, workOrderNo, startTime, endTime, type);
		//创建分组Map，按OperationReportSummaryKey分组，值为result的与运算
		Map<OperationReportSummaryKey, OperationReportSummaryValue> map = new HashMap<OperationReportSummaryKey, OperationReportSummaryValue>();
		for (OperationReport operationReport : operationReports) {
			//创建Key
			OperationReportSummaryKey key = new OperationReportSummaryKey();
			key.setLine(operationReport.getLine());
			key.setWorkOrderNo(operationReport.getWorkOrderNo());
			key.setOperator(operationReport.getOperator());
			//获取该key之前的value
			OperationReportSummaryValue value =  map.get(key);
			if(value == null) {
				value = new OperationReportSummaryValue();
			}
			boolean result = operationReport.getResult().equals("PASS") ? true : false;
			if(result == true) {
				value.setPassCount(value.getPassCount() + 1);
			}else {
				value.setFailCount(value.getFailCount() + 1);
			}
			map.put(key, value);
		}
		//转化map为VOs
		List<OperationReportSummary> operationReportSummaries = new ArrayList<OperationReportSummary>();
		for (Entry<OperationReportSummaryKey, OperationReportSummaryValue> entry : map.entrySet()) {
			OperationReportSummary summary = new OperationReportSummary();
			summary.setLine(entry.getKey().getLine());
			summary.setWorkOrderNo(entry.getKey().getWorkOrderNo());
			summary.setOperator(entry.getKey().getOperator());
			summary.setPassCount(entry.getValue().getPassCount());
			summary.setFailCount(entry.getValue().getFailCount());
			operationReportSummaries.add(summary);
		}
		return operationReportSummaries;
	}


	@Override
	public List<StockLogVO> listStockLogs(String operator, String materialNo, String custom, String position,
			String startTime, String endTime) throws ParseException {
		
		StockLogExample stockLogExample = new StockLogExample();
		StockLogExample.Criteria stockLogCriteria = stockLogExample.createCriteria();
		
        //筛选时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(startTime != null && !startTime.equals("")) {
        	stockLogCriteria.andOperationTimeGreaterThanOrEqualTo(simpleDateFormat.parse(startTime));
        }
        if(endTime != null && !endTime.equals("")) {
        	stockLogCriteria.andOperationTimeLessThanOrEqualTo(simpleDateFormat.parse(endTime));
        }
        
  		//筛选操作员
  		if(operator != null && !operator.equals("")) {
  			stockLogCriteria.andOperatorEqualTo(operator);
  		}
  		
  		//筛选仓号
  		if(position != null && !position.equals("")) {
  			stockLogCriteria.andPositionEqualTo(position);
  		}
  		
  		//筛选供应商
  		if(custom != null && !custom.equals("")) {
  			stockLogCriteria.andCustomEqualTo(custom);
  		}
  		
  		//筛选料号
  		if(materialNo != null && !materialNo.equals("")) {
  			stockLogCriteria.andMaterialNoEqualTo(materialNo);
  		}
  		
        //时间降序
  		stockLogExample.setOrderByClause("operation_time desc");
  		
  		return stockLogToStockLogVOFiller.fill(stockLogMapper.selectByExample(stockLogExample));
	}


}
