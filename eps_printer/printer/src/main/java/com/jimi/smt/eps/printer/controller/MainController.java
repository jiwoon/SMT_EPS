package com.jimi.smt.eps.printer.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.jimi.smt.eps.printer.entity.Material;
import com.jimi.smt.eps.printer.entity.MaterialProperties;
import com.jimi.smt.eps.printer.entity.StockLog;
import com.jimi.smt.eps.printer.mapper.StockLogMapper;
import com.jimi.smt.eps.printer.util.ExcelHelper;
import com.jimi.smt.eps.printer.util.MybatisHelper;
import com.jimi.smt.eps.printer.util.MybatisHelper.MybatisSession;

import cc.darhao.dautils.api.DateUtil;
import cc.darhao.dautils.api.ResourcesUtil;
import cc.darhao.dautils.api.TextFileUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * 主页控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class MainController implements Initializable {

	/**
	 * 配置文件名
	 */
	private static final String CONFIG_FILE_NAME = "printer.cfg";
	
	/**
	 * 配置：上一次料号表路径
	 */
	private static final String CONFIG_KEY_FILE_PATH = "filePath";
	
	/**
	 * 配置：所选料号表Sheet名
	 */
	private static final String CONFIG_KEY_SHEET_NAME = "sheetName";
	
	/**
	 * 配置：打印目标（二维码 or RFID or 两者）
	 */
	private static final String CONFIG_KEY_PRINT_TARGET = "printTarget";
	
	/**
	 * Mybatis的配置文件路径
	 */
	private static final String MYBATIS_CONFIG_PATH = "mybatis/mybatis-config.xml";
	
	private Logger logger = LogManager.getRootLogger();
	
	@FXML
	private AnchorPane parentAp;
	@FXML
	private TextField fileSelectTf;
	@FXML
	private TextField materialNoTf;
	@FXML
	private TextField descriptionTf;
	@FXML
	private TextField nameTf;
	@FXML
	private TextField quantityTf;
	@FXML
	private TextField seatNoTf;
	@FXML
	private TextField remarkTf;
	@FXML
	private Button fileSelectBt;
	@FXML
	private Button printBt;
	@FXML
	private Label stateLb;
	@FXML
	private Label materialNoLb;
	@FXML
	private Label descriptionLb;
	@FXML
	private Label nameLb;
	@FXML
	private Label quantityLb;
	@FXML
	private Label seatLb;
	@FXML
	private Label timeLb;
	@FXML
	private Label remarkLb;
	@FXML
	private ImageView codeIv;
	@FXML
	private AnchorPane previewAp;
	@FXML
	private Label materialNoLb1;
	@FXML
	private Label descriptionLb1;
	@FXML
	private Label nameLb1;
	@FXML
	private Label quantityLb1;
	@FXML
	private Label seatLb1;
	@FXML
	private Label timeLb1;
	@FXML
	private Label remarkLb1;
	@FXML
	private ImageView codeIv1;
	@FXML
	private AnchorPane previewAp1;
	@FXML
	private ChoiceBox tableSelectCb;
	@FXML
	private TableView materialTb;
	@FXML
	private TableColumn materialNoCol;
	@FXML
	private TableColumn nameCol;
	@FXML
	private TableColumn descriptionCol;
	@FXML
	private TableColumn seatNoCol;
	@FXML
	private TableColumn quantityCol;
	@FXML
	private TableColumn remarkCol;
	@FXML
	private Button configBt;
	@FXML
	private TextField copyTf;
	@FXML
	private CheckBox ignoreCb;
	@FXML
	private RadioButton bothRb;
	@FXML
	private RadioButton codeRb;
	@FXML
	private RadioButton rfidRb;
	
	private Stage primaryStage;
	
	private ExcelHelper excel;
	
	private Properties properties;
	
	//打印者id
	private String userId;
	
	/**
	 * 二维码和RFID内的数据<br>
	 * 格式：料号@数量@时间戳@工号
	 */
	private static String data;
	
	//Table的数据源
	private List<Material> materials;
	
	//打印标签纸控制socket
	private Socket printerSocket;
	
	//写入RFID标签控制socket
	private static Socket rfidSocket;
	
	//打印任务的份数
	private int copies;

	//RFID的COM口号
	private int port;
	
	//一次完整任务的打印结果
	public static boolean rfidResult, codeResult;
	
	//流水号
	private static int serialNo = 0; 
	
	//Socket_IP
	private final static String ip = "localhost";
	
	//入库数据连接
	private MybatisSession<StockLogMapper> session;
	
	//入库数据消息队列
	private List<String> stockLogList = new ArrayList<String>();
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		initTableCol();
		initMaterialNoTfListener();
		initTableSelectorCbListener();
		initMaterialPropertiesTfsListener();
		initHotKey();
		initDataFromConfig();
		initPrinter();
		initRFID();
	}


	public void onFileSelectBtClick() {
		//初始化文件选择器
		FileChooser chooser = new FileChooser();
		chooser.setTitle("选择供应商料号表文件");
		chooser.getExtensionFilters().add(new ExtensionFilter("供应商料号表文件", "*.xls" , "*.xlsx"));
		//尝试读取配置文件获取上次默认路径
		String filePath = new String();
		File materialFile = null;
		try {
			filePath = properties.getProperty(CONFIG_KEY_FILE_PATH);
			if(filePath != null && !filePath.equals("")) {
				File file = new File(filePath);
				if(file.getParentFile().exists()) {
					chooser.setInitialDirectory(file.getParentFile());
				}
			}
		} catch (NullPointerException e) {
			try {
				new File(CONFIG_FILE_NAME).createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				error("创建配置文件时出现IO错误");
			}
		}
		//选择文件
		materialFile = chooser.showOpenDialog(primaryStage);
		if(materialFile != null) {
			//初始化Excel
			fileSelectTf.setText(materialFile.getAbsolutePath());
			try {
				excel = ExcelHelper.from(materialFile);
			} catch (IOException e1) {
				e1.printStackTrace();
				error("加载供应商料号表出现IO错误");
			}
			//加载表选择器数据
			loadTableSelectorData();
			//设置当前sheet
			excel.switchSheet(0);
			//设置当前下拉选项
			tableSelectCb.getSelectionModel().select(0);
			//设置料号为空
			materialNoTf.setText("");
			//存配置
			properties.setProperty(CONFIG_KEY_FILE_PATH, materialFile.getAbsolutePath());
			try {
				properties.store(new FileOutputStream(new File(CONFIG_FILE_NAME)), null);
			} catch (IOException e) {
				e.printStackTrace();
				error("保存配置文件时出现IO错误");
			}
		}
	}
		
	
	public void onPrintBtClick() {
		//信息完整性校验
		String quantity = quantityTf.getText();
		String seat = seatNoTf.getText();
		if(quantity == null || quantity.equals("") || seat == null || seat.equals("")) {
			error("请填写数量和位置信息");
			return;
		}
		//数字类型校验
		String copy = copyTf.getText();
		try{
			int quantityInt= Integer.parseInt(quantity);
			int copyInt = Integer.parseInt(copy);
			if(copyInt < 1 || quantityInt < 1) {
				throw new NumberFormatException();
			}
			copies = copyInt;
			showIdWindow();
		}catch (NumberFormatException e) {
			error("请输入正整数");
		}
	}


	public void onCallConfig() {
		showConfigWindow();
	}

	
	public void onIgnoreClick() {
		if(ignoreCb.isSelected()) {
			nameTf.setDisable(false);
			descriptionTf.setDisable(false);
			seatNoTf.setDisable(false);
			quantityTf.setDisable(false);
			remarkTf.setDisable(true);
			remarkTf.setText("未校验");
			remarkLb.setText("未校验");
			printBt.setDisable(false);
			info("已开启忽略校验模式，请确保正确输入料号，并且尽早完善料号表格式并退出该模式");
		}else {
			resetControllers();
		}
	}
	
	
	/**
	 * 打印任务
	 */
	public void print() {
		rfidResult = codeResult = true;
		//准备操作
		printBt.setDisable(true);
		printBt.setText("打印中...");
		info("打印中...");
		try {
	    	for (int i = 0; i < copies; i++) {
				subPrint();
				if(!rfidResult) {
					throw new Exception("未成功写入RFID数据！");
				}else if(!codeResult){
					throw new Exception("未成功打印二维码标签！");
				}
				info("打印成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			stockLogList.remove(stockLogList.size());
			error("发生错误：" + e.getMessage());
		}
    	materialNoTf.setText("");
    	materialNoTf.requestFocus();
    	printBt.setText("打印");
    	//提交数据库
    	commitDataBase();
	}


	/**
	 * 打印子任务，此方法会被print调用若干次，次数为份数
	 * @throws Exception
	 */
	public void subPrint() throws Exception{
		//生成时间
		timeLb.setText(DateUtil.yyyyMMddHHmmss(new Date()));
		//生成数据
		createData();
		//记录入库日志
		try {
			stockLogList.add(data);
			//判断打印目标
			if(codeRb.isSelected()) {
				//生成二维码
				createCode();
				//生成截图
				createImage();
				//发送打印指令
			    callPrinter();
			}else if(rfidRb.isSelected()){
				//弹出RFID对话框
				showRfidAlert();
			}else if(bothRb.isSelected()) {
				//弹出RFID对话框
				showRfidAlert();
				//生成二维码
				createCode();
				//生成截图
				createImage();
				//发送打印指令
			    callPrinter();
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * 调用打印机
	 */
	private void callPrinter() {
		codeResult = false;
		try {
			if(printerSocket == null || !printerSocket.isConnected()) {
				throw new IOException();
			}
			printerSocket.getOutputStream().write(1);
			if(printerSocket.getInputStream().read() == 1) {
	    		codeResult = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 生成数据
	 */
	private void createData() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(materialNoLb.getText().trim());
		stringBuffer.append("@");
		stringBuffer.append(quantityLb.getText().trim().equals("") ? "0" : quantityLb.getText().trim());
		stringBuffer.append("@");
		stringBuffer.append(System.currentTimeMillis());
		stringBuffer.append("@");
		stringBuffer.append(userId);
		stringBuffer.append("@");
		stringBuffer.append(tableSelectCb.getSelectionModel().getSelectedItem().toString().trim());
		stringBuffer.append("@");
		stringBuffer.append(seatLb.getText().trim());
		stringBuffer.append("@");
		stringBuffer.append(serialNo++);
		stringBuffer.append("@");
		data = stringBuffer.toString();
	}


	/**
	 * 记录入库到数据库
	 * @param data
	 * @throws Exception
	 */
	private void insertStockLog(String data) throws Exception {
		String[] datas = data.split("@");
		StockLog log = new StockLog();
		log.setMaterialNo(datas[0] == null ? "" : datas[0]);
		log.setQuantity(Integer.valueOf(datas[1]));
		log.setTimestamp(datas[2] == null ? "" : datas[2]);
		log.setOperator(datas[3] == null ? "" : datas[3]);
		log.setCustom(datas[4] == null ? "" : datas[4]);
		log.setPosition(datas[5] == null ? "" : datas[5]);
		log.setOperationTime(new Date());
		session.getMapper().insert(log);
	}


	/**
	 * 尝试提交入库记录，若失败将会记录到文件，将在下次调用时一并提交
	 */
	private void commitDataBase() {
		//插入数据库
		new Thread(() -> {
			File file = new File("stock_log_backup.dat");
			try {
				//从硬盘读取备份文件拼接到队列
				if(file.exists()) {
					String[] datas = TextFileUtil.readFromFile("stock_log_backup.dat").split("\\|");
	    			for (String string : datas) {
						stockLogList.add(string);
					}
				}
				//插入库
				session = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, StockLogMapper.class);
				for (String data : stockLogList) {
					insertStockLog(data);
				}
				//提交并双清
				session.commit();
				stockLogList.clear();
				file.delete();
			} catch (Exception e) {
				//队列记录备份硬盘文件并清除队列
				if(!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				StringBuffer sb = new StringBuffer();
				for (String data : stockLogList) {
					sb.append(data);
					sb.append("|");
				}
				sb.deleteCharAt(sb.length() - 1);
				try {
					TextFileUtil.writeToFile("stock_log_backup.dat", sb.toString());
					stockLogList.clear();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}


	private void createCode() {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
        try {
        	//生成矩阵  
        	BitMatrix bitMatrix = new MultiFormatWriter().encode(data,BarcodeFormat.QR_CODE, 260, 260, hints);
            //输出并显示图像
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "gif", byteArrayOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            codeIv.setImage(SwingFXUtils.toFXImage(ImageIO.read(byteArrayInputStream), null));
        }catch (IOException e) {
			error("输出二维码图像时出错");
			e.printStackTrace();
		} catch (WriterException e) {
			error("生成二维码时出错");
			e.printStackTrace();
		}
        
	}


	private void createImage() {
		//拷贝到大图层
		materialNoLb1.setText(materialNoLb.getText());
		nameLb1.setText(nameLb.getText());
		descriptionLb1.setText(descriptionLb.getText());
		seatLb1.setText(seatLb.getText());
		quantityLb1.setText(quantityLb.getText());
		remarkLb1.setText(userId + " / " + remarkLb.getText());
		timeLb1.setText(timeLb.getText());
		codeIv1.setImage(codeIv.getImage());
		previewAp1.setVisible(true);
	    Image image = previewAp1.snapshot(null, null);
	    try {
	    	//根据分辨率设置尺寸
	    	int resolution = Integer.parseInt(TextFileUtil.readFromFile("e.cfg").split(",")[2]);
	    	BufferedImage bi = SwingFXUtils.fromFXImage(image, null);
	    	if(resolution != 300) {
	    		int width = (int)(bi.getWidth() / 300.0 * resolution);
	    		int height = (int)(bi.getHeight() / 300.0 * resolution);
		    	BufferedImage newBi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		    	newBi.getGraphics().drawImage(bi, 0, 0, width, height ,null);
		    	bi = newBi;
	    	}
	        if(!ImageIO.write(bi, "gif", new File("Picture.gif"))) {
	        	throw new IOException();
	        }
	    } catch (IOException e) {
	    	error("生成图片时出错");
	        e.printStackTrace();
	    }finally {
	    	previewAp1.setVisible(false);
	    }
	}
	
	
	/**
	 * 加载表选择器数据
	 */
	private void loadTableSelectorData() {
		ObservableList<String> list = FXCollections.observableArrayList();
		for (int i = 0; i <  excel.getBook().getNumberOfSheets(); i++) {
			String name = excel.getBook().getSheetAt(i).getSheetName();
			list.add(name);
		}
		tableSelectCb.setItems(list);
		info("数据解析成功，请在上方选择表");
	}


	/**
	 * 加载表数据
	 */
	private void loadTableData() {
		try {
			materials = excel.unfill(Material.class, 1);
		} catch (Exception e) {
			error("数据解析失败，请参考\"标准范例表\"编写供应商料号表文件");
			materialTb.setItems(null);
			return;
		}
		ObservableList<MaterialProperties> materialPropertiesList = FXCollections.observableArrayList();
		for (Material material : materials) {
			MaterialProperties materialProperties = new MaterialProperties(material);
			materialPropertiesList.add(materialProperties);
		}
		materialTb.setItems(materialPropertiesList);
		info("数据解析成功，请在右上方扫入或输入料号");
		materialNoTf.requestFocus();
	}

	private void initDataFromConfig() {
		//检查e.cfg存在与否，不存在则重新创建
		if(!new File("e.cfg").exists()) {
			try {
				TextFileUtil.writeToFile("e.cfg", "0,0,300");
			} catch (IOException e1) {
				logger.error("e.cfg文件创建失败");
			}
		}
		//读取上次文件路径和表名
		properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(CONFIG_FILE_NAME)));
		} catch (FileNotFoundException | NullPointerException e) {
			try {
				new File(CONFIG_FILE_NAME).createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				error("创建配置文件时出现IO错误");
			}
		} catch (IOException e) {
			e.printStackTrace();
			error("读取配置文件时出现IO错误");
		}
		String filePath = properties.getProperty(CONFIG_KEY_FILE_PATH);
		String sheetName = properties.getProperty(CONFIG_KEY_SHEET_NAME);
		info("请选择供应商料号表文件");
		
		//如果没有配置则不做什么
		if(filePath == null || filePath.equals("")) {
			return;
		}
		//读取文件
		File materialFile = new File(filePath);
		try {
			excel = ExcelHelper.from(materialFile);
			
			//设置当前文件名
			fileSelectTf.setText(filePath);
			info("文件加载成功");
			
			//加载表选择下拉菜单数据
			loadTableSelectorData();
			
			//切换到指定sheet
			if(sheetName == null || sheetName.equals("")) {
				return;
			}
			if(!excel.switchSheet(sheetName)) {
				error("表\""+ sheetName +"\"不存在");
				return;
			}
			//设置当前下拉选项
			tableSelectCb.getSelectionModel().select(excel.getBook().getSheetIndex(sheetName));
			
			//加载表数据
			loadTableData();
		} catch (IOException e) {
			e.printStackTrace();
			error("供应商料号表文件\""+ materialFile.getName() +"\"不存在");
		}
		//初始化时间
		timeLb.setText(DateUtil.yyyyMMddHHmmss(new Date()));
	}


	private void initHotKey() {
		//初始化打印热键
		parentAp.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().compareTo(KeyCode.ENTER) == 0){
					//控制焦点转移或打印
					if(materialNoTf.isFocused()) {
						quantityTf.requestFocus();
					}else if(quantityTf.isFocused()){
						if(!printBt.isDisable()) {
							onPrintBtClick();
						}
					}
				}
			}
		});
//		ChangeListener<String> listener = new ChangeListener<String>() {
//			
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				if(newValue.equals("")) {
//					return;
//				}
//				char c = newValue.charAt(newValue.length() - 1);
//				if(c == '\n' || c == '\r') {
//					//控制焦点转移或打印
//					if(materialNoTf.isFocused()) {
//						quantityTf.requestFocus();
//					}else if(quantityTf.isFocused()){
//						if(!printBt.isDisable()) {
//							onPrintBtClick();
//						}
//					}
//				}
//			}
//		};
//		materialNoTf.textProperty().addListener(listener);
//		quantityTf.textProperty().addListener(listener);
	}


	private void initPrinter() {
		new Thread(()-> {
			//初始化打印机
			try {
				if(!new File("printer.exe").exists()) {
					throw new IOException();
				}
				Runtime.getRuntime().exec("printer.exe");
				printerSocket = new Socket();
				printerSocket.setSoTimeout(1000);
				printerSocket.connect(new InetSocketAddress(ip, 10101), 3000);
			} catch (IOException e) {
				Platform.runLater(()->{
					error("启动打印机程序失败，请检查打印机是否在工作并重启程序");
				});
				e.printStackTrace();
			}
		}).start();
	}


	private void initRFID() {
		new Thread(()-> {
			//初始化RFID
			try {
				if(!new File("SMT_EPS_RFID_WRITER.exe").exists()) {
					throw new IOException();
				}
				Runtime.getRuntime().exec("SMT_EPS_RFID_WRITER.exe");
				rfidSocket = new Socket();
				rfidSocket.connect(new InetSocketAddress(ip, 10102), 3000);
				//获取初始化结果
				rfidSocket.setSoTimeout(30000);
				int result = rfidSocket.getInputStream().read();
				if(result == '0') {
					//更改COM号再重启RFID程序
					if(port == 31) {
						throw new IOException("已尝试0~31的端口，依然无法找到RFID设备");
					}
					port++;
					try {
						TextFileUtil.writeToFile("RFIDcomm.cfg", "Port=" + port);
						initRFID();
					} catch (IOException e) {
						Platform.runLater(()->{
							error("启动RFID程序失败，缺少配置文件RFIDComm.cfg");
							initPrintTargetRbs();
							codeRb.setSelected(true);
						});
						e.printStackTrace();
					}
				}else {
					Platform.runLater(()->{
						info("RFID程序加载完毕");
						rfidRb.setDisable(false);
						bothRb.setDisable(false);
						initPrintTargetRbs();
					});
				}
			} catch (IOException e) {
				Platform.runLater(()->{
					error("启动RFID程序失败，请检查RFID读写器是否在工作并重启程序");
					initPrintTargetRbs();
					codeRb.setSelected(true);
				});
				e.printStackTrace();
			}
		}).start();
		if(Thread.currentThread().getName().equals("JavaFX Application Thread")) {
			info("加载RFID程序中...");
		}
	}


	private void initPrintTargetRbs() {
		ChangeListener listener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				//保存到配置文件中
				String value = "0";
				if(codeRb.isSelected()) {
					value = "0";
				}else if(rfidRb.isSelected()){
					value = "1";
				}else if(bothRb.isSelected()) {
					value = "2";
				}
				//存配置
				properties.setProperty(CONFIG_KEY_PRINT_TARGET, value);
				try {
					properties.store(new FileOutputStream(new File(CONFIG_FILE_NAME)), null);
				} catch (IOException e) {
					e.printStackTrace();
					error("保存配置文件时出现IO错误");
				}
			}
			
		};
		codeRb.selectedProperty().addListener(listener);
		rfidRb.selectedProperty().addListener(listener);
		bothRb.selectedProperty().addListener(listener);
		//读配置
		String value = properties.getProperty(CONFIG_KEY_PRINT_TARGET, "0");
		switch (value) {
		case "0":
			codeRb.setSelected(true);
			break;
		case "1":
			rfidRb.setSelected(true);
			break;
		case "2":
			bothRb.setSelected(true);
			break;
		default:
			codeRb.setSelected(true);
			break;
		}
	}


	private void initTableCol() {
		//初始化表格列表
		materialNoCol.setCellValueFactory(new PropertyValueFactory<>("no"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		seatNoCol.setCellValueFactory(new PropertyValueFactory<>("seat"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		remarkCol.setCellValueFactory(new PropertyValueFactory<>("remark"));
	}


	private void initTableSelectorCbListener() {
		//初始化下拉框监听器
		tableSelectCb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(newValue.intValue() == -1) {
					//存配置
					properties.setProperty(CONFIG_KEY_SHEET_NAME, "");
				}else {
					//切换到指定sheet
					if(!excel.switchSheet(newValue.intValue())) {
						error("表切换失败");
						return;
					}
					//更新数据
					loadTableData();
					//存配置
					properties.setProperty(CONFIG_KEY_SHEET_NAME, excel.getBook().getSheetName(newValue.intValue()));
					//设置料号为空
					materialNoTf.setText("");
				}
				try {
					properties.store(new FileOutputStream(new File(CONFIG_FILE_NAME)), null);
				} catch (IOException e) {
					e.printStackTrace();
					error("配置文件时出现IO错误");
				}
			}
		});
	}


	private void initMaterialNoTfListener() {
		//初始化物料编号文本域监听器
		materialNoTf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(materials == null) {
					printBt.setDisable(true);
					return;
				}
				if(ignoreCb.isSelected()) {
					materialNoLb.setText(materialNoTf.getText());
					printBt.setDisable(false);
					return;
				}
				for (Material material : materials) {
					//如果存在该料号则显示属性，并且允许更改和打印，并且把料号显示为绿色，否则不显示属性，禁止更改和打印，且料号为黑色
					if(newValue != null && !newValue.equals("") && material.getNo().toUpperCase().equals(newValue.toUpperCase())) {
						nameTf.setDisable(false);
						descriptionTf.setDisable(false);
						seatNoTf.setDisable(false);
						quantityTf.setDisable(false);
						remarkTf.setDisable(false);
						
						nameTf.setText(material.getName());
						descriptionTf.setText(material.getDescription());
						seatNoTf.setText(material.getSeat());
						quantityTf.setText(material.getQuantity());
						remarkTf.setText(material.getRemark());
						
						materialNoLb.setText(material.getNo());
						nameLb.setText(material.getName());
						descriptionLb.setText(material.getDescription());
						seatLb.setText(material.getSeat());
						quantityLb.setText(material.getQuantity());
						remarkLb.setText(material.getRemark());
						
						materialNoTf.setStyle("-fx-text-fill: green;");
						
						printBt.setDisable(false);
						
						//焦点移至数量输入框
						quantityTf.requestFocus();
						
						info("料号存在，打印已就绪（热键：回车）");
						break;
					}
					resetControllers();
					
					materialNoTf.setStyle("-fx-text-fill: black;");
					
//					info("请确认输入的料号是否存在");
				}
			}

		});
	}
	

	private void initMaterialPropertiesTfsListener() {
		nameTf.textProperty().addListener(new MaterialPropertiesTfChangeListener("name"));
		descriptionTf.textProperty().addListener(new MaterialPropertiesTfChangeListener("description"));
		seatNoTf.textProperty().addListener(new MaterialPropertiesTfChangeListener("seat"));
		quantityTf.textProperty().addListener(new MaterialPropertiesTfChangeListener("quantity"));
		remarkTf.textProperty().addListener(new MaterialPropertiesTfChangeListener("remark"));
	}

	
	/**
	 * 显示正常状态
	 * @param message
	 */
	private void info(String message) {
		stateLb.setTextFill(Color.BLACK);
		stateLb.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		stateLb.setText(DateUtil.HHmmss(new Date()) +" - "+ message);
	}


	/**
	 * 显示错误状态，并记录日志
	 * @param message
	 */
	private void error(String message) {
		stateLb.setTextFill(Color.WHITE);
		stateLb.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		stateLb.setText(DateUtil.HHmmss(new Date()) +" - "+ message);
		logger.error(message);
	}


	class MaterialPropertiesTfChangeListener implements ChangeListener<String>{
	
		private String materialPropertyName;
		
		public MaterialPropertiesTfChangeListener(String materialPropertyName) {
			this.materialPropertyName = materialPropertyName;
		}
		
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			switch (materialPropertyName) {
			case "name":
				nameLb.setText(nameTf.getText());
				break;
			case "description":
				descriptionLb.setText(descriptionTf.getText());
				break;
			case "seat":
				seatLb.setText(seatNoTf.getText());
				break;
			case "quantity":
				quantityLb.setText(quantityTf.getText());
				break;
			case "remark":
				remarkLb.setText(remarkTf.getText());
				break;
			default:
				break;
			}
		}
		
	}

	
	private void showConfigWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(ResourcesUtil.getResourceURL("fxml/config.fxml"));
			Parent root = loader.load();
			ConfigController configController = loader.getController();
	        //显示
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			configController.setStage(stage);
			stage.setScene(new Scene(root));
			stage.show();
		}catch (IOException e) {
			e.printStackTrace();
			error("加载窗口时出错");
		}
		
	}
	
	
	private void showIdWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(ResourcesUtil.getResourceURL("fxml/id.fxml"));
			Parent root = loader.load();
			IdController idController = loader.getController();
	        //显示
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			idController.setMainController(MainController.this);
			idController.setStage(stage);
			stage.setScene(new Scene(root));
			stage.show();
		}catch (IOException e) {
			e.printStackTrace();
			error("加载窗口时出错");
		}
		
	}
	
	
	private void showRfidAlert() {
		try {
			FXMLLoader loader = new FXMLLoader(ResourcesUtil.getResourceURL("fxml/rfid.fxml"));
			Parent root = loader.load();
			RfidController rfidController = loader.getController();
			 //显示
			Alert alert = new Alert(AlertType.NONE);
			alert.setTitle("写入数据");
			alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			alert.initOwner(primaryStage);
			alert.setGraphic(root);
			rfidController.setAlert(alert);
			alert.showAndWait();
		}catch (IOException e) {
			e.printStackTrace();
			error("加载窗口时出错");
		}
		
	}
	
	
	private void resetControllers() {
		nameTf.setDisable(true);
		descriptionTf.setDisable(true);
		seatNoTf.setDisable(true);
		quantityTf.setDisable(true);
		remarkTf.setDisable(true);
		
		nameTf.setText("");
		descriptionTf.setText("");
		seatNoTf.setText("");
		quantityTf.setText("");
		remarkTf.setText("");
		
		materialNoLb.setText("");
		nameLb.setText("");
		descriptionLb.setText("");
		seatLb.setText("");
		quantityLb.setText("");
		remarkLb.setText("");
		
		printBt.setDisable(true);
	}
	

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}


	public Socket getPrinterSocket() {
		return printerSocket;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 二维码和RFID内的数据<br>
	 * 格式：料号@数量@时间戳@工号
	 */
	public static String getData() {
		return data;
	}


	public static Socket getRfidSocket() {
		return rfidSocket;
	}

	
}
