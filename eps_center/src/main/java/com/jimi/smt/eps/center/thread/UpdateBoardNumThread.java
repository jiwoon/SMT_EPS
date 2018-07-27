package com.jimi.smt.eps.center.thread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import cc.darhao.dautils.api.TextFileUtil;

public class UpdateBoardNumThread extends Thread {

    private static Logger logger = LogManager.getRootLogger();

    private int num = 0;

    /**
     * 板子数量文件
     */
    private static final String fileBoardNum = "/home/pi/Downloads/msg.txt";

    /**
     * 初始化GPIO口数据
     */
    final GpioController gpio = GpioFactory.getInstance();

    @Override
    public void run() {
        // 提示已运行
        logger.info("中控  服务端     更新板子数量线程已开启!");
        GpioPinDigitalInput io29 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, PinPullResistance.PULL_UP);
        io29.setShutdownOptions(true);
        logger.info("开始进行监听并更新板子数量");
        io29.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState().isLow()) {
                    try {
                        String num_str = TextFileUtil.readFromFile(fileBoardNum);
                        if (num_str != null && !num_str.isEmpty()) {
                            num = Integer.parseInt(num_str);
                            num = num + 1;
                            TextFileUtil.writeToFile(fileBoardNum, num + "");
                            logger.info("板子数量+1,现在数量为：" + TextFileUtil.readFromFile(fileBoardNum));
                        }
                    } catch (IOException e) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        PrintStream printStream = new PrintStream(bos);
                        e.printStackTrace(printStream);
                        logger.error(new String(bos.toByteArray()));
                    }
                }
            }
        });
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(bos);
                e.printStackTrace(printStream);
                logger.error(new String(bos.toByteArray()));
            }
        }
    }
}
