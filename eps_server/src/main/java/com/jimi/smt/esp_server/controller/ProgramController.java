package com.jimi.smt.esp_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.esp_server.util.ResultJSON;

/**
 * 排位表控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/program")
public class ProgramController {

	@RequestMapping("/goUpload")
	public ModelAndView goUpload() {
		return new ModelAndView("upload");
	}
	
	
	@ResponseBody
	@RequestMapping("/upload")
	public ResultJSON  upload(MultipartFile  programFile) {
		System.out.println(programFile);
		return new ResultJSON("succeed");
	}
	
	
}
