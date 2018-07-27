package com.jimi.smt.eps.center.socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.center.constant.ControlResult;
import com.jimi.smt.eps.center.constant.ControlledDevice;
import com.jimi.smt.eps.center.constant.ErrorCode;
import com.jimi.smt.eps.center.constant.Operation;
import com.jimi.smt.eps.center.pack.ControlPackage;
import com.jimi.smt.eps.center.pack.ControlReplyPackage;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import cc.darhao.dautils.api.TextFileUtil;
import cc.darhao.jiminal.callback.OnPackageArrivedListener;
import cc.darhao.jiminal.core.BasePackage;
import cc.darhao.jiminal.core.SyncCommunicator;

public class AlarmConnectToSocket {

    private static Logger logger = LogManager.getRootLogger();

    private SyncCommunicator communicator;

    private static final String PACKAGE_PATH = "com.jimi.smt.eps.center.pack";

    /**
     * 对端口进行监听
     */
    private static final int LOCAL_PORT = 12345;

    /**
     * 初始化GPIO口数据
     */
    final GpioController gpio = GpioFactory.getInstance();

    GpioPinDigitalOutput io13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
    GpioPinDigitalOutput io15 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
    GpioPinDigitalOutput io16 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
    GpioPinDigitalOutput io18 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
   
    /**
     * 报警灯、接驳台等硬件状态文件
     */
    private static final String fileState = "/home/pi/Downloads/state.txt";
    
    
    public AlarmConnectToSocket() {
        communicator = new SyncCommunicator(LOCAL_PORT, PACKAGE_PATH);
    }

    /**
     * 打开端口，启动套接字服务器
     */
    public void open() {
        logger.info("SMT 中控服务端开启，监听包的到来!");
        communicator.startServer(new OnPackageArrivedListener() {

            @Override
            public void onPackageArrived(BasePackage p, BasePackage r) {
                try {
                  if (p instanceof ControlPackage && r instanceof ControlReplyPackage) {
                        ControlPackage controlPackage = (ControlPackage) p;
                        ControlReplyPackage controlReplyPackage = (ControlReplyPackage) r;
                        ControlledDevice controlledDevice = controlPackage.getControlledDevice();
                        controlReplyPackage.setClientDevice(controlPackage.getClientDevice());
                        if (controlledDevice == ControlledDevice.CENTER_CONTROLLER) {
                            if (controlPackage.getOperation() == Operation.OFF) {
                                // 系统关机
                                Runtime runtime = Runtime.getRuntime();
                                Process process = runtime.exec("halt -p");
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(process.getInputStream()));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    System.out.println(line);
                                }
                                reader.close();
                                controlReplyPackage.setControlResult(ControlResult.SUCCEED);
                                controlReplyPackage.setErrorCode(ErrorCode.SUCCEED);
                            } else if (controlPackage.getOperation() == Operation.RESET) {
                                // 系统重启
                                Runtime runtime = Runtime.getRuntime();
                                Process process = runtime.exec("init 6");
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(process.getInputStream()));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    System.out.println(line);
                                }
                                reader.close();
                                controlReplyPackage.setControlResult(ControlResult.SUCCEED);
                                controlReplyPackage.setErrorCode(ErrorCode.SUCCEED);
                            }
                        } else if (controlledDevice == ControlledDevice.CONVEYOR) {
                            if (controlPackage.getOperation() == Operation.OFF) {
                                // 接驳台关闭
                                logger.info("接驳台关闭");
                                io13.high();
                                Thread.sleep(200);
                                io13.low();
                                io18.high();
                                if (io13.isLow() && io18.isHigh()) {
                                    controlReplyPackage.setControlResult(ControlResult.SUCCEED);
                                    controlReplyPackage.setErrorCode(ErrorCode.SUCCEED);
                                    TextFileUtil.writeToFile(fileState, "00000001");
                                } else {
                                    controlReplyPackage.setControlResult(ControlResult.FAILED);
                                    controlReplyPackage.setErrorCode(ErrorCode.RELAY_FAILURE);
                                }
                            } else if (controlPackage.getOperation() == Operation.ON) {
                                // 接驳台打开
                                logger.info("接驳台打开");
                                io16.high();
                                Thread.sleep(200);
                                io16.low();
                                io18.low();
                                if (io16.isLow() && io18.isLow()) {
                                    controlReplyPackage.setControlResult(ControlResult.SUCCEED);
                                    controlReplyPackage.setErrorCode(ErrorCode.SUCCEED);
                                    TextFileUtil.writeToFile(fileState, "00000011");
                                } else {
                                    controlReplyPackage.setControlResult(ControlResult.FAILED);
                                    controlReplyPackage.setErrorCode(ErrorCode.RELAY_FAILURE);
                                }
                            }
                        } else if (controlledDevice == ControlledDevice.ALARM) {
                            if (controlPackage.getOperation() == Operation.OFF) {
                                // 报警灯关闭
                                logger.info("报警灯关闭");
                                io15.low();
                                if (io15.isLow()) {
                                    controlReplyPackage.setControlResult(ControlResult.SUCCEED);
                                    controlReplyPackage.setErrorCode(ErrorCode.SUCCEED);
                                    TextFileUtil.writeToFile(fileState, "00000011");
                                } else {
                                    controlReplyPackage.setControlResult(ControlResult.FAILED);
                                    controlReplyPackage.setErrorCode(ErrorCode.RELAY_FAILURE);
                                }
                            } else if (controlPackage.getOperation() == Operation.ON) {
                                // 报警灯打开
                                logger.info("报警灯打开");
                                io15.high();
                                if (io15.isHigh()) {
                                    controlReplyPackage.setControlResult(ControlResult.SUCCEED);
                                    controlReplyPackage.setErrorCode(ErrorCode.SUCCEED);
                                    TextFileUtil.writeToFile(fileState, "00000111");
                                } else {
                                    controlReplyPackage.setControlResult(ControlResult.FAILED);
                                    controlReplyPackage.setErrorCode(ErrorCode.RELAY_FAILURE);
                                }
                            }
                        }
                    }
                    // 记录包协议
                    logger.info(p.protocol + "包到达");
                } catch (Exception e) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PrintStream printStream = new PrintStream(bos);
                    e.printStackTrace(printStream);
                    logger.error(new String(bos.toByteArray()));
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
