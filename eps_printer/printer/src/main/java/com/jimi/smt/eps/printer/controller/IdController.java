package com.jimi.smt.eps.printer.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.printer.entity.UserExample;
import com.jimi.smt.eps.printer.mapper.UserMapper;
import com.jimi.smt.eps.printer.util.MybatisHelper;
import com.jimi.smt.eps.printer.util.MybatisHelper.MybatisSession;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * id扫描控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class IdController implements Initializable {

	private Logger logger = LogManager.getRootLogger();
	
	@FXML
	private TextField idTf;
	@FXML
	private Label tipLb;
	
	private MainController mainController;

	private Stage stage;
	
	class QueryTask extends Task<Void>{
		String errorMsg;
		String value;
		
		public QueryTask(String value) {
			this.value = value;
		}
		
		@Override
		protected Void call() throws Exception {
			String id = null;
			try {
				//base64解码（去掉最后一个附加的等号）
				id = new String(Base64.getDecoder().decode(value.substring(0, value.length() - 1)));
				//查询数据库是否存在该职工
				MybatisSession<UserMapper> userSession = MybatisHelper.getMS("mybatis/mybatis-config.xml",UserMapper.class);
				UserExample userExample = new UserExample();
				userExample.createCriteria().andIdEqualTo(id).andEnabledEqualTo(true);
				if(userSession.getMapper().selectByExample(userExample).isEmpty()) {
					//大小写反转再解码，兼容扫描枪bug
					char[] chars = value.toCharArray();
					for (int i = 0 ; i < chars.length ; i++) {
						char c = chars[i];
						if(c >= 'A' && c <= 'Z') {
							c += 32;
						}else if(c >= 'a' && c <= 'z'){
							c -= 32;
						}
						chars[i] = c;
					}
					String value2 = new String(chars);
					id = new String(Base64.getDecoder().decode(value2.substring(0, value2.length() - 1)));
					userExample = new UserExample();
					userExample.createCriteria().andIdEqualTo(id).andEnabledEqualTo(true);
					if(userSession.getMapper().selectByExample(userExample).isEmpty()) {
						errorMsg = "该职工不存在或已离职";
						throw new Exception();
					}
				}
			} catch (IOException e) {
				errorMsg = "查询数据库失败，请检查网络";
				throw e;
			} catch (IllegalArgumentException e) {
				errorMsg = "工号编码格式错误";
				throw e;
			}catch (Exception e) {
				throw e;
			}
			mainController.setUserId(id);
			return null;
		}
		
		@Override
		protected void succeeded() {
			stage.close();
			mainController.print();
		}
		
		@Override
		protected void failed() {
			error(errorMsg);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idTf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//判断等号
				if(newValue.endsWith("?")) {
					new Thread(new QueryTask(newValue)).start();
				}
			}
		});
	}


	public void error(String message) {
		new Thread(()->{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Platform.runLater(()->{
				tipLb.setTextFill(Color.RED);
				tipLb.setText(message);
				logger.error(message);
			});
		}).start();
	}


	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
