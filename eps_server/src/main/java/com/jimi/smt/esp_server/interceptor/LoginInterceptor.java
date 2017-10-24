package com.jimi.smt.esp_server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jimi.smt.esp_server.annotation.Open;

/**
 * 登录拦截器，对带有@Open注解的方法无需登录
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			Open open = ((HandlerMethod) handler).getMethodAnnotation(Open.class);
			if(open != null) {
				return true;
			}
			if(request.getSession().getAttribute("logined") != null) {
				return true;
			}
			//跳转到登录页面
			request.getRequestDispatcher("/WEB-INF/jsp/user/goLogin.jsp").forward(request, response);
			return false;
		}
		return true;
	}
	
}
