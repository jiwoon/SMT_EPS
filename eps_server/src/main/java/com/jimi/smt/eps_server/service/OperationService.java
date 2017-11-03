package com.jimi.smt.eps_server.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jimi.smt.eps_server.entity.vo.ClientReport;
import com.jimi.smt.eps_server.entity.vo.DisplayReport;
import com.jimi.smt.eps_server.entity.vo.OperationReport;

/**
 * 操作日志服务接口
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface OperationService {

	/**
	 * 根据条件列出客户报表
	 * @return
	 * @throws ParseException 
	 */
	List<ClientReport> listClientReport(String client, String programNo, String line, String orderNo, String workOrderNo, String startTime, String endTime) throws ParseException;
	
	/**
	 * 根据条件生成并下载excel
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	ResponseEntity<byte[]> downloadClientReport(String client, String programNo, String line, String orderNo, String workOrderNo, String startTime, String endTime) throws ParseException, IOException;

	/**
	 * 根据线号显示实时展示报表
	 * @param line
	 * @return
	 */
	DisplayReport listDisplayReport(String line);

	/**
	 * 根据条件列出客户报表
	 * @return
	 * @throws ParseException 
	 */
	List<OperationReport> listOperationReport(String client, String line, String workOrderNo, String startTime, String endTime , Integer type) throws ParseException;
	
	/**
	 * 根据条件生成并下载excel
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	ResponseEntity<byte[]> downloadOperationReport(String client, String line, String workOrderNo, String startTime, String endTime , Integer type) throws ParseException, IOException;
	
}
