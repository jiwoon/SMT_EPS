package com.jimi.smt.eps_server.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.eps_server.entity.vo.ProgramVO;
import com.jimi.smt.eps_server.service.ProgramService;
import com.jimi.smt.eps_server.util.ResultUtil;

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
	
	
	@RequestMapping("/goManage")
	public ModelAndView goManage() {
		return new ModelAndView("program/goManage");
	}
	
	
	@ResponseBody
	@RequestMapping("/list")
	public List<ProgramVO> list() {
		return programService.list();
	}
	
	
	@ResponseBody
	@RequestMapping("/delete")
	public ResultUtil delete(String id) {
		if(programService.delete(id)) {
			return ResultUtil.succeed();
		}else {
			return ResultUtil.failed();
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/upload")
	public ResultUtil  upload(MultipartFile  programFile, Integer boardType) {
		//文件名检查
		String originalFileName = programFile.getOriginalFilename();
		if(!originalFileName.endsWith(".xls") && !originalFileName.endsWith(".xlsx")){
			return ResultUtil.failed("上传失败，必须为xls\\xlsx格式的文件");
		}
		
		Map<String, Object> result = null;
		
		//格式检查
		try {
			result = programService.upload(programFile, boardType);
		} catch (IOException e) {
			return ResultUtil.failed("上传失败，IO错误，请重试，或联系开发者",e);
		} catch (RuntimeException e) {
			return ResultUtil.failed("上传失败，解析文件时出错，请参考排位表标准格式规范：http://39.108.231.15/eps_server/static/standard.docx",e);
		}
		
		//结果检查
		int realParseNum = (int)result.get("real_parse_num");
		int planParseNum = (int)result.get("plan_parse_num");
		String actionName = (String)result.get("action_name");
		if(realParseNum == 0) {
			return ResultUtil.failed("操作失败，请检查表格内是否有空行，若有去掉该行再重试");
		}else if(realParseNum < planParseNum){
			return ResultUtil.failed(actionName + "完成，共检测到"+ planParseNum +"张表，但只解析成功"+ realParseNum +"张表，请检查是否有空表或表中是否有空行");
		}else {
			return ResultUtil.succeed(actionName + "完成，共解析到"+ realParseNum +"张表");
		}
	}
	
	
}
