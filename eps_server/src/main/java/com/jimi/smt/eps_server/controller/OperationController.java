package com.jimi.smt.eps_server.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.eps_server.annotation.Role;
import com.jimi.smt.eps_server.annotation.Role.RoleType;
import com.jimi.smt.eps_server.entity.vo.ClientReport;
import com.jimi.smt.eps_server.entity.vo.DisplayReport;
import com.jimi.smt.eps_server.entity.vo.OperationReport;
import com.jimi.smt.eps_server.service.OperationService;
import com.jimi.smt.eps_server.util.ResultUtil;

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
	
	
	@RequestMapping("/goDisplayReport")
	public ModelAndView goDisplayReport() {
		return new ModelAndView("operation/goDisplayReport");
	}
	
	
	@RequestMapping("/goDisplayReport2")
	public ModelAndView goDisplayReport2() {
		System.out.println("进入goDisplayReport2-Controller:" + System.currentTimeMillis());
		return new ModelAndView("operation/goDisplayReport2");
	}
	
	@Role(RoleType.IPQC)
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
	
	
	@Role(RoleType.IPQC)
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
	
	
	@Role(RoleType.IPQC)
	@ResponseBody
	@RequestMapping("/listOperationReport")
	public List<OperationReport> listOperationReport(String client, String line, String workOrderNo, String startTime, String endTime, Integer type) {
		if(type == null) {
			ResultUtil.failed("参数不足");
			return null;
		}
		try {
			return operationService.listOperationReport(client, line, workOrderNo, startTime, endTime, type);
		} catch (ParseException e) {
			ResultUtil.failed("日期格式不正确", e);
		}
		return null;
	}
	
	
	@Role(RoleType.IPQC)
	@RequestMapping("/downloadOperationReport")
	public ResponseEntity<byte[]> downloadOperationReport(String client, String line, String workOrderNo, String startTime, String endTime, Integer type){
		if(type == null) {
			ResultUtil.failed("参数不足");
			return null;
		}
		try {
			return operationService.downloadOperationReport(client, line, workOrderNo, startTime, endTime, type);
		} catch (ParseException e) {
			ResultUtil.failed("日期格式不正确", e);
		} catch (IOException e) {
			ResultUtil.failed("IO异常", e);
		}
		return null;
	}
	
	
	@Role(RoleType.IPQC)
	@ResponseBody
	@RequestMapping("/listDisplayReport")
	public DisplayReport listDisplayReport(String line) {
		if(line == null || line.equals("")) {
			return null;
		}
		DisplayReport displayReport = operationService.listDisplayReport(line);
		return displayReport;
	}
	
	
}
