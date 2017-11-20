package com.jimi.smt.eps_server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jimi.smt.eps_server.annotation.Open;
import com.jimi.smt.eps_server.annotation.Role;

/**
 * 权限拦截器，对带有@Role注解的方法进行选择性放行
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class JurisdictionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			//如果是带@Open注解直接放行
			Open open = ((HandlerMethod) handler).getMethodAnnotation(Open.class);
			if(open != null) {
				return true;
			}
			Role role = ((HandlerMethod) handler).getMethodAnnotation(Role.class);
			if(role == null) {
				response.getWriter().println("{\"result\":\"failed_access_denied\"}");
				return false;
			}
			//如果已登录则放行
			for (Role.RoleType roleName : role.roles()) {
				if(roleName.equals(request.getSession().getAttribute("logined"))) {
					return true;
				}
			}
			response.getWriter().println("{\"result\":\"failed_access_denied\"}");
			return false;
		}
		return true;
	}
	
}
