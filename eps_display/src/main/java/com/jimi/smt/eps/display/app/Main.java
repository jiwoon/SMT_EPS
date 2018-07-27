package com.jimi.smt.eps.display.app;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.jimi.smt.eps.display.controller.DisplayController;

import cc.darhao.dautils.api.ResourcesUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private DisplayController displayController;
	
	private static final String VERSION = "2.0.0";
	
	private static final String FILE_NAME = "EPS_Display-" + VERSION + ".jar";
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
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
				//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/fxml/Display.fxml"));
				
				FXMLLoader loader = new FXMLLoader(ResourcesUtil.getResourceURL("fxml/Display.fxml"));
				Parent root =  loader.load();
				displayController = loader.getController();
				displayController.closeWindow(primaryStage);
				Scene scene = new Scene(root,1366,940);
				scene.getStylesheets().add(ResourcesUtil.getResourceURL("css/application.css").toExternalForm());
				primaryStage.setTitle("产线实时监控-V"+VERSION);
				primaryStage.setScene(scene);
				primaryStage.show();
				
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
			
		}
	}
	
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
	
	public static String getVersion() {
		return VERSION;
	}
}
