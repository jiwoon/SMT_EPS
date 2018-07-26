package com.jimi.smt.eps_server.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jimi.smt.eps_server.annotation.Open;
import com.jimi.smt.eps_server.entity.StatusDetail;
import com.jimi.smt.eps_server.entity.vo.StatusDetailsVO;
import com.jimi.smt.eps_server.service.StatusService;


@Controller
@RequestMapping("/")
public class StatusDetailsController {
	
	@Autowired
	StatusService statusService;
	@Open
	@ResponseBody
	@RequestMapping("/getStatusDetails")
	public StatusDetailsVO ListStatusDetailsByHour(String line) {
		return statusService.ListStatusDetailsByHour(line);
	}
	
	@Open
	@ResponseBody
	@RequestMapping("/getStatusDetailsByDay")
	public List<StatusDetail> ListStatusDetailsByDay() {
		return statusService.ListStatusDetailsByDay();
	}
}
