package com.jimi.smt.eps.printer.app;

import java.io.IOException;

import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.jimi.smt.eps.printer.controller.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	private MainController mainController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Resources.getResourceURL("fxml/main.fxml"));
		Parent root = loader.load();
		//把Stage存入MainController
		mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);
        //显示
        primaryStage.setTitle("防错料系统 - 条码打印器");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}
	
	
	
	/**
	 * 程序入口
	 * @param args
	 */
	public static void main(String[] args) {
		
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(Resources.getResourceAsStream("log4j/log4j.xml"));
			Configurator.initialize(null, source);   
		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}
	
	
	@Override
	public void stop() throws Exception {
		mainController.getPrinterSocket().close();
	}
	
}
