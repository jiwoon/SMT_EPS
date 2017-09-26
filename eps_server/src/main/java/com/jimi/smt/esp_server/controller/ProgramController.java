package com.jimi.smt.esp_server.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.esp_server.exception.XLSException;
import com.jimi.smt.esp_server.service.ProgramService;
import com.jimi.smt.esp_server.util.ResultUtil;

/**
 * 排位表控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/program")
public class ProgramController {

	@Autowired
	private ProgramService programService;
	
	
	@RequestMapping("/goUpload")
	public ModelAndView goUpload() {
		return new ModelAndView("upload");
	}
	
	
	@ResponseBody
	@RequestMapping("/upload")
	public ResultUtil  upload(MultipartFile  programFile) {
		String originalFileName = programFile.getOriginalFilename();
		if(!originalFileName.endsWith(".xls") && !originalFileName.endsWith(".xlsx")){
			return ResultUtil.failed("上传失败，必须为xls\\xlsx格式的文件");
		}
		try {
			programService.upload(programFile);
		} catch (IOException e) {
			return ResultUtil.failed("上传失败，未知错误，请重试",e);
		} catch (XLSException e) {
			return ResultUtil.failed("上传失败，解析文件时出错，请确保是标准的排班表文件",e);
		}
		return ResultUtil.succeed();
	}
	
	
}
