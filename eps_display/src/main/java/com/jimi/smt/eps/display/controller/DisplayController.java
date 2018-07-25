package com.jimi.smt.eps.display.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jimi.smt.eps.display.app.Main;
import com.jimi.smt.eps.display.constant.BoardResetReson;
import com.jimi.smt.eps.display.constant.ClientDevice;
import com.jimi.smt.eps.display.constant.ControlResult;
import com.jimi.smt.eps.display.constant.Line;
import com.jimi.smt.eps.display.entity.Log;
import com.jimi.smt.eps.display.entity.Login;
import com.jimi.smt.eps.display.entity.Program;
import com.jimi.smt.eps.display.entity.ProgramItemVisit;
import com.jimi.smt.eps.display.entity.ResultData;
import com.jimi.smt.eps.display.mapper.LoginMapper;
import com.jimi.smt.eps.display.mapper.OperationMapper;
import com.jimi.smt.eps.display.mapper.ProgramItemVisitMapper;
import com.jimi.smt.eps.display.mapper.ProgramMapper;
import com.jimi.smt.eps.display.pack.BoardResetPackage;
import com.jimi.smt.eps.display.pack.BoardResetReplyPackage;
import com.jimi.smt.eps.display.util.HttpHelper;
import com.jimi.smt.eps.display.util.MybatisHelper;
import com.jimi.smt.eps.display.util.MybatisHelper.MybatisSession;

import cc.darhao.dautils.api.BytesParser;
import cc.darhao.dautils.api.FieldUtil;
import cc.darhao.jiminal.callback.OnConnectedListener;
import cc.darhao.jiminal.callback.OnReplyPackageArrivedListener;
import cc.darhao.jiminal.core.AsyncCommunicator;
import cc.darhao.jiminal.core.BasePackage;
import cc.darhao.jiminal.core.PackageParser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import okhttp3.Call;
import okhttp3.Response;

public class DisplayController implements Initializable {

	// Mybatis的配置文件路径
	private static final String MYBATIS_CONFIG_PATH = "mybatis/mybatis-config.xml";

	private static final String PACKAGE_PATH = "com.jimi.smt.eps.display.pack";
	// 重置请求
	private static final String RESET_ACTION = "reset";
	// 选择当前工单请求
	private static final String SWITCH_ACTION = "switch";
	// 刷新表格数据线程的启动延时时间
	private static final Integer TIME_DELAY = 0;
	// 刷线表格数据线程的启动间隔时间
	private static final Integer TIME_PERIOD = 5000;
	// 默认结果
	private static final Integer DEFAULT_RESULT = 2;
	
	private static final boolean IS_NETWORK = true;
	// 定时器是否更新数据
	private static boolean isUpdate = false;
	// 不需要填充
	@FXML
	private Label lineLb;
	@FXML
	private Label operatorNameLb;
	@FXML
	private Label workOrderLb;
	@FXML
	private Label boardTybeLb;
	@FXML
	private Label lineseatNameLb;
	@FXML
	private Label scanLineseatNameLb;
	@FXML
	private Label materialNoNameLb;
	@FXML
	private Label scanMaterialNoNameLb;
	/// 需填充下拉框
	@FXML
	private ComboBox<String> lineCb;
	@FXML
	private ComboBox<String> workOrderCb;
	@FXML
	private ComboBox<String> boardTybeCb;
	// 需填充标签label
	@FXML
	private Label operatorLb;
	@FXML
	private Label lineseatLb;
	@FXML
	private Label scanLineseatLb;
	@FXML
	private Label materialNoLb;
	@FXML
	private Label scanMaterialNoLb;
	@FXML
	private Label typeLb;
	@FXML
	private Label resultLb;
	@FXML
	private Label versionLb;
	@FXML
	private Button resetBt;

	@FXML
	private TableView<ResultData> DataTv;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn lineseatCl;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn storeIssueResultCl;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn feedResultCl;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn changeResultCl;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn checkResultCl;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn checkAllResultCl;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn firstCheckAllResultCl;

	// http连接
	HttpHelper httpHelper = new HttpHelper();
	// 数据库数据连接
	private MybatisSession<ProgramItemVisitMapper> programItemVisitSession;

	private MybatisSession<ProgramMapper> programSession;

