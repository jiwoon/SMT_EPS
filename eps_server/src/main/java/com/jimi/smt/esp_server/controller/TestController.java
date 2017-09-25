package com.jimi.smt.esp_server.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {
	
	public int i = 100;
	
	private static Logger logger = LogManager.getLogger();
	
	@ResponseBody
	@RequestMapping("/a")
	public String test(String b) {
		logger.error("TEST TEST TEST2");
		return b;
	}
	
}
