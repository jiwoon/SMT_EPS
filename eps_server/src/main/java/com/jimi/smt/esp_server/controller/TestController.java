package com.jimi.smt.esp_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {
	
	public int i = 100;
	
	@ResponseBody
	@RequestMapping("/a")
	public String test(String b) {
		return b;
	}
	
}
