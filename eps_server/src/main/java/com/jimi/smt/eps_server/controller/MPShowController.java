package com.jimi.smt.eps_server.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.eps_server.annotation.Open;
import com.jimi.smt.eps_server.entity.vo.MPShowItemVO;
import com.jimi.smt.eps_server.service.MPShowService;
import com.jimi.smt.eps_server.util.ResultUtil;

/**
 * 公粽号报表控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/MPShow")
public class MPShowController {

	@Autowired
	private MPShowService mpShowService;
	
	
	@Open
	@RequestMapping("/goShow")
	public ModelAndView goShow() {
		return new ModelAndView("MPShow/goShow");
	}
	
	
	@Open
	@RequestMapping("/goCDShow")
	public ModelAndView goCDShow() {
		return new ModelAndView("MPShow/goCDShow");
	}
	
	
	@Open
	@ResponseBody
	@RequestMapping("/show")
	public List<MPShowItemVO> show(String startTime, String endTime) {
		try {
			return mpShowService.show(startTime, endTime);
		} catch (ParseException e) {
			ResultUtil.failed("日期格式不正确", e);
		}
		return null;
	}
	
}
