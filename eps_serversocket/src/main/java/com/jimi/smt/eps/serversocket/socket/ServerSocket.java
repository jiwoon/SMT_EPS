package com.jimi.smt.eps.serversocket.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.serversocket.constant.Line;
import com.jimi.smt.eps.serversocket.entity.BoardNum;
import com.jimi.smt.eps.serversocket.entity.Display;
import com.jimi.smt.eps.serversocket.entity.DisplayExample;
import com.jimi.smt.eps.serversocket.entity.Log;
import com.jimi.smt.eps.serversocket.entity.Login;
import com.jimi.smt.eps.serversocket.entity.LoginExample;
import com.jimi.smt.eps.serversocket.entity.Program;
import com.jimi.smt.eps.serversocket.entity.ProgramExample;
import com.jimi.smt.eps.serversocket.entity.ProgramExample.Criteria;
import com.jimi.smt.eps.serversocket.mapper.BoardNumMapper;
import com.jimi.smt.eps.serversocket.mapper.DisplayMapper;
import com.jimi.smt.eps.serversocket.mapper.LogMapper;
import com.jimi.smt.eps.serversocket.mapper.LoginMapper;
import com.jimi.smt.eps.serversocket.mapper.ProgramMapper;
import com.jimi.smt.eps.serversocket.pack.BoardNumPackage;
import com.jimi.smt.eps.serversocket.pack.LoginPackage;
import com.jimi.smt.eps.serversocket.pack.LoginReplyPackage;
import com.jimi.smt.eps.serversocket.util.MybatisHelper;
import com.jimi.smt.eps.serversocket.util.MybatisHelper.MybatisSession;

import cc.darhao.dautils.api.BytesParser;
import cc.darhao.dautils.api.FieldUtil;
import cc.darhao.jiminal.callback.OnPackageArrivedListener;
import cc.darhao.jiminal.core.BasePackage;
import cc.darhao.jiminal.core.PackageParser;
import cc.darhao.jiminal.core.SyncCommunicator;

/**
 * 包测试套接字 <br>
 * <b>2018年1月6日</b>
 * 
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ServerSocket {

    private static Logger logger = LogManager.getRootLogger();

    private SyncCommunicator communicator;

    private static final int LOCAL_PORT = 23333;

    private static final String PACKAGE_PATH = "com.jimi.smt.eps.serversocket.pack";

    private static final String MYBATIS_CONFIG_PATH = "mybatis/mybatis-config.xml";

    /**
     * 物料是几联板
     */
    private int structure;

    /**
     * 当前工单记录到数据库的已生产数量
     */
    private int alreadyProductOld;

    public ServerSocket() {
        communicator = new SyncCommunicator(LOCAL_PORT, PACKAGE_PATH);
    }

    /**
     * 打开端口，启动套接字服务器
     */
    public void open() {
        logger.info("SMT server socket is running now!");
        communicator.startServer(new OnPackageArrivedListener() {

            @Override
            public void onPackageArrived(BasePackage p, BasePackage r) {
                try {
                    MybatisSession<LogMapper> logSession = null;
                    MybatisSession<LoginMapper> loginSession = null;
                    MybatisSession<DisplayMapper> displaySession = null;
                    MybatisSession<ProgramMapper> programSession = null;
                    MybatisSession<BoardNumMapper> boardNumSession = null;
                    logSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, LogMapper.class);
                    loginSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, LoginMapper.class);
                    boardNumSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, BoardNumMapper.class);
                    displaySession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, DisplayMapper.class, "smt");
                    programSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, ProgramMapper.class, "smt");
                    // 处理登录包逻辑
                    if (p instanceof LoginPackage && r instanceof LoginReplyPackage) {
                        LoginPackage loginPackage = (LoginPackage) p;
                        LoginReplyPackage loginReplyPackage = (LoginReplyPackage) r;
                        // 根据mac地址获取线别
                        String mac = loginPackage.getCenterControllerMAC();
                        LoginExample example = new LoginExample();
                        example.createCriteria().andMacEqualTo(mac);
                        List<Login> logins = loginSession.getMapper().selectByExample(example);
                        if (!logins.isEmpty()) {
                            Login login = logins.get(0);
                            loginReplyPackage.setLine(Line.valueOf("L" + login.getLine()));
                            loginReplyPackage.setTimestamp(new Date());
                            // //更新IP
                            // login.setIp(p.senderIp);
                            // loginSession.getMapper().updateByPrimaryKey(login);
                            // loginSession.commit();
                        } else {
                            loginReplyPackage.setLine(Line.L30X);
                            loginReplyPackage.setTimestamp(new Date());
                        }
                        // 处理板子数量上传包逻辑
                    } else if (p instanceof BoardNumPackage) {
                        BoardNumPackage boardNumPackage = (BoardNumPackage) p;
                        /* if (boardNumPackage.getLine().toString().equals("L308")) { */
                        DisplayExample displayExample = new DisplayExample();
                        displayExample.createCriteria()
                                .andLineEqualTo(boardNumPackage.getLine().toString().substring(1, 4));
                        List<Display> displays = displaySession.getMapper().selectByExample(displayExample);
                        if (!displays.isEmpty()) {
                            Display display = displays.get(0);
                            if (display.getWorkOrder() != null && display.getBoardType() != null) {
                                ProgramExample programExample = new ProgramExample();
                                Criteria criteria = programExample.createCriteria();
                                criteria.andWorkOrderEqualTo(display.getWorkOrder());
                                criteria.andBoardTypeEqualTo(display.getBoardType());
                                criteria.andLineEqualTo(boardNumPackage.getLine().toString().substring(1, 4));
                                List<Program> programs = programSession.getMapper().selectByExample(programExample);
                                if (!programs.isEmpty()) {
                                    Program programFirst = programs.get(0);
                                    structure = programFirst.getStructure();
                                    alreadyProductOld = programFirst.getAlreadyProduct();
                                    Program program = new Program();
                                    // criteria.andIdEqualTo(programFirst.getId());
                                    program.setAlreadyProduct(
                                            (boardNumPackage.getBoardNum()) * structure + alreadyProductOld);
                                    programSession.getMapper().updateByExampleSelective(program, programExample);
                                    programSession.commit();
                                }
                            }
                        }
                        /*
                         * } else { BoardNum boardNum = new BoardNum(); String line =
                         * boardNumPackage.getLine().toString(); line = line.substring(1,
                         * line.length()); boardNum.setLine(line);
                         * boardNum.setNum(boardNumPackage.getBoardNum());
                         * boardNum.setTime(boardNumPackage.getTimestamp());
                         * boardNumSession.getMapper().insert(boardNum); boardNumSession.commit(); }
                         */
                    }
                    // 创建日志：收到的包
                    Log pLog = createLogByPackage(p);
                    logSession.getMapper().insert(pLog);
                    // 创建日志：回复的包
                    Log rLog = createLogByPackage(r);
                    logSession.getMapper().insert(rLog);
                    logSession.commit();
                    // 记录包协议
                    logger.info(p.protocol + "package arrived");
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

    /**
     * 根据包创建日志实体
     * 
     * @param p
     * @return
     */
    private Log createLogByPackage(BasePackage p) {
        Log log = new Log();
        FieldUtil.copy(p, log);
        log.setTime(new Date());
        String data = BytesParser.parseBytesToHexString(PackageParser.serialize(p));
        log.setData(data);
        return log;
    }
}
