package com.jimi.smt.eps_server.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.jimi.smt.eps_server.annotation.Log;
import com.jimi.smt.eps_server.entity.ActionLog;
import com.jimi.smt.eps_server.mapper.ActionLogMapper;

/**
 * 日志拦截器，对带有@Log注解的方法进行日志记录
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ActionLogMapper actionLogMapper;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			//拦截带@Log注解的方法
			Log log = ((HandlerMethod) handler).getMethodAnnotation(Log.class);
			if(log != null) {
				//记录日志
				String url = request.getRequestURL().toString();
				String ip = request.getRemoteAddr();
				String parameters = JSON.toJSONString(request.getParameterMap());
				ActionLog actionLog = new ActionLog();
				actionLog.setIp(ip);
				actionLog.setParameters(parameters);
				actionLog.setTime(new Date());
				actionLog.setUrl(url);
				int result = actionLogMapper.insert(actionLog);
				//日志插入失败时不执行后续
				if(result == 0) {
					return false;
				}
			}
			return true;
		}
		return true;
	}
	
}
