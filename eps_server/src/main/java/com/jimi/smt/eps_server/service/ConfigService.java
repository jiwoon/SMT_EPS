package com.jimi.smt.eps_server.service;

import java.util.List;

import com.jimi.smt.eps_server.entity.Config;

/**
 * 配置服务接口
 * 
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface ConfigService {
	
	/**
	 * 更新配置
	 * @param configs
	 * @return
	 */
	boolean set(List<Config> configs);

	
	/**
	 * 列出所有配置
	 * @return
	 */
	List<Config> list();

}
