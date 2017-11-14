package com.jimi.smt.eps_server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jimi.smt.eps_server.entity.Config;
import com.jimi.smt.eps_server.mapper.ConfigMapper;
import com.jimi.smt.eps_server.service.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {

	@Autowired
	private ConfigMapper configMappler;
	

	@Override
	public List<Config> list() {
		return configMappler.selectByExample(null);
	}


	@Override
	public boolean set(List<Config> configs) {
		int i = 0;
		for (Config config : configs) {
			i += configMappler.updateByPrimaryKey(config);
		}
		if(i == configs.size()) {
			return true;
		}else {
			return false;
		}
	}

}
