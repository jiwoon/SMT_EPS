package com.jimi.smt.eps_appclient.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jimi.smt.eps_appclient.Activity.QCActivity;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.HttpUtils;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EvenBusTest;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;
import com.jimi.smt.eps_appclient.Views.MyEditTextDel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class CheckMaterialFragment extends Fragment implements OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    private GlobalData globalData;
    private LoadingDialog loadingDialog;
    //检料视图
    private View vCheckMaterialFragment;

    //操作员　站位　料号
    private TextView edt_Operation;
    private MyEditTextDel edt_LineSeat;
    private MyEditTextDel edt_Material;
    private TextView tv_Result, tv_Remark, tv_LastInfo;
    //当前扫描的站位,料号
    private String curLineSeat, curMaterial;
    //当前料号表中的站位,料号
    private String curOrgSeatNo;
    private String curOrgMaterialNo;

    //当前检料时用到的排位料号表
    private List<MaterialItem> lCheckMaterialItems = new ArrayList<MaterialItem>();
    //核料时检测的料号
    private List<MaterialItem> checkItems = new ArrayList<MaterialItem>();
    //当前检料项
    private int curCheckMaterialId = -1;
    private GlobalFunc globalFunc;
    private QCActivity qcActivity;

    private boolean mHidden = false;

    private boolean first_checkAll_result;
    private static final int PROGRAM_UPDATE = 102;//站位表更新
    private static final int FIRST_CHECKALL_TRUE = 103;
    private static final int FIRST_CHECKALL_FALSE = 104;
    private static final int CHANGE_SUC = 105;
    private static final int CHANGE_FAIL = 106;
    private int checkType;
    @SuppressLint("HandlerLeak")
    private Handler checkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
            switch (msg.what) {

                case PROGRAM_UPDATE:
                    qcActivity.showInfo("提示", "站位表更新!", "IPQC未做首次全检");
                    break;
                case FIRST_CHECKALL_TRUE:
                    break;
                case FIRST_CHECKALL_FALSE:
                    edt_LineSeat.setText("");
                    if (checkType == 0) {
                        // TODO: 2018/4/9  
                        qcActivity.showInfo("提示", "IPQC未做首次全检", "");
                    }
                    break;
                case CHANGE_FAIL:
                    //换料失败
                    clearAndSetFocus();
                    showWarnResult((String) msg.obj);
                    break;
                case CHANGE_SUC:
                    //换料成功
                    curLineSeat = (String) msg.obj;
                    edt_Material.requestFocus();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreateView");
        //注册订阅
        EventBus.getDefault().register(this);
        qcActivity = (QCActivity) getActivity();
        globalData = (GlobalData) getActivity().getApplication();
        globalFunc = new GlobalFunc(getActivity());
        qcActivity.callBackInfoDialogClick(new QCActivity.QCActivityInterface() {
            @Override
            public void infoDialogClick_callBack() {
                /*
                if (loadingDialog != null && loadingDialog.isShowing()){
                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                }
                */
                if (!mHidden) {
                    Log.d(TAG, "接收回调");
                    clearAndSetFocus();
                    checkType = 1;
                    getFirstCheckAllResult(0);
                }
            }
        });
    }

    //FragmentTransaction来控制fragment调用该方法
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.mHidden = hidden;
        Log.d(TAG, "onHiddenChanged - " + hidden);
        if (!hidden) {
            if (qcActivity.updateDialog != null && qcActivity.updateDialog.isShowing()) {
                qcActivity.updateDialog.cancel();
                qcActivity.updateDialog.dismiss();
            }
            checkType = 0;
            //判断是否首次全检
            getFirstCheckAllResult(0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vCheckMaterialFragment = inflater.inflate(R.layout.checkmaterial_layout, container, false);
        //获取工单号和操作员
        Intent intent = getActivity().getIntent();
        savedInstanceState = intent.getExtras();
        Log.i(TAG, "curOderNum::" + savedInstanceState.getString("orderNum") + " -- curOperatorNUm::" + savedInstanceState.getString("operatorNum"));

        globalData.setOperator(savedInstanceState.getString("operatorNum"));

        if (qcActivity.updateDialog != null && qcActivity.updateDialog.isShowing()) {
            qcActivity.updateDialog.cancel();
            qcActivity.updateDialog.dismiss();
        }
        checkType = 0;
        //判断是否首次全检
        getFirstCheckAllResult(0);

        initViews(savedInstanceState);
        initData();//初始化数据
        initEvents();

        return vCheckMaterialFragment;
    }

    //监听订阅的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenBusTest event) {
        Log.d(TAG, "onEventMainThread - " + event.getUpdated());
        if (event.getUpdated() == 0) {
            /*
            if (loadingDialog != null && loadingDialog.isShowing()){
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
            */
            Log.d(TAG, "mHidden - " + mHidden);
            Log.d(TAG, "isUpdateProgram - " + globalData.isUpdateProgram());
            if (!mHidden) {
                if (qcActivity.updateDialog != null && qcActivity.updateDialog.isShowing()) {
                    qcActivity.updateDialog.cancel();
                    qcActivity.updateDialog.dismiss();
                }
                checkType = 0;
                //判断是否首次全检
                getFirstCheckAllResult(1);
            }

        }
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_check_order = (TextView) vCheckMaterialFragment.findViewById(R.id.tv_check_order);
        edt_Operation = (TextView) vCheckMaterialFragment.findViewById(R.id.tv_check_Operation);
        edt_LineSeat = (MyEditTextDel) vCheckMaterialFragment.findViewById(R.id.edt_check_lineseat);
        edt_Material = (MyEditTextDel) vCheckMaterialFragment.findViewById(R.id.edt_check_material);
        tv_Result = (TextView) vCheckMaterialFragment.findViewById(R.id.tv_Result);
        tv_Remark = (TextView) vCheckMaterialFragment.findViewById(R.id.tv_Remark);
        tv_LastInfo = (TextView) vCheckMaterialFragment.findViewById(R.id.tv_LastInfo);

        tv_check_order.setText(bundle.getString("orderNum"));
        edt_Operation.setText(bundle.getString("operatorNum"));

        edt_LineSeat.requestFocus();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG, "initEvents");
        edt_LineSeat.setOnEditorActionListener(this);
        edt_Material.setOnEditorActionListener(this);

        edt_LineSeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    /* todo
                    if (!globalData.isUpdateProgram()){
                        checkType = 0;
                        //判断是否首次全检
                        getFirstCheckAllResult();
                        Log.d(TAG,"globalData-OperType:"+globalData.getOperType());
                    }*/

                }
            }
        });

        edt_Material.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(edt_LineSeat.getText())) {
                        edt_Material.setCursorVisible(false);
                        edt_LineSeat.setText("");
                        edt_LineSeat.requestFocus();
                    } else {
                        edt_Material.setCursorVisible(true);
                    }
                }
            }
        });

        tv_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAgain();
            }
        });
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG, "initData");
        //填充数据
        lCheckMaterialItems.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();
        for (MaterialItem materialItem : materialItems) {
            MaterialItem feedMaterialItem = new MaterialItem(globalData.getWork_order(), globalData.getBoard_type(),
                    globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(),
                    materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
            lCheckMaterialItems.add(feedMaterialItem);
        }
        curCheckMaterialId = -1;
    }

    /**
     * @param textView 　触发的控件
     * @param i
     * @param keyEvent 　key事件
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        Log.i(TAG, "onEditorAction");
        //回车键
        if (i == EditorInfo.IME_ACTION_SEND ||
                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
            switch (keyEvent.getAction()) {
                //按下
                case KeyEvent.ACTION_UP:
                    //先判断是否联网
                    if (globalFunc.isNetWorkConnected()) {
                        if (first_checkAll_result) {
                            //扫描内容
                            String scanValue = String.valueOf(((EditText) textView).getText());
                            scanValue = scanValue.replaceAll("\r", "");
                            Log.i(TAG, "scan Value:" + scanValue);
                            textView.setText(scanValue);
                            //将扫描的内容更新至列表中
                            switch (textView.getId()) {

                                case R.id.edt_check_lineseat:
                                    checkAgain();
                                    //站位
                                    scanValue = globalFunc.getLineSeat(scanValue);
                                    edt_LineSeat.setText(scanValue);
                                    for (int j = 0; j < lCheckMaterialItems.size(); j++) {
                                        MaterialItem materialItem = lCheckMaterialItems.get(j);
                                        if (materialItem.getOrgLineSeat().equalsIgnoreCase(scanValue)) {
                                            curCheckMaterialId = j;
                                            curOrgMaterialNo = materialItem.getOrgMaterial();
                                            materialItem.setScanLineSeat(scanValue);
                                            checkItems.add(materialItem);
                                            Log.i(TAG, "materialItem - " + materialItem.getOrgMaterial());
                                        }
                                    }
                                    if (curCheckMaterialId < 0) {
                                        curLineSeat = scanValue;
                                        curOrgSeatNo = "";
                                        curOrgMaterialNo = "";
                                        showCheckMaterialResult("FAIL", "排位表不存在此站位！", 0, null);
                                        return true;
                                    }
                                    //判断该站位是否换料成功
                                    isChanged(lCheckMaterialItems.get(curCheckMaterialId));
                                    break;

                                case R.id.edt_check_material:
                                    //料号
                                    scanValue = globalFunc.getMaterial(scanValue);
                                    textView.setText(scanValue);
                                    //扫到的料号
                                    curMaterial = scanValue;
                                    curCheckMaterialId = -1;
                                    for (MaterialItem materialItem : checkItems) {
                                        materialItem.setScanMaterial(scanValue);
                                        Log.i(TAG, "materialItem - " + materialItem.getOrgMaterial());
                                        if (materialItem.getOrgMaterial().equalsIgnoreCase(scanValue)) {
                                            curCheckMaterialId = 1;
                                        }
                                    }

                                    if (curCheckMaterialId == -1) {
                                        for (MaterialItem materialItem : checkItems) {
                                            materialItem.setResult("FAIL");
                                            materialItem.setRemark("站位与料号不对应");
                                        }
                                    } else if (curCheckMaterialId == 1) {
                                        for (MaterialItem materialItem : checkItems) {
                                            materialItem.setResult("PASS");
                                            materialItem.setRemark("站位和料号正确");
                                        }
                                    }
                                    showCheckMaterialResult(checkItems.get(0).getResult(), checkItems.get(0).getRemark(), 1, checkItems);

                                    /*
                                    //扫描到的站位
                                    String scanSeatNo = curLineSeat;
                                    ArrayList<Integer> lineSeats = new ArrayList<Integer>();
                                    for (int k = 0; k < lCheckMaterialItems.size(); k++) {
                                        MaterialItem materialItem = lCheckMaterialItems.get(k);
                                        if (materialItem.getOrgMaterial().equalsIgnoreCase(scanValue)) {
                                            //料号存在
                                            lineSeats.add(k);
                                            if (!materialItem.getOrgLineSeat().equalsIgnoreCase(scanSeatNo)) {
                                                //料号对应的站位与扫描的站位不同
                                                curCheckMaterialId = -2;
                                                curOrgSeatNo = materialItem.getOrgLineSeat();
                                            }
                                        }
                                    }
                                    //遍历所有相同料号的站位,判断与扫描到的站位是否一样
                                    for (int j = 0; j < lineSeats.size(); j++) {
                                        if (lCheckMaterialItems.get(lineSeats.get(j)).getOrgLineSeat().equals(scanSeatNo)) {
                                            curCheckMaterialId = lineSeats.get(j);
                                        }
                                    }
                                    //显示结果
                                    String Remark = "";
                                    if (curCheckMaterialId < 0) {
                                        curOrgSeatNo = scanSeatNo;
                                        if (curCheckMaterialId == -2) {
                                            Remark = "站位与料号不对应！";
                                            showCheckMaterialResult(1, Remark, 1);
                                        } else {
                                            Remark = "排位表不存在此料号！";
                                            showCheckMaterialResult(1, Remark, 1);
                                        }
                                    } else {
                                        MaterialItem materialItem = lCheckMaterialItems.get(curCheckMaterialId);
                                        materialItem.setScanMaterial(scanValue);
                                        materialItem.setResult("PASS");
                                        materialItem.setRemark("站位和料号正确!");
                                        showCheckMaterialResult(0, "站位和料号正确!", 1);
                                    }
                                    */


                                    break;
                            }
                        } else {
                            // TODO: 2018/4/9
                            /*
                            if (loadingDialog != null && loadingDialog.isShowing()){
                                loadingDialog.cancel();
                                loadingDialog.dismiss();
                            }
                            */
                            qcActivity.showInfo("提示", "IPQC未做首次全检", "");
                        }
                    } else {
                        globalFunc.showInfo("警告", "请检查网络连接是否正常!", "请连接网络!");
                        //自动清空站位和料号以便下一项检测
                        clearAndSetFocus();
                    }

                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    //显示抽检结果
    @SuppressLint("SetTextI18n")
    private void showCheckMaterialResult(String checkResult, String strRemark, int logType, List<MaterialItem> itemList) {
        String result = "";
        switch (checkResult) {
            case "PASS":
                tv_Result.setBackgroundColor(Color.GREEN);
                result = "PASS";
                tv_Remark.setTextColor(Color.argb(255, 102, 153, 0));
                break;
            case "FAIL":
                tv_Result.setBackgroundColor(Color.RED);
                result = "FAIL";
                tv_Remark.setTextColor(Color.RED);
                break;
        }
        tv_Remark.setText(strRemark);
        tv_Result.setText(result);
        tv_LastInfo.setText("扫描结果\r\n站位:" + curLineSeat + "\r\n" + "料号:" + curMaterial);



        /*
        //保存至数据库日志
        MaterialItem materialItem;
        if (curCheckMaterialId >= 0) {
            materialItem = lCheckMaterialItems.get(curCheckMaterialId);
        } else {
            materialItem = new MaterialItem(
                    globalData.getWork_order(), globalData.getBoard_type(), globalData.getLine(), -1, (byte) 0,
                    curOrgSeatNo, curOrgMaterialNo, String.valueOf(edt_LineSeat.getText()),
                    String.valueOf(String.valueOf(edt_Material.getText())),
                    result, strRemark);
        }
        Log.d(TAG, "curCheckMaterialId:" + curCheckMaterialId);
        Log.d(TAG, "materialItem-" + materialItem.getOrgLineSeat() + "-" + materialItem.getOrgMaterial());
        */


        if (logType == 1) {
            if (itemList != null && itemList.size() > 0) {
                for (MaterialItem materialItem : itemList) {
                    //添加操作日志
                    globalData.setOperType(Constants.CHECKMATERIAL);
                    globalFunc.AddDBLog(globalData, materialItem);
                    //添加显示日志
                    globalData.setUpdateType(Constants.CHECKMATERIAL);
                    // TODO: 2018/4/27
//            globalFunc.updateVisitLog(globalData, materialItem);
                    HttpUtils.getHttpUtils().operate(materialItem, Constants.CHECKMATERIAL);
                }
            }
        }
        //自动清空站位和料号以便下一项检测
        clearAndSetFocus();
    }

    //判断是否全部进行了首次全检
    private void getFirstCheckAllResult(int condition) {
        //判断工位检测功能是否打开
        if (Constants.isCheckWorkType) {
            if (!first_checkAll_result) {
                loadingDialog = new LoadingDialog(getActivity(), "正在加载...");
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    boolean result = new DBService().isOrderFirstCheckAll(globalData.getLine(),
                            globalData.getWork_order(), globalData.getBoard_type());
                    Message message = Message.obtain();
                    first_checkAll_result = result;
                    if (!result) {
                        if (condition == 0) {
                            message.what = FIRST_CHECKALL_FALSE;
                        } else if (condition == 1) {
                            message.what = PROGRAM_UPDATE;
                        }
                    }

                    checkHandler.sendMessage(message);
                }
            }).start();
        } else {
            //工位检测功能未打开
            first_checkAll_result = true;
        }
    }

    //判断某个站位是否换料成功
    private void isChanged(final MaterialItem materialItem) {
        /*
        loadingDialog = new LoadingDialog(getActivity(),"正在加载...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        */
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = true;
                List<Integer> integers = new DBService().getChangeResult(materialItem);
                for (Integer integer : integers) {
                    if (integer == 0) {
                        result = false;
                        break;
                    }
                }
                Message message = Message.obtain();
                if (result) {
                    //换料成功
                    message.what = CHANGE_SUC;
                } else {
                    //换料失败
                    message.what = CHANGE_FAIL;
                }
                message.obj = materialItem.getScanLineSeat();
                checkHandler.sendMessage(message);
            }
        }).start();
    }

    //显示警告结果
    @SuppressLint("SetTextI18n")
    private void showWarnResult(String lineSeat) {
        tv_Remark.setText("该站位未换料成功");
        tv_Remark.setTextColor(Color.argb(255, 182, 171, 17));
        tv_Result.setText("WARN");
        tv_Result.setBackgroundColor(Color.YELLOW);
        tv_LastInfo.setText("扫描结果\r\n站位:" + lineSeat + "\r\n" + "料号:");
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 重新检下一个料
     */
    private void checkAgain() {
        Log.i(TAG, "testAgain");
        curCheckMaterialId = -1;
        tv_Remark.setText("");
        tv_Result.setText("");
        tv_Result.setBackgroundColor(Color.TRANSPARENT);
        curLineSeat = "";
        curMaterial = "";
        edt_Material.setText("");

        curOrgSeatNo = "";
        curOrgMaterialNo = "";
        checkItems.clear();
    }

    private void clearAndSetFocus() {
        edt_LineSeat.setText("");
        edt_Material.setText("");
        edt_LineSeat.requestFocus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销订阅
        EventBus.getDefault().unregister(this);
    }

}
