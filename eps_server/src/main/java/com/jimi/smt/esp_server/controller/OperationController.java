package com.jimi.smt.esp_server.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.esp_server.annotation.Open;
import com.jimi.smt.esp_server.entity.vo.ClientReport;
import com.jimi.smt.esp_server.entity.vo.DisplayReport;
import com.jimi.smt.esp_server.service.OperationService;
import com.jimi.smt.esp_server.util.ResultUtil;

/**
 * 操作日志控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/operation")
public class OperationController {

	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("/goClientReport")
	public ModelAndView goClientReport() {
		return new ModelAndView("operation/goClientReport");
	}
	
	
	@RequestMapping("/goIPQCReport")
	public ModelAndView goIPQCReport() {
		return new ModelAndView("operation/goIPQCReport");
	}
	
	
	@RequestMapping("/goStoreReport")
	public ModelAndView goStoreReport() {
		return new ModelAndView("operation/goStoreReport");
	}
	
	
	@Open
	@RequestMapping("/goDisplayReport")
	public ModelAndView goDisplayReport() {
		return new ModelAndView("operation/goDisplayReport");
	}
	
	
	@Open
	@RequestMapping("/goDisplayReport2")
	public ModelAndView goDisplayReport2() {
		return new ModelAndView("operation/goDisplayReport2");
	}
	
	
	@ResponseBody
	@RequestMapping("/listClientReport")
	public List<ClientReport> listClientReport(String client, String programNo, String line, String orderNo, String workOrderNo, String startTime, String endTime) {
		try {
			return operationService.listClientReport(client, programNo, line, orderNo, workOrderNo, startTime, endTime);
		} catch (ParseException e) {
			ResultUtil.failed("日期格式不正确", e);
		}
		return null;
	}
	
	
	@RequestMapping("/downloadClientReport")
	public ResponseEntity<byte[]> downloadClientReport(String client, String programNo, String line, String orderNo, String workOrderNo, String startTime, String endTime){
		try {
			return operationService.downloadClientReport(client, programNo, line, orderNo, workOrderNo, startTime, endTime);
		} catch (ParseException e) {
			ResultUtil.failed("日期格式不正确", e);
		} catch (IOException e) {
			ResultUtil.failed("IO异常", e);
		}
		return null;
	}
	
	
	@Open
	@ResponseBody
	@RequestMapping("/listDisplayReport")
	public DisplayReport listDisplayReport(String line) {
		return operationService.listDisplayReport(line);
	}
	
	
}
