package com.jimi.smt.esp_server.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
		int num = 0;
		try {
			num = programService.upload(programFile);
		} catch (IOException e) {
			return ResultUtil.failed("上传失败，IO错误，请重试，或联系开发者",e);
		} catch (RuntimeException e) {
			return ResultUtil.failed("上传失败，解析文件时出错，请确保是标准的排班表文件",e);
		}
		if(num == 0) {
			return ResultUtil.failed("上传失败，请检查表格第10行是否为空，若为空则去掉该行再重试");
		}
		return ResultUtil.succeed("上传成功，共上传"+num+"张表");
	}
	
	
}
