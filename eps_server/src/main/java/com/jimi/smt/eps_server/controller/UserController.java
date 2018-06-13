package com.jimi.smt.eps_server.controller;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jimi.smt.eps_server.annotation.Log;
import com.jimi.smt.eps_server.annotation.Open;
import com.jimi.smt.eps_server.entity.vo.UserVO;
import com.jimi.smt.eps_server.service.UserService;
import com.jimi.smt.eps_server.util.QRCodeUtil;
import com.jimi.smt.eps_server.util.ResultUtil;

import cc.darhao.dautils.api.FontImageUtil;

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
	
	
	@Log
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
	
	
	@Log
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
	
	
	@Open
	@RequestMapping("/getCodePic")
	public ModelAndView getCodePic(HttpServletResponse response, HttpSession session, String id, Integer size) throws Exception {
		if(id == null || id.equals("")) {
			return new ModelAndView("user/goManage");
		} 
		//从session读取size
		if(size == null) {
			if(session.getAttribute("code_size") == null) {
				size = 300;
			}else {
				size = (Integer) session.getAttribute("code_size");
			}
		}
		//把size存到session
		session.setAttribute("code_size", size);
		response.setContentType("image/png");  
		//生成加密信息
		String encodedId = new String(Base64.getEncoder().encode(id.getBytes()));
		encodedId += "?";
		//生成二维码
		Font font = new Font("宋体", Font.PLAIN, 128 / id.length());
		FileOutputStream fileOutputStream = new FileOutputStream(new File(session.getServletContext().getRealPath("/static/temp.png")));
		BufferedImage textImage = FontImageUtil.createImage(id, font, 64, 64);
		QRCodeUtil.encode(encodedId, textImage, "", fileOutputStream, false);
		//传递参数
		ModelAndView modelAndView = new ModelAndView("user/getCodePic");
		modelAndView.addObject("id", id);
		modelAndView.addObject("size", size);
		return modelAndView;
	}
	
	
}
