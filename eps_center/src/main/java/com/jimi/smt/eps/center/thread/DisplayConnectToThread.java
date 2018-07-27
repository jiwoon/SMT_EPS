package com.jimi.smt.eps.center.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.center.socket.DisplayConnectToSocket;

public class DisplayConnectToThread extends Thread {

    private static Logger logger = LogManager.getRootLogger();

    private DisplayConnectToSocket displayConnectToSocket = new DisplayConnectToSocket();

    @Override
    public void run() {
        // 提示已运行
        logger.info("中控  服务端     display连接线程已开启!");
        displayConnectToSocket.open();
    }
}
