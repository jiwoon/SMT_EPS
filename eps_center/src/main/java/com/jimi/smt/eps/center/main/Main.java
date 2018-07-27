package com.jimi.smt.eps.center.main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.jimi.smt.eps.center.socket.AlarmConnectToSocket;
import com.jimi.smt.eps.center.thread.ConnectToServerThread;
import com.jimi.smt.eps.center.thread.DisplayConnectToThread;
import com.jimi.smt.eps.center.thread.UpdateBoardNumThread;

import cc.darhao.dautils.api.ResourcesUtil;

public class Main {

    public static void main(String[] args) throws IOException {
        // 初始化Logger
        ConfigurationSource source;
        try {
            source = new ConfigurationSource(ResourcesUtil.getResourceAsStream("log4j/log4j.xml"));
            Configurator.initialize(null, source);
            // 开启ConnectToServerThread
             new ConnectToServerThread().start();
            // 开启UpdateBoardNumThread
             new UpdateBoardNumThread().start();
            // 开启DisplayConnectToThread
            new DisplayConnectToThread().start();
        } catch (IOException e) {
            LogManager.getRootLogger().error(e.getMessage());
            e.printStackTrace();
        }

        // 初始化alarmConnectToSocket
         AlarmConnectToSocket alarmConnectToSocket = new AlarmConnectToSocket();
         alarmConnectToSocket.open();

    }
}
