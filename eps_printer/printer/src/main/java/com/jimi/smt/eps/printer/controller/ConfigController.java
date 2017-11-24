package com.jimi.smt.eps.printer.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.printer.util.TextFileUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 配置页面控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ConfigController implements Initializable {

	private Logger logger = LogManager.getRootLogger();
	
	@FXML
	private TextField marginLeftTf;
	@FXML
	private TextField marginTopTf;
	@FXML
	private Button adjustBt;
	
	private Stage stage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			String xy = TextFileUtil.readFromFile("e.cfg");
			String[] margins = xy.split(",");
			marginLeftTf.setText(margins[0]);
			marginTopTf.setText(margins[1]);
		} catch (IOException e) {
			try {
				TextFileUtil.writeToFile("e.cfg", "0,0");
				initialize(arg0, arg1);
			} catch (IOException e1) {
				logger.error("e.cfg文件创建失败");
			}
			logger.error("e.cfg文件不存在，已试图创建");
		}
	}

	
	public void onAdjustClick() {
		try {
			TextFileUtil.writeToFile("e.cfg", marginLeftTf.getText() + "," + marginTopTf.getText());
			stage.close();
		} catch (IOException e) {
			new Alert(AlertType.ERROR, "保存失败", ButtonType.OK).show();
			logger.error("e.cfg文件保存失败");
		}
	}


	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