	private MybatisSession<OperationMapper> operationSession;

	private MybatisSession<LoginMapper> loginSession;
	// 定时器
	private static Timer updateTimer = new Timer(true);
	// 表格数据
	private ObservableList<ResultData> tableLsit = null;
	// 日志记录
	private Logger logger = LogManager.getRootLogger();

	AsyncCommunicator asyncCommunicator = null;

	BoardResetPackage boardResetPackage = new BoardResetPackage();
	
	private List<String> lines = null; 

	public void initialize(URL arg0, ResourceBundle arg1) {
		initVersionLb();
		initSession();
		initlineCb();
		initDatatTV();
		initButton();
		lineCbChange();
		workOrderChange();
		boardTypeChange();
		// 定时任务：刷新表单
		updateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				String line = lineCb.getSelectionModel().getSelectedItem() == null ? ""
						: lineCb.getSelectionModel().getSelectedItem().toString();
				String workOrder = workOrderCb.getSelectionModel().getSelectedItem() == null ? ""
						: workOrderCb.getSelectionModel().getSelectedItem().toString();
				String boardType = boardTybeCb.getSelectionModel().getSelectedItem() == null ? ""
						: boardTybeCb.getSelectionModel().getSelectedItem().toString();
				if (!line.equals("") && !workOrder.equals("") && !boardType.equals("")) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							if (isUpdate) {
								updateText();
							}
						}
					});
				}
			}
		}, TIME_DELAY, TIME_PERIOD);
	}

	/**
	 * 初始化session，该类session可以使用缓存，表中数据不常变化
	 */
	private void initSession() {
		try {
			programSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, ProgramMapper.class, "smt_001");
			operationSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, OperationMapper.class, "smt_001");
			loginSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, LoginMapper.class, "smt_002");
		} catch (IOException e) {
			logger.error("数据库session创建失败");
			e.printStackTrace();
		}
	}

	private void initVersionLb() {
		versionLb.setText("V"+Main.getVersion()+" © 2018 几米物联技术有限公司  All rights reserved.");
	}
	/**
	 * 初始化线号选择框
	 */
	public void initlineCb() {

		lines = loginSession.getMapper().selectLine();
		ObservableList<String> lineList = FXCollections.observableArrayList(lines);
		lineCb.getItems().clear();
		lineCb.setItems(lineList);
	}

	/**
	 * 初始化表格
	 */ 
	@SuppressWarnings({ "unchecked" })
	public void initDatatTV() {

		lineseatCl.setCellValueFactory(new PropertyValueFactory<ResultData, String>("lineseat"));
		storeIssueResultCl.setCellValueFactory(new PropertyValueFactory<ResultData, Integer>("storeIssueResult"));
		feedResultCl.setCellValueFactory(new PropertyValueFactory<ResultData, Integer>("feedResult"));
		changeResultCl.setCellValueFactory(new PropertyValueFactory<ResultData, Integer>("changeResult"));
		checkResultCl.setCellValueFactory(new PropertyValueFactory<ResultData, Integer>("checkResult"));
		checkAllResultCl.setCellValueFactory(new PropertyValueFactory<ResultData, Integer>("checkAllResult"));
		firstCheckAllResultCl.setCellValueFactory(new PropertyValueFactory<ResultData, Integer>("firstCheckAllResult"));
		initcell(storeIssueResultCl);
		initcell(feedResultCl);
		initcell(changeResultCl);
		initcell(checkResultCl);
		initcell(checkAllResultCl);
		initcell(firstCheckAllResultCl);
	}

	/**
	 * 初始化表格中某一列的每个单元格
	 * @param tableColumn 列
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initcell(TableColumn tableColumn) {

		tableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
			public TableCell<ResultData, Integer> call(TableColumn pColumn) {
				return new TableCell<ResultData, Integer>() {

					@Override
					public void updateItem(Integer item, boolean empty) {

						super.updateItem(item, empty);
						this.setText(null);
						if (!empty) {
							if (item.toString().equals("0")) {
								this.setText("×");
								this.setTextFill(Color.RED);
							} else if (item.toString().equals("1")) {
								this.setText("●");
								this.setTextFill(Color.GREEN);
							} else if (item.toString().equals("2")) {
								this.setText("○");
								this.setTextFill(Color.BLUE);
							} else if (item.toString().equals("3")) {
								this.setText("×");
								this.setTextFill(Color.ORANGE);
							}
							if (item.toString().equals("4")) {
								this.setText("◎");
								this.setTextFill(Color.PURPLE);
							}
						}
					}
				};
			}
		});
	}

	/*
	 * 线号选择框文本内容变更监听器
	 */
	public void lineCbChange() {
		lineCb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null && newValue.intValue() >= 0) {
					String line = lineCb.getItems().get(newValue.intValue());
					Login login = null;
					try {
						resetWorkOrderCb(line);
						login = loginSession.getMapper().selectByLine(line);
					} catch (Exception e) {
						logger.error("数据库连接出错");
						e.printStackTrace();
					}
					String remoteIp = login.getIp();
					int port = login.getPort();
					// String remoteIp = "10.10.11.119";
					int port = 23334;
					System.out.println("IP:" + remoteIp + "  PORT:" + port);
					asyncCommunicator = new AsyncCommunicator(remoteIp, port, PACKAGE_PATH);
					boardResetPackage.setLine(Line.values()[newValue.intValue()+1]);
					boardResetPackage.setClientDevice(ClientDevice.PC);
					boardResetPackage.setBoardResetReson(BoardResetReson.WORK_ORDER_RESTART);
					System.out.println(boardResetPackage.getLine());
					clearText();
				}

			}
		});
	}

	/**
	 * 工单选择框文本内容变更监听器
	 * 
	 */
	public void workOrderChange() {

		workOrderCb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null && newValue.intValue() >= 0) {
					String line = lineCb.getSelectionModel().getSelectedItem() == null ? ""
							: lineCb.getSelectionModel().getSelectedItem().toString();
					String workOrder = workOrderCb.getItems().get(newValue.intValue());
					if (!line.equals("") && !workOrder.equals("")) {
						Program program = new Program();
						program.setLine(line);
						program.setWorkOrder(workOrder);
						List<String> boardTypes = programSession.getMapper().SelectByWorkOrderAndLine(program);
						ObservableList<String> boardTybeList = FXCollections
								.observableArrayList(initBoardType(boardTypes));
						boardTybeCb.getItems().clear();
						boardTybeCb.setItems(boardTybeList);
						clearText();
					}
				}

			}
		});

	}



	// 板面类型选择框文本内容变更监听器
	public void boardTypeChange() {
		boardTybeCb.getSelectionModel();
		boardTybeCb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null && newValue.intValue() >= 0) {
					String line = lineCb.getSelectionModel().getSelectedItem() == null ? "" : lineCb.getSelectionModel().getSelectedItem().toString();
					String workOrder = workOrderCb.getSelectionModel().getSelectedItem() == null ? "" : workOrderCb.getSelectionModel().getSelectedItem().toString();
					String boardType = boardTybeCb.getItems().get(newValue.intValue());
					if (!line.equals("") && !workOrder.equals("") && boardType != null && !boardType.equals("")) {
						Integer boardTypeNo = getBoardTypeNo(boardType);
						Map<String, String> map = new HashMap<>();
						map.put("line", line);
						map.put("workOrder", workOrder);
						map.put("boardType", boardTypeNo.toString());
						setDisableCb(true);
						resetBt.setDisable(true);
						isUpdate = false;
						if (asyncCommunicator != null) {
							asyncCommunicator.connect(new OnConnectedListener() {
								//发送工单生产数量重置包
								@Override
								public void onSucceed() {
									asyncCommunicator.send(boardResetPackage, new OnReplyPackageArrivedListener() {

										@Override
										public void onReplyPackageArrived(BasePackage r) {
											Log sLog = createLogByPackage(boardResetPackage);
											logger.info("发送重置包：" + sLog.getData());
											if (r != null && r instanceof BoardResetReplyPackage) {
												BoardResetReplyPackage reply = (BoardResetReplyPackage) r;
												Log rLog = createLogByPackage(reply);
												logger.info("接收重置包：" + rLog.getData());
												if (reply.getControlResult().equals(ControlResult.SUCCEED)) {
													try {
														//发送选择工单请求
														httpHelper.requestHttp(SWITCH_ACTION, map, new okhttp3.Callback() {

															@Override
															public void onResponse(Call call, Response response)
																	throws IOException {
																if (response.body().string()
																		.equals("{\"result\":\"succeed\"}")) {
																	Platform.runLater(new Runnable() {
																		@Override
																		public void run() {
																			isUpdate = true;
																			setDisableCb(false);
																			resetBt.setDisable(false);
																			updateText();
																		}
																	});
																} else {
																	httpFail(SWITCH_ACTION, !IS_NETWORK, line);
																}
															}

															@Override
															public void onFailure(Call call, IOException e) {
																httpFail(SWITCH_ACTION, IS_NETWORK, line);
																e.printStackTrace();
															}
														});

													} catch (IOException e) {
														httpFail(SWITCH_ACTION, IS_NETWORK, line);
														e.printStackTrace();
													}
												}else {
													resetProductNbFail(!IS_NETWORK,line);
												}
											}else {
												resetProductNbFail(!IS_NETWORK,line);
											}
										}

										@Override
										public void onCatchIOException(IOException e) {
											resetProductNbFail(IS_NETWORK,line);
											e.printStackTrace();
										}
									});
								}
								
								@Override
								public void onFailed(IOException e) {
									resetProductNbFail(IS_NETWORK,line);
									if (asyncCommunicator != null) {
										asyncCommunicator.close();
									}
									e.printStackTrace();
								
								}
							});
							
						}
					}
				}
			}
		});

	}

	/**
	 * 重置按钮监听器
	 */
	public void initButton() {
		resetBt.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String boardType = boardTybeCb.getSelectionModel().getSelectedItem() == null ? ""
						: boardTybeCb.getSelectionModel().getSelectedItem().toString();
				if (boardType.equals("") || !isUpdate) {
					new Alert(AlertType.INFORMATION, "你还没有选定工单信息", ButtonType.OK).show();
				} else {
					Optional<ButtonType> optional = new Alert(AlertType.WARNING, "你确定要重置该工单的状态吗？\n这会初始化除发料以外的所有状态",
							ButtonType.YES, ButtonType.CANCEL).showAndWait();
					if (optional != null && optional.get().equals(ButtonType.YES)) {
						try {
							Map<String, String> map = new HashMap<>();
							String line = lineCb.getSelectionModel().getSelectedItem().toString();
							String workOrder = workOrderCb.getSelectionModel().getSelectedItem().toString();
							Integer boardTypeNo = getBoardTypeNo(boardType);
							map.put("line", line);
							map.put("workOrder", workOrder);
							map.put("boardType", boardTypeNo.toString());
							setDisableCb(true);
							resetBt.setDisable(true);
							httpHelper.requestHttp(RESET_ACTION, map, new okhttp3.Callback() {

								@Override
								public void onResponse(Call call, Response response) throws IOException {
									if (response != null
											&& response.body().string().equals("{\"result\":\"succeed\"}")) {
										Platform.runLater(new Runnable() {
											@Override
											public void run() {
												setDisableCb(false);
												resetBt.setDisable(false);
												updateText();

											}
										});
									} else {
										httpFail(RESET_ACTION, !IS_NETWORK, null);
									}
								}

								@Override
								public void onFailure(Call call, IOException e) {

									httpFail(RESET_ACTION, IS_NETWORK, null);
									e.printStackTrace();
								}
							});

						} catch (Exception e) {
							httpFail(RESET_ACTION, IS_NETWORK, null);
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	

	/**
	 * 重置工单生产数量失败
	 * @param network 是否是网络原因
	 * @param line 重置失败的线号
	 */
	private void resetProductNbFail(boolean network,String line) {
		if (network) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					logger.error("重置工单生产数目失败，IP地址：" + asyncCommunicator.getRemoteIp()
							+ "connect失败，网络连接出错");
					new Alert(AlertType.ERROR, "重置工单失败，请检查你的网络连接", ButtonType.OK).show();
					setDisableCb(false);
					resetBt.setDisable(false);
					resetWorkOrderCb(line);
				}
			});
		}else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					logger.error("重置工单生产数目失败，服务器原因");
					new Alert(AlertType.ERROR, "重置工单生产数目失败，服务器原因", ButtonType.OK).show();
					setDisableCb(false);
					resetBt.setDisable(false);
					resetWorkOrderCb(line);
				}
			});
		}
	}
	
	/**
	 * 发送http请求失败
	 * @param action http请求类型，switch还是reset
	 * @param isNetwork 是否是网络问题
	 * @param line 线号
	 */
	private void httpFail(String action,boolean isNetwork, String line) {
		if (action.equals(SWITCH_ACTION)) {
			if (isNetwork) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						logger.error("选定工单失败，switch请求失败，网络连接出错");
						new Alert(AlertType.ERROR, "选定工单失败，请检查你的网络连接", ButtonType.OK).show();
						setDisableCb(false);
						resetBt.setDisable(false);
						resetWorkOrderCb(line);
					}
				});
			}else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						logger.error("选定工单失败，服务器内部错误");
						new Alert(AlertType.ERROR, "选定工单失败，服务器内部错误", ButtonType.OK).show();
						setDisableCb(false);
						resetBt.setDisable(false);
						resetWorkOrderCb(line);
					}
				});
			}
		}else if (action.equals(RESET_ACTION)) {
			if (isNetwork) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						logger.error("重置工单失败，reset请求失败，网络连接出错");
						new Alert(AlertType.ERROR, "重置工单失败，请检查你的网络连接", ButtonType.OK).show();
						setDisableCb(false);
						resetBt.setDisable(false);
					}
				});
			}else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						logger.error("重置工单失败，服务器内部原因");
						new Alert(AlertType.ERROR, "重置工单失败，服务器内部错误", ButtonType.OK).show();
						setDisableCb(false);
						resetBt.setDisable(false);
					}
				});
			}
		}
	}
	

	
	private void setDisableCb(boolean status) {
		lineCb.setDisable(status);
		workOrderCb.setDisable(status);
		boardTybeCb.setDisable(status);
	}
	/**
	 * 更新站位，扫描站位，物料号，扫描物料号，操作员，操作结果以及表格数据
	 * 
	 * @param program
	 * @throws IOException
	 */
	private synchronized void updateText() {

		String line = lineCb.getSelectionModel().getSelectedItem() == null ? ""
				: lineCb.getSelectionModel().getSelectedItem().toString();
		String workOrder = workOrderCb.getSelectionModel().getSelectedItem() == null ? ""
				: workOrderCb.getSelectionModel().getSelectedItem().toString();
		String boardType = boardTybeCb.getSelectionModel().getSelectedItem() == null ? ""
				: boardTybeCb.getSelectionModel().getSelectedItem().toString();
		if (!line.equals("") && !workOrder.equals("") && !boardType.equals("")) {
			try {
				Integer boardTypeNo = getBoardTypeNo(boardType);
				Program program = new Program();
				program.setLine(line);
				program.setWorkOrder(workOrder);
				program.setBoardType(boardTypeNo);
				programItemVisitSession = MybatisHelper.getMS(MYBATIS_CONFIG_PATH, ProgramItemVisitMapper.class,
						"smt_001");
				List<ProgramItemVisit> programItemVisits = programItemVisitSession.getMapper().selectByProgram(program);
				String operator = operationSession.getMapper().selectOperator(program);
				operator = operator == null ? "unkonwn" : operator;
				if (programItemVisits != null && programItemVisits.size() > 0) {
					ProgramItemVisit programItemVisit = programItemVisits.get(0);
					lineseatLb.setText(programItemVisit.getLineseat());
					scanLineseatLb.setText(programItemVisit.getScanLineseat());
					materialNoLb.setText(programItemVisit.getMaterialNo());
					scanMaterialNoLb.setText(programItemVisit.getScanMaterialNo());
					operatorLb.setText(operator);
					setTypeAndResult(programItemVisit);
					tableLsit = FXCollections.observableArrayList(programItemVisitToResultData(programItemVisits));
					DataTv.setItems(tableLsit);
				}
			} catch (IOException e) {
				logger.error("updateText中ProgramItemVisit表Session创建失败");
				e.printStackTrace();
			} finally {
				programItemVisitSession.colseSession();
			}
		}
	}

	/**
	 * 设置typeLb和resultLb文本值，更改操作结果
	 * 
	 * @param programItemVisit
	 */
	private void setTypeAndResult(ProgramItemVisit programItemVisit) {

		if (programItemVisit.getLastOperationType() != null) {
			switch (programItemVisit.getLastOperationType()) {
			case 0:
				typeLb.setText("上  料");
				showResult(programItemVisit.getFeedResult());
				break;
			case 1:
				typeLb.setText("换  料");
				showResult(programItemVisit.getChangeResult());
				break;
			case 2:
				typeLb.setText("核  料");
				showResult(programItemVisit.getCheckResult());
				break;
			case 3:
				typeLb.setText("全  检");
				showResult(programItemVisit.getCheckAllResult());
				break;
			case 4:
				typeLb.setText("发  料");
				showResult(programItemVisit.getStoreIssueResult());
				break;
			case 5:
				typeLb.setText("首  检");
				showResult(programItemVisit.getFirstCheckAllResult());
				break;
			default:
				showResult(DEFAULT_RESULT);
				break;
			}
		} else {
			showResult(DEFAULT_RESULT);
		}
	}

	private void showResult(Integer result) {
		switch (result) {
		case 0:
			typeLb.setStyle(
					"-fx-background-color:red;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei';");
			resultLb.setStyle(
					"-fx-background-color:red;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			resultLb.setText("FAIL");
			break;
		case 1:
			typeLb.setStyle(
					"-fx-background-color:green;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			resultLb.setStyle(
					"-fx-background-color:green;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			resultLb.setText("PASS");
			break;
		case 2:
			typeLb.setStyle(
					"-fx-background-color:green;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			resultLb.setStyle(
					"-fx-background-color:green;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			typeLb.setText("操  作");
			resultLb.setText("结  果");
			break;
		case 3:
			typeLb.setStyle(
					"-fx-background-color:red;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			resultLb.setStyle(
					"-fx-background-color:red;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			resultLb.setText("已超时");
			break;
		case 4:
			typeLb.setStyle(
					"-fx-background-color:purple;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			resultLb.setStyle(
					"-fx-background-color:purple;-fx-alignment:center;-fx-font-size:160px;-fx-text-fill:white;-fx-font-family:'Microsoft YaHei'");
			typeLb.setText("已换料");
			resultLb.setText("请核料");
			break;
		default:
			break;
		}
	}

	/**
	 * 将ProgramItemVisit转化为可被表格识别的ResultData
	 * 
	 * @param programItemVisits
	 * @return
	 */
	private List<ResultData> programItemVisitToResultData(List<ProgramItemVisit> programItemVisits) {
		List<ResultData> resultDatas = new ArrayList<>();
		for (ProgramItemVisit programItemVisit : programItemVisits) {
			ResultData resultData = new ResultData();
			resultData.setLineseat(programItemVisit.getLineseat());
			resultData.setStoreIssueResult(programItemVisit.getStoreIssueResult());
			resultData.setFeedResult(programItemVisit.getFeedResult());
			resultData.setChangeResult(programItemVisit.getChangeResult());
			resultData.setCheckResult(programItemVisit.getCheckResult());
			resultData.setCheckAllResult(programItemVisit.getCheckAllResult());
			resultData.setFirstCheckAllResult(programItemVisit.getFirstCheckAllResult());
			resultDatas.add(resultData);
		}
		return resultDatas;
	}

	/**
	 * 清除文本框文本，表格数据
	 */
	private void clearText() {
		lineseatLb.setText("");
		scanLineseatLb.setText("");
		materialNoLb.setText("");
		scanMaterialNoLb.setText("");
		operatorLb.setText("");
		showResult(DEFAULT_RESULT);
		DataTv.setItems(null);
	}

	/**
	 * 清空板面类型下拉框，重置工单下拉框内容
	 * @param line 线号
	 */
	private void resetWorkOrderCb(String line) {
		workOrderCb.getItems().clear();
		boardTybeCb.getItems().clear();
		List<String> workorders = programSession.getMapper().selectByLine(line);
		ObservableList<String> workOrderList = FXCollections.observableArrayList(workorders);
		workOrderCb.setItems(workOrderList);
		

	}
	
	/**
	 * 将板面类型数字转化为中文
	 * @param boardTybes 板面类型数字字符数组
	 * @return
	 */
	private List<String> initBoardType(List<String> boardTybes) {
		List<String> boardTybeList = new ArrayList<>();
		for (String boardTybe : boardTybes) {
			if (boardTybe.equals("0")) {
				boardTybeList.add("默认");
			} else if (boardTybe.equals("1")) {
				boardTybeList.add("AB面");
			} else if (boardTybe.equals("2")) {
				boardTybeList.add("A面");
			} else if (boardTybe.equals("3")) {
				boardTybeList.add("B面");
			}
		}
		return boardTybeList;
	}

	// 将板面类型中文转化为数字
	private Integer getBoardTypeNo(String boardType) {
		Integer boardTypeNo = null;
		if (boardType.equals("默认")) {
			boardTypeNo = 0;
		} else if (boardType.equals("AB面")) {
			boardTypeNo = 1;
		} else if (boardType.equals("A面")) {
			boardTypeNo = 2;
		} else if (boardType.equals("B面")) {
			boardTypeNo = 3;
		}
		return boardTypeNo;
	}
	
	/**
	 * 关闭程序事件
	 * @param primaryStage
	 */
	public void closeWindow(Stage primaryStage) {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				String line = lineCb.getSelectionModel().getSelectedItem() == null ? ""
						: lineCb.getSelectionModel().getSelectedItem().toString();
				if (!line.equals("")) {
					if (asyncCommunicator != null) {
						asyncCommunicator.connect(new OnConnectedListener() {
							
							@Override
							public void onSucceed() {
								asyncCommunicator.send(boardResetPackage, new OnReplyPackageArrivedListener() {

									@Override
									public void onReplyPackageArrived(BasePackage r) {
										Log sLog = createLogByPackage(boardResetPackage);
										logger.info("发送重置包：" + sLog.getData());
										if (r != null && r instanceof BoardResetReplyPackage) {
											BoardResetReplyPackage reply = (BoardResetReplyPackage) r;
											Log rLog = createLogByPackage(reply);
											logger.info("接收重置包：" + rLog.getData());
											if (!reply.getControlResult().equals(ControlResult.SUCCEED)) {
												logger.error(
														"关闭时重置工单生产数目失败，取消工单失败，IP:" + asyncCommunicator.getRemoteIp() + "服务器内部错误");
												
											}else {
												logger.info("关闭时重置生产数目成功");
												
											}
											if (asyncCommunicator != null) {
												asyncCommunicator.close();
											}
										}else {
											logger.error(
													"关闭时重置工单生产数目失败，取消工单失败，IP:" + asyncCommunicator.getRemoteIp() + "服务器内部错误");
											if (asyncCommunicator != null) {
												asyncCommunicator.close();
											}
										}
										sendHttpClose(line);
									}

									@Override
									public void onCatchIOException(IOException exception) {
										logger.error("关闭时重置工单生产数目失败，取消工单失败，IP:" + asyncCommunicator.getRemoteIp() + "网络连接错误");
										if (asyncCommunicator != null) {
											asyncCommunicator.close();
										}
										sendHttpClose(line);
									}
								});
							}
							
							@Override
							public void onFailed(IOException e) {
								logger.error("关闭时重置工单生产数目失败，取消工单失败，IP:" + asyncCommunicator.getRemoteIp() + "网络连接错误");
								if (asyncCommunicator != null) {
									asyncCommunicator.close();
								}
								sendHttpClose(line);
							}
						});
						
						
					}
				}
			}
		});
	}
	
	private void sendHttpClose(String line) {
		Map<String, String> map = new HashMap<>();
		map.put("line", line);
		try {
			httpHelper.requestHttp(SWITCH_ACTION, map, new okhttp3.Callback() {

				@Override
				public void onResponse(Call call, Response response)
						throws IOException {
					if (response.body().string().equals("{\"result\":\"succeed\"}")) {
						logger.info("关闭时取消工单成功，线号："+line);
					} else {
						logger.error("关闭时取消工单失败，服务器内部错误，线号："+line);
					}
					System.exit(0);
				}

				@Override
				public void onFailure(Call call, IOException e) {
					logger.error("关闭时取消工单失败，网络连接出错，线号："+line);
					System.exit(0);
				}
			});
		} catch (IOException httpe) {
			logger.error("关闭时取消工单失败，网络连接出错，线号："+line);
			System.exit(0);
			
		}
	}

	/**
	 * 根据包创建日志实体
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