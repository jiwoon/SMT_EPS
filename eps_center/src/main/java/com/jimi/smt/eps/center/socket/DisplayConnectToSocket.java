package com.jimi.smt.eps.center.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.center.constant.ControlResult;

import com.jimi.smt.eps.center.constant.ErrorCode;
import com.jimi.smt.eps.center.pack.BoardNumPackage;
import com.jimi.smt.eps.center.pack.BoardNumReplyPackage;
import com.jimi.smt.eps.center.pack.BoardResetPackage;
import com.jimi.smt.eps.center.pack.BoardResetReplyPackage;

import cc.darhao.dautils.api.TextFileUtil;
import cc.darhao.jiminal.callback.OnPackageArrivedListener;
import cc.darhao.jiminal.core.BasePackage;
import cc.darhao.jiminal.core.SyncCommunicator;

public class DisplayConnectToSocket {

    private static Logger logger = LogManager.getRootLogger();

    private SyncCommunicator communicatorServer;

    private SyncCommunicator communicatorClient;

    private static final String PACKAGE_PATH = "com.jimi.smt.eps.center.pack";

    private static final int TIME_OUT = 100000;

    private static final int SERVER_PORT = 23333;

    private static final int CLIENT_PORT = 23334;

    private static final String SERVER_IP = "10.10.11.110";
    

    /**
     * 板子数量文件
     */
     private static final String fileBoardNum = "/home/pi/Downloads/msg.txt";
    //private static final String fileBoardNum = "C:\\Users\\jimi\\Desktop\\msg.txt";

    
    public DisplayConnectToSocket() {
        // display连接到中控
        communicatorClient = new SyncCommunicator(CLIENT_PORT, PACKAGE_PATH);

        // 中控连接到serversocket
        communicatorServer = new SyncCommunicator(SERVER_IP, SERVER_PORT, PACKAGE_PATH);
        communicatorServer.setTimeout(TIME_OUT);
        try {
            communicatorServer.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * 打开端口，启动套接字服务器
     */
    public void open() {
        logger.info("SMT 中控  display服务端开启，监听包的到来!");
        communicatorClient.startServer(new OnPackageArrivedListener() {

            @SuppressWarnings("unused")
            @Override
            public void onPackageArrived(BasePackage p, BasePackage r) {
                try {
                    // 处理板子数量重置包逻辑
                    if (p instanceof BoardResetPackage && r instanceof BoardResetReplyPackage) {
                        BoardResetPackage boardResetPackage = (BoardResetPackage) p;
                        BoardResetReplyPackage boardResetReplyPackage = (BoardResetReplyPackage) r;
                        BoardNumPackage boardNumPackage = new BoardNumPackage();
                        String boardNum = TextFileUtil.readFromFile(fileBoardNum);
                        if (boardNum == null || boardNum.equals("")) {                            
                            boardNumPackage.setBoardNum(Integer.parseInt("0"));
                        } else {
                            boardNumPackage.setBoardNum(Integer.parseInt(TextFileUtil.readFromFile(fileBoardNum)));
                        }
                        boardNumPackage.setTimestamp(new Date());
                        boardNumPackage.setLine(boardResetPackage.getLine());
                        BoardNumReplyPackage boardNumReplyPackage = (BoardNumReplyPackage) communicatorServer
                                .send(boardNumPackage);
                        logger.info("在时间:" + boardNumPackage.getTimestamp() + " 产线：" + boardResetPackage.getLine().toString() + " 的display客户端 发送上传板子数量包");
                        TextFileUtil.writeToFile(fileBoardNum, "0");
                        boardResetReplyPackage.setClientDevice(boardResetPackage.getClientDevice());
                        if ("0".equals(TextFileUtil.readFromFile(fileBoardNum))) {
                            boardResetReplyPackage.setControlResult(ControlResult.SUCCEED);
                            boardResetReplyPackage.setErrorCode(ErrorCode.SUCCEED);
                        } else {
                            boardResetReplyPackage.setControlResult(ControlResult.FAILED);
                            boardResetReplyPackage.setErrorCode(ErrorCode.RELAY_FAILURE);
                        }
                    }
                    // 记录包协议
                    logger.info(p.protocol + "包到达");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCatchIOException(IOException e) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(bos);
                e.printStackTrace(printStream);
                logger.error(new String(bos.toByteArray()));
            }
        });
    }
}
