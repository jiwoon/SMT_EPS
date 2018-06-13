package com.jimi.smt.eps.printer.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cc.darhao.dautils.api.BytesParser;
import cc.darhao.dautils.api.DateUtil;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * RFID写入任务控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class RfidController implements Initializable {

	private Logger logger = LogManager.getRootLogger();
	
	@FXML
	private Label materialNoLb;
	@FXML
	private Label quantityLb;
	@FXML
	private Label timeLb;
	@FXML
	private Label idLb;
	@FXML
	private Label tipLb;
	@FXML
	private Button exitBt;
	@FXML
	private Button writeBt;
	@FXML
	private AnchorPane parentAp;
	
	private Alert alert;

	private Socket rfidSocket;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//获取用于写入的数据
		String[] dataSet = MainController.getData().split("@");
		//显示在界面
		materialNoLb.setText(dataSet[0]);
		quantityLb.setText(dataSet[1]);
		timeLb.setText(DateUtil.yyyyMMddHHmmss(new Date(Long.valueOf(dataSet[2]))));
		idLb.setText(dataSet[3]);
		//获取socket
		rfidSocket = MainController.getRfidSocket();
		//初始化热键
		initHotKey();
		writeBt.requestFocus();
		//初始化结果
		MainController.rfidResult = false;
	}

	
	private void initHotKey() {
		//初始化写入热键
		parentAp.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().compareTo(KeyCode.SPACE) == 0){
					if(!writeBt.isDisable()) {
						onWriteBtClick();
					}
				}
			}
		});
	}
	

	public void onWriteBtClick(){
		//禁用按钮
		writeBt.setDisable(true);
		writeBt.setText("写入中...");
		//开启发送子线程
		new Thread() {
			
			private String errorMsg = "";
			
			public void run() {
				try {
					//构造数据
					byte[] data = new byte[63];
					byte[] temp = MainController.getData().getBytes();//第一次：把自然语言转换成十六进制文本
					if(temp.length > data.length) {
						throw new Exception("数据过大，无法写入");
					}
					for (int i = 0; i < temp.length; i++) {
						data[i] = temp[i];
					}
					//转换成十六进制文本后取出字符集
					data = toHexStringAndToBytes(data);//第二次：把十六进制文本转换成十六进制字节集
					//发送准备写入指令
					rfidSocket.getOutputStream().write('1');
					//发送数据
					rfidSocket.getOutputStream().write(data);
					//接收返回值
					int result = rfidSocket.getInputStream().read();
					if(result != '1') {
						throw new Exception("失败，请靠近再重试");
					}
				} catch (IOException e) {
					e.printStackTrace();
					errorMsg = "与RFID程序通讯失败";
				}catch (Exception e) {
					errorMsg = e.getMessage();
				}
				//在UI线程执行
				Platform.runLater(()->{
					if(errorMsg.equals("")) {
						MainController.rfidResult = true;
						alert.close();
					}else {
						error(errorMsg);
						writeBt.setDisable(false);
						writeBt.setText("按下空格 写入数据");
						writeBt.requestFocus();
					}
				});
			}
		}.start();
	}
	
	
	private byte[] toHexStringAndToBytes(byte[] data) {
		String hexData = BytesParser.parseBytesToHexString(BytesParser.cast(data));
		String[] hexDatas = hexData.split(" ");
		String resultData = "";
		for (String da : hexDatas) {
			resultData += da;
		}
		data = resultData.getBytes();
		return data;
	};
	
	
	public void error(String message) {
		tipLb.setTextFill(Color.RED);
		tipLb.setText(message);
		logger.error(message);
	}

	public void info(String message) {
		tipLb.setTextFill(Color.BLACK);
		tipLb.setText(message);
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	
}
