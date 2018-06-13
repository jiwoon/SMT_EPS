package com.jimi.smt.eps_server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jimi.smt.eps_server.annotation.Open;

/**
 * 登录拦截器，对带有@Open注解的方法无需登录
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			//如果是带@Open注解直接放行
			Open open = ((HandlerMethod) handler).getMethodAnnotation(Open.class);
			if(open != null) {
				return true;
			}
			//如果已登录则放行
			if(request.getSession().getAttribute("logined") != null) {
				return true;
			}
			//如果是POST则返回JSON否则跳转到登录界面
			if(request.getMethod().equals("POST")) {
				response.getWriter().println("{\"result\":\"failed_access_denied\"}");
			}else {
				request.getRequestDispatcher("/WEB-INF/jsp/user/goLogin.jsp").forward(request, response);
			}
			return false;
		}
		return true;
	}
	
}
