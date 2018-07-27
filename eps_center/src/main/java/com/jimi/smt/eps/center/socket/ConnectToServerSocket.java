package com.jimi.smt.eps.center.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.center.constant.Line;
import com.jimi.smt.eps.center.pack.BoardNumPackage;
import com.jimi.smt.eps.center.pack.BoardNumReplyPackage;
import com.jimi.smt.eps.center.pack.HeartPackage;
import com.jimi.smt.eps.center.pack.HeartReplyPackage;
import com.jimi.smt.eps.center.pack.LoginPackage;
import com.jimi.smt.eps.center.pack.LoginReplyPackage;

import cc.darhao.dautils.api.TextFileUtil;
import cc.darhao.jiminal.core.SyncCommunicator;

public class ConnectToServerSocket {

    private static Logger logger = LogManager.getRootLogger();

    private SyncCommunicator communicator;

    private static final String PACKAGE_PATH = "com.jimi.smt.eps.center.pack";

    /**
     * 超时时间
     */
    private static final int TIME_OUT = 100000;

    /**
     * 服务器IP
     */
    private static final String LOCAL_IP = "10.10.11.110";

    /**
     * 服务器端口
     */
    private static final int LOCAL_PORT = 23333;

    /**
     * 产线
     */
    private Line line;

    /**
     * 板子数量文件
     */
    private static final String fileBoardNum = "/home/pi/Downloads/msg.txt";

    /**
     * 报警灯、接驳台等硬件状态文件
     */
    private static final String fileState = "/home/pi/Downloads/state.txt";

    /**
     * 报警灯、接驳台、红外线初始状态
     */
    private static final String initState = "00000011";

    
    public ConnectToServerSocket() {
        // 连接serversocket
        communicator = new SyncCommunicator(LOCAL_IP, LOCAL_PORT, PACKAGE_PATH);
        communicator.setTimeout(TIME_OUT);
        try {
            communicator.connect();
            sendCmdToServerForLogin();
            logger.info("产线：" + line + "已连接serversocket");
            TextFileUtil.writeToFile(fileState, initState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * 发送心跳包到服务器
     */
    @SuppressWarnings("unused")
    public synchronized void sendCmdToServerForHeart() throws Exception {
        HeartPackage heartPackage = new HeartPackage();
        heartPackage.setLine(line);
        heartPackage.setTimestamp(new Date());
        String state = TextFileUtil.readFromFile(fileState);
        if (state.substring(state.length() - 3, state.length() - 2).equals("1")) {
            heartPackage.setAlarmEnabled(true);
        } else {
            heartPackage.setAlarmEnabled(false);
        }
        if (state.substring(state.length() - 2, state.length() - 1).equals("1")) {
            heartPackage.setConveyorEnabled(true);
        } else {
            heartPackage.setConveyorEnabled(false);
        }
        if (state.substring(state.length() - 1, state.length()).equals("1")) {
            heartPackage.setInfraredEnabled(true);
        } else {
            heartPackage.setInfraredEnabled(false);
        }
        HeartReplyPackage heartReplyPackage = (HeartReplyPackage) communicator.send(heartPackage);
        logger.info("在时间: " + heartPackage.getTimestamp() + " 产线: " + line + " 发送心跳包");
    }

    
    /**
     * 发送登录包到服务器
     */
    public synchronized void sendCmdToServerForLogin() throws Exception {
        LoginPackage loginPackage = new LoginPackage();
        loginPackage.setCenterControllerMAC(getMACAddress());
        LoginReplyPackage loginReplyPackage = (LoginReplyPackage) communicator.send(loginPackage);
        line = loginReplyPackage.getLine();
        logger.info("在时间: " + loginReplyPackage.getTimestamp() + " 产线: " + line + " 发送登录包");
    }

    
    /**
     * 发送上传板子数量包到服务器
     */
    @SuppressWarnings("unused")
    public synchronized void sendCmdToServerForBoardNum() throws Exception {
        BoardNumPackage boardNumPackage = new BoardNumPackage();
        String boardNum = TextFileUtil.readFromFile(fileBoardNum);
        if (boardNum == null || boardNum.equals("")) {
            TextFileUtil.writeToFile(fileBoardNum, "0");
            boardNumPackage.setBoardNum(Integer.parseInt("0"));
        } else {
            boardNumPackage.setBoardNum(Integer.parseInt(TextFileUtil.readFromFile(fileBoardNum)));
        }
        boardNumPackage.setLine(line);
        boardNumPackage.setTimestamp(new Date());
        BoardNumReplyPackage boardNumReplyPackage = (BoardNumReplyPackage) communicator.send(boardNumPackage);
        logger.info("在时间: " + boardNumPackage.getTimestamp() + " 产线: " + line + " 发送上传板子数量包");
    }

    
    /**
     * 重新连接
     */
    public void reconnect() throws Exception {
        communicator.close();
        communicator.connect();
        logger.info("重新连接成功");
    }

    
    /**
     * 获取树莓派的mac地址
     */
    private static String getMACAddress() throws Exception {
        String mac = null;
        try {
            Process pro = Runtime.getRuntime().exec("ifconfig");
            InputStream is = pro.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String message = br.readLine();
            int index = -1;
            while (message != null) {
                if ((index = message.indexOf("HWaddr")) > 0) {
                    mac = message.substring(index + 7).trim();
                    break;
                }
                message = br.readLine();
            }
            br.close();
            pro.destroy();
        } catch (IOException e) {
            System.out.println("出错了，不能得到mac地址!");
            return null;
        }
        return mac.replaceAll(":", " ");
    }
}
