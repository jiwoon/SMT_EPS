package com.jimi.smt.esp_server.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jimi.smt.esp_server.entity.User;
import com.jimi.smt.esp_server.entity.UserExample;
import com.jimi.smt.esp_server.entity.UserExample.Criteria;
import com.jimi.smt.esp_server.entity.filler.UserToUserVOFiller;
import com.jimi.smt.esp_server.entity.vo.UserVO;
import com.jimi.smt.esp_server.mapper.UserMapper;
import com.jimi.smt.esp_server.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserToUserVOFiller filler;
	
	@Override
	public String add(String id, Integer classType, String name, Integer type, String password) {
		if(userMapper.selectByPrimaryKey(id) != null) {
			return "failed_id_exist";
		}
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setType(type);
		user.setPassword(password);
		user.setCreateTime(new Date());
		if(userMapper.insertSelective(user) == 1) {
			return "succeed";
		}
		return "failed_unknown";
	}

	@Override
	public String update(String id, Integer classType, String name, Integer type, String password, Boolean enabled) {
		if(userMapper.selectByPrimaryKey(id) == null) {
			return "failed_not_found";
		}
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setType(type);
		user.setPassword(password);
		user.setEnabled(enabled);
		if(userMapper.updateByPrimaryKeySelective(user) == 1){
			return "succeed";
		}
		return "failed_unknown";
	}

	@Override
	public List<UserVO> list(String id, Integer classType, String name, Integer type, String orderBy, Boolean enabled) {
		UserExample userExample = new UserExample();
		Criteria criteria = userExample.createCriteria();
		if(id != null) {
			criteria.andIdEqualTo(id);
		}
		if(name != null) {
			criteria.andNameEqualTo(name);
		}
		if(type != null) {
			criteria.andTypeEqualTo(type);
		}
		if(enabled != null) {
			criteria.andEnabledEqualTo(enabled);
		}
		if(classType != null) {
			criteria.andClassTypeEqualTo(classType);
		}
		userExample.setOrderByClause(orderBy);
		return filler.fill(userMapper.selectByExample(userExample));
	}

	@Override
	public String login(String id, String password) {
		User user = userMapper.selectByPrimaryKey(id);
		if(user == null || user.getType() != 3) {
			return "failed_not_admin";
		}
		if(user.getEnabled() == false) {
			return "failed_not_enabled";
		}
		if(user.getPassword() != null && !user.getPassword().equals(password)) {
			return "failed_wrong_password";
		}
		return "succeed";
	}

}
