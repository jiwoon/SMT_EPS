package com.jimi.smt.esp_server.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jimi.smt.esp_server.entity.Operation;
import com.jimi.smt.esp_server.entity.OperationExample;
import com.jimi.smt.esp_server.entity.ProgramBackup;
import com.jimi.smt.esp_server.entity.ProgramBackupExample;
import com.jimi.smt.esp_server.entity.filler.OperationToClientReportFiller;
import com.jimi.smt.esp_server.entity.vo.ClientReport;
import com.jimi.smt.esp_server.entity.vo.DisplayReport;
import com.jimi.smt.esp_server.entity.vo.DisplayReportItem;
import com.jimi.smt.esp_server.mapper.OperationMapper;
import com.jimi.smt.esp_server.mapper.ProgramBackupMapper;
import com.jimi.smt.esp_server.service.OperationService;

@Service
public class OperationServiceImpl implements OperationService {

	@Autowired
	private OperationMapper operationMapper;
	@Autowired
	private ProgramBackupMapper programBackupMapper;
	@Autowired
	private OperationToClientReportFiller filler;
	
	@Override
	public List<ClientReport> listClientReport(String client, String programNo, String line, String orderNo,
			String workOrderNo, String startTime, String endTime) throws ParseException {
		List<ClientReport> clientReports = new ArrayList<ClientReport>();
		
		OperationExample operationExample = new OperationExample();
        OperationExample.Criteria operationCriteria = operationExample.createCriteria();
        //筛选PASS
        operationCriteria.andResultEqualTo("PASS");
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转化时间
        if(startTime != null) {
        	operationCriteria.andTimeGreaterThanOrEqualTo(simpleDateFormat.parse(startTime));
        }
        
        if(endTime != null) {
        	operationCriteria.andTimeLessThanOrEqualTo(simpleDateFormat.parse(endTime));
        }
        
        List<Operation> operations = operationMapper.selectByExample(operationExample);
        
        ProgramBackupExample programBackupExample = new ProgramBackupExample();
        ProgramBackupExample.Criteria programBackupCriteria = programBackupExample.createCriteria();
        //筛选客户
		if(client != null) {
			programBackupCriteria.andClientEqualTo(client);
		}
		//筛选程序表编号
		if(programNo != null) {
			programBackupCriteria.andProgramNoEqualTo(programNo);
		}
		//筛选线别
		if(line != null) {
			programBackupCriteria.andLineEqualTo(line);
		}
		
		List<ProgramBackup> programBackups = programBackupMapper.selectByExample(programBackupExample);
		//匹配
		for (Operation operation : operations) {
			for (ProgramBackup programBackup : programBackups) {
				if(programBackup.getId().equals(operation.getFileid())) {
					//把操作日志转化为客户报告
					clientReports.add(filler.fill(operation));
					break;
				}
			}
		}
		
		return clientReports;
	}

	@Override
	public ResponseEntity<byte[]> downloadClientReport(String client, String programNo, String line, String orderNo,
			String workOrderNo, String startTime, String endTime) throws ParseException, IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		//创建表头样式
		HSSFCellStyle headCs = workbook.createCellStyle();
		headCs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		headCs.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
		headCs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont headFont = workbook.createFont();
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headFont.setFontName("Arial");
		headFont.setFontHeightInPoints((short) 12);
		headCs.setFont(headFont);
		//创建数据样式
		HSSFCellStyle bodyCs = workbook.createCellStyle();
		bodyCs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont bodyFont = workbook.createFont();
		bodyFont.setFontName("Arial");
		bodyFont.setFontHeightInPoints((short) 10);
		bodyCs.setFont(bodyFont);
		//创建表头
		HSSFRow head = sheet.createRow(0);
		HSSFCell cell = null;
		cell = head.createCell(0);
		cell.setCellStyle(headCs);
		cell.setCellValue("产线编码");
		cell = head.createCell(1);
		cell.setCellStyle(headCs);
		cell.setCellValue("工单编码");
		cell = head.createCell(2);
		cell.setCellStyle(headCs);
		cell.setCellValue("客户订单号");
		cell = head.createCell(3);
		cell.setCellStyle(headCs);
		cell.setCellValue("槽位");
		cell = head.createCell(4);
		cell.setCellStyle(headCs);
		cell.setCellValue("物料料号");
		cell = head.createCell(5);
		cell.setCellStyle(headCs);
		cell.setCellValue("物料描述");
		cell = head.createCell(6);
		cell.setCellStyle(headCs);
		cell.setCellValue("物料规格");
		cell = head.createCell(7);
		cell.setCellStyle(headCs);
		cell.setCellValue("操作类型");
		cell = head.createCell(8);
		cell.setCellStyle(headCs);
		cell.setCellValue("操作人");
		cell = head.createCell(9);
		cell.setCellStyle(headCs);
		cell.setCellValue("操作时间");
		//获取数据
		List<ClientReport> clientReports = listClientReport(client, programNo, line, orderNo, workOrderNo, startTime, endTime);
		//创建表体
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH：mm：ss");
		for (int i = 0; i < clientReports.size(); i++) {
			ClientReport clientReport = clientReports.get(i);
			HSSFRow row = sheet.createRow(i + 1);
			HSSFCell cell2 = null;
			cell2 = row.createCell(0);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getLine());
			cell2 = row.createCell(1);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getWorkOrderNo());
			cell2 = row.createCell(2);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getOrderNo());
			cell2 = row.createCell(3);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getLineseat());
			cell2 = row.createCell(4);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getMaterialNo());
			cell2 = row.createCell(5);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getMaterialDescription());
			cell2 = row.createCell(6);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getMaterialSpecitification());
			cell2 = row.createCell(7);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getOperationType());
			cell2 = row.createCell(8);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(clientReport.getOperator());
			cell2 = row.createCell(9);
			cell2.setCellStyle(bodyCs);
			cell2.setCellValue(sdf.format(clientReport.getTime()));
		}
		//设置自动列宽
		for (int i = 0; i < 10; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 4 *256);
		}
		//导出到内存
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		HttpHeaders headers = new HttpHeaders(); 
		//设置头信息
		String time = sdf.format(new Date());
		String filename = new String(("客户报表" + time + ".xls").getBytes("utf-8"), "iso-8859-1");
		headers.setContentDispositionFormData("attachment", filename);   
		headers.setContentType(MediaType.parseMediaType("application/x-xls")); 
		//返回流
		return new ResponseEntity<byte[]>(bos.toByteArray(), headers, HttpStatus.CREATED);    
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
			.andTimeLessThanOrEqualTo(t);
		List<Operation> operations = operationMapper.selectByExample(operationExample);
		//线别筛选
		List<Operation> operations2 = new ArrayList<Operation>(operations.size());
		ProgramBackupExample programBackupExample = new ProgramBackupExample();
		programBackupExample.createCriteria().andLineEqualTo(line);
		List<ProgramBackup> programBackups = programBackupMapper.selectByExample(programBackupExample);
		for (Operation operation : operations) {
			for (ProgramBackup programBackup : programBackups) {
				if(programBackup.getId().equals(operation.getFileid())) {
					operations2.add(operation);
					break;
				}
			}
		}
		//遍历
		for (Operation operation : operations2) {
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
				break;
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


}
