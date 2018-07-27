package com.jimi.smt.eps.serversocket.main;

import java.io.IOException;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.jimi.smt.eps.serversocket.socket.ServerSocket;
import cc.darhao.dautils.api.ResourcesUtil;

public class Main {

    public static void main(String[] args) throws IOException {
        // 初始化Logger
        ConfigurationSource source;
        try {
            source = new ConfigurationSource(ResourcesUtil.getResourceAsStream("log4j/log4j.xml"));
            Configurator.initialize(null, source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 初始化ServerSocket
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.open();
    }

}
