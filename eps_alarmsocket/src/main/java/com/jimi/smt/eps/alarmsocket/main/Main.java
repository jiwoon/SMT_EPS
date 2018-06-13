package com.jimi.smt.eps.alarmsocket.main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.jimi.smt.eps.alarmsocket.thread.ErrorCheckThread;

import cc.darhao.dautils.api.ResourcesUtil;

public class Main {
	
	public static void main(String[] args){
		//初始化Logger
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(ResourcesUtil.getResourceAsStream("log4j/log4j.xml"));
			Configurator.initialize(null, source);   
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//初始化ErrorCheck线程
		try {
			new ErrorCheckThread().start();
		} catch (Exception e) {
			LogManager.getRootLogger().error(e.getMessage());;
			e.printStackTrace();
		}
	}

}
