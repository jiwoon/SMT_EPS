package com.jimi.smt.eps.center.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.center.socket.ConnectToServerSocket;

public class ConnectToServerThread extends Thread {

    private static Logger logger = LogManager.getRootLogger();

    /**
     * 心跳包周期
     */
    private static final long heartCycle = 180000;

    /**
     * 上传板子数量包周期
     */
    private static final long boardCycle = 1800000;

    /**
     * 中控连接服务器的socket
     */
    private ConnectToServerSocket connectToServerSocket = new ConnectToServerSocket();

    @Override
    public void run() {
        // 提示已运行
        logger.info("中控   客户端    线程已开启!");
        try {
            Thread heart = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(heartCycle);
                            connectToServerSocket.sendCmdToServerForHeart();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            heart.start();
            Thread boardNum = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(boardCycle);
                            connectToServerSocket.sendCmdToServerForBoardNum();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            boardNum.start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            try {
                connectToServerSocket.reconnect();
                run();
            } catch (Exception e1) {
                e1.printStackTrace();
                logger.error(e1.getMessage());
            }
        }
    }
}
