package com.jimi.smt.eps_server.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jimi.smt.eps_server.entity.Operation;
import com.jimi.smt.eps_server.entity.OperationExample;
import com.jimi.smt.eps_server.entity.Program;
import com.jimi.smt.eps_server.entity.ProgramExample;
import com.jimi.smt.eps_server.entity.filler.OperationToClientReportFiller;
import com.jimi.smt.eps_server.entity.filler.OperationToOperationReportFiller;
import com.jimi.smt.eps_server.entity.vo.ClientReport;
import com.jimi.smt.eps_server.entity.vo.DisplayReport;
import com.jimi.smt.eps_server.entity.vo.DisplayReportItem;
import com.jimi.smt.eps_server.entity.vo.OperationReport;
import com.jimi.smt.eps_server.mapper.OperationMapper;
import com.jimi.smt.eps_server.mapper.ProgramMapper;
import com.jimi.smt.eps_server.service.OperationService;
import com.jimi.smt.eps_server.util.ExcelHelper;

@Service
public class OperationServiceImpl implements OperationService {

	@Autowired
	private OperationMapper operationMapper;
	@Autowired
	private ProgramMapper programMapper;
	@Autowired
	private OperationToClientReportFiller operationToClientReportFiller;
	@Autowired
	private OperationToOperationReportFiller operationToOperationReportFiller;
	
	@Override
	public List<ClientReport> listClientReport(String client, String programNo, String line, String orderNo,
			String workOrderNo, String startTime, String endTime) throws ParseException {
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

	@Override
	public ResponseEntity<byte[]> downloadClientReport(String client, String programNo, String line, String orderNo,
			String workOrderNo, String startTime, String endTime) throws ParseException, IOException {
		ExcelHelper helper = ExcelHelper.create();
		//获取数据
		List<ClientReport> clientReports = listClientReport(client, programNo, line, orderNo, workOrderNo, startTime, endTime);
		helper.fill(clientReports);
		return helper.getDownloadEntity("客户报表.xls", true);
	}

	
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
		}
		
		return displayReport;
	}

	@Override
	public List<OperationReport> listOperationReport(String client, String line, String workOrderNo, String startTime,
			String endTime, Integer type) throws ParseException {
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
					//把操作日志转化为客户报告
					operationReports.add(operationToOperationReportFiller.fill(operation));
					break;
				}
			}
		}
		
		return operationReports;
	}

	@Override
	public ResponseEntity<byte[]> downloadOperationReport(String client, String line, String workOrderNo,
			String startTime, String endTime, Integer type) throws ParseException, IOException {
		List<OperationReport> operationReports = listOperationReport(client, line, workOrderNo, startTime, endTime, type);
		ExcelHelper helper = ExcelHelper.create();
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


}
