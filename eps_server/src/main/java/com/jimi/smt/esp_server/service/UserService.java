package com.jimi.smt.esp_server.service;

import java.util.List;

import com.jimi.smt.esp_server.entity.vo.UserVO;

/**
 * 用户业务层
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface UserService {

	/**
	 * 新增一个用户
	 * @param id
	 * @param name
	 * @param type
	 * @param password
	 * @return
	 */
	String add(String id, String name, Integer type, String password);
	
	/**
	 * 修改指定工号的用户信息
	 * @param id
	 * @param name
	 * @param type
	 * @param password
	 * @param enabled
	 * @return
	 */
	String update(String id, String name, Integer type, String password, Boolean enabled);

	/**
	 * 根据条件列出用户
	 * 前三个为查询条件，多选为求交集；
		orderBy：
		根据字段名排序
		可能的值为：
		id(默认)
		name
		type
		create_time；
		enabled为true时，只显示在职用户，默认为false
	 * @param id
	 * @param name
	 * @param type
	 * @param password
	 * @param orderBy
	 * @param enabled
	 * @return
	 */
	List<UserVO> list(String id, String name, Integer type, String orderBy, Boolean enabled);
	
	/**
	 * 使用管理员id以及对应的密码进行登录（如果有），
	 * 只有登录成功后才能使用其他接口，
	 * 否则调用其他接口时会返回failed_access_denied
	 * @param id
	 * @param password
	 * @return
	 */
	String login(String id, String password);
	
}
