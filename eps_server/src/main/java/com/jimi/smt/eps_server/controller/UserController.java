package com.jimi.smt.eps_server.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.eps_server.annotation.Open;
import com.jimi.smt.eps_server.entity.vo.UserVO;
import com.jimi.smt.eps_server.service.UserService;
import com.jimi.smt.eps_server.util.ResultUtil;

/**
 * 用户控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@RequestMapping("/goManage")
	public ModelAndView goManage() {
		return new ModelAndView("user/goManage");
	}
	
	
	@Open
	@RequestMapping("/goLogin")
	public ModelAndView goLogin() {
		return new ModelAndView("user/goLogin");
	}
	
	
	@ResponseBody
	@RequestMapping("/add")
	public ResultUtil add(String id, Integer classType, String name, Integer type, String password) {
		if(id == null && type == null) {
			ResultUtil.failed("参数不足");
			return ResultUtil.failed();
		}
		String result = userService.add(id, classType, name , type, password);
		if(result.equals("succeed")) {
			return ResultUtil.succeed();
		}else {
			return ResultUtil.failed(result);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/list")
	public List<UserVO> list(String id, Integer classType, String name, Integer type, String password, String orderBy, Boolean enabled) {
		return userService.list(id, classType, name , type, orderBy, enabled);
	}
	
	
	@ResponseBody
	@RequestMapping("/update")
	public ResultUtil update(String id, Integer classType, String name, Integer type, String password, Boolean enabled) {
		if(id == null) {
			ResultUtil.failed("参数不足");
			return ResultUtil.failed();
		}
		String result = userService.update(id, classType, name , type, password, enabled);
		if(result.equals("succeed")) {
			return ResultUtil.succeed();
		}else {
			return ResultUtil.failed(result);
		}
	}
	
	
	@Open
	@ResponseBody
	@RequestMapping("/login")
	public ResultUtil login(String id, String password, HttpSession session) {
		if(id == null) {
			ResultUtil.failed("参数不足");
			return ResultUtil.failed();
		}
		String result = userService.login(id, password);
		if(!result.startsWith("failed")) {
			session.setAttribute("logined", result);
			return ResultUtil.succeed();
		}else {
			return ResultUtil.failed(result);
		}
	}
	
	
}
