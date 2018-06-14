package com.jimi.smt.eps.printer.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.jimi.smt.eps.printer.controller.MainController;

import cc.darhao.dautils.api.ResourcesUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application{

	private MainController mainController;
	
	private static final String VERSION = "1.5";
	
	private static final String FILE_NAME = "EPS_Printer-" + VERSION + ".jar";

	@Override
	public void start(Stage primaryStage) throws Exception {
		//确保只有一个实例启动
		if(isRunning()) {
			new Alert(AlertType.WARNING, "另一个实例已经在运行中，请勿重复运行！", ButtonType.OK).show();
			new Thread(()->{
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}).start();
		}else {
			clearDeadProcess();
			FXMLLoader loader = new FXMLLoader(ResourcesUtil.getResourceURL("fxml/app.fxml"));
			Parent root = loader.load();
			//把Stage存入MainController
			mainController = loader.getController();
	        mainController.setPrimaryStage(primaryStage);
	        //显示
	        primaryStage.setTitle("防错料系统 - 条码打印器 " + VERSION);
	        primaryStage.setScene(new Scene(root));
	        primaryStage.show();
		}
	}
	
	
	
	/**
	 * 程序入口
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(ResourcesUtil.getResourceAsStream("log4j/log4j.xml"));
			Configurator.initialize(null, source);   
		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}
	
	
	@Override
	public void stop() throws Exception {
		mainController.getPrinterSocket().close();
		//发送RFID程序关闭指令
		MainController.getRfidSocket().getOutputStream().write('0');
		MainController.getRfidSocket().close();
	}
	
	
	/**
	 * 判断是否已经有一个实例在运行
	 * @return
	 * @throws IOException 
	 */
	public static boolean isRunning() throws IOException {
		String line = null;
		InputStream in = Runtime.getRuntime().exec("jps").getInputStream();
		BufferedReader b = new BufferedReader(new InputStreamReader(in));
		int count = 0;
		while ((line = b.readLine()) != null) {
			if (line.contains(FILE_NAME)) {
				count++;
				if (count > 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 清除僵尸进程
	 * @throws IOException 
	 */
	public static void clearDeadProcess() throws IOException {
		Runtime.getRuntime().exec("taskkill /im printer.exe /f");
		Runtime.getRuntime().exec("taskkill /im SMT_EPS_RFID_WRITER.exe /f");
	}
	
}
