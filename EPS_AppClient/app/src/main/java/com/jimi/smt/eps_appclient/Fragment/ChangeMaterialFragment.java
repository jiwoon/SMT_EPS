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

import com.jimi.smt.eps_appclient.Activity.FactoryLineActivity;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EvenBusTest;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;
import com.jimi.smt.eps_appclient.Views.MyEditTextDel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @ 描述:换料
 */
public class ChangeMaterialFragment extends Fragment implements TextView.OnEditorActionListener, View.OnFocusChangeListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    private GlobalData globalData;
    private LoadingDialog loadingDialog;
    //换料视图
    private View vChangeMaterialFragment;
    //操作员　站位　料号
    private TextView edt_Operation;
    private MyEditTextDel edt_LineSeat;
    private MyEditTextDel edt_OrgMaterial;
    private MyEditTextDel edt_ChgMaterial;
    private TextView tv_Result, tv_Remark, tv_lastInfo;

    //当前的站位，线上料号，更换料号
    private String curLineSeat, curOrgMaterial, curChgMaterial;
    //线上料号的时间戳、更换的料号的时间戳
    private String orgTimestamp, chgTimestamp;

    //当前换料时用到的排位料号表x
    private List<MaterialItem> lChangeMaterialItem = new ArrayList<MaterialItem>();
    //该站位的料号(包括替换料)、和位置
    private ArrayList<String> materialList = new ArrayList<String>();
    private ArrayList<Integer> materialIndex = new ArrayList<Integer>();

    //当前换料项
    private int curChangeMaterialId = -1;
    private GlobalFunc globalFunc;
    private FactoryLineActivity factoryLineActivity;
    private InfoDialog infoDialog;

    private boolean first_checkAll_result;
    private static final int FIRST_CHECKALL_TRUE = 101;
    private static final int FIRST_CHECKALL_FALSE = 102;
    private static final int PROGRAM_UPDATE = 103;//站位表更新

    private int checkType;
    private boolean mHidden = false;

    @SuppressLint("HandlerLeak")
    private Handler changeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
            switch (msg.what) {
                case FIRST_CHECKALL_FALSE:
                    edt_LineSeat.setText("");
                    if (checkType == 0) {
                        showInfo("提示", "IPQC未做首次全检", "");
                    }
                    break;

                case PROGRAM_UPDATE:
                    showInfo("提示", "站位表更新!", "IPQC未做首次全检");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vChangeMaterialFragment = inflater.inflate(R.layout.changematerial_layout, container, false);
        //获取工单号和操作员
        Intent intent = getActivity().getIntent();
        savedInstanceState = intent.getExtras();
        Log.i(TAG, "curOderNum::" + savedInstanceState.getString("orderNum") + " -- curOperatorNUm::" + savedInstanceState.getString("operatorNum"));
        globalData.setOperator(savedInstanceState.getString("operatorNum"));
        if (factoryLineActivity.updateDialog != null && factoryLineActivity.updateDialog.isShowing()) {
            factoryLineActivity.updateDialog.cancel();
            factoryLineActivity.updateDialog.dismiss();
        }
        checkType = 0;
        //判断是否首次全检
        getFirstCheckAllResult(0);
        initData();
        initViews(savedInstanceState);
        initEvents();

        return vChangeMaterialFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        //注册订阅
        EventBus.getDefault().register(this);
        factoryLineActivity = (FactoryLineActivity) getActivity();
        globalData = (GlobalData) getActivity().getApplication();
        globalFunc = new GlobalFunc(getActivity());
    }

    //监听订阅的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenBusTest event) {
        Log.d(TAG, "onEventMainThread - " + event.getUpdated());
        if (event.getUpdated() == 0) {
            if (infoDialog != null && infoDialog.isShowing()) {
                infoDialog.cancel();
                infoDialog.dismiss();
            }

            Log.d(TAG, "mHidden - " + mHidden);
            Log.d(TAG, "isUpdateProgram - " + globalData.isUpdateProgram());
            if (!mHidden) {
                checkType = 0;
                if (factoryLineActivity.updateDialog != null && factoryLineActivity.updateDialog.isShowing()) {
                    factoryLineActivity.updateDialog.cancel();
                    factoryLineActivity.updateDialog.dismiss();
                }
                getFirstCheckAllResult(1);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "onHiddenChanged-" + hidden);
        this.mHidden = hidden;
        if (!hidden) {
            if (factoryLineActivity.updateDialog != null && factoryLineActivity.updateDialog.isShowing()) {
                factoryLineActivity.updateDialog.cancel();
                factoryLineActivity.updateDialog.dismiss();
            }
            checkType = 0;
            //判断是否首次全检
            getFirstCheckAllResult(0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored");
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        //注销订阅
        EventBus.getDefault().unregister(this);
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_change_order = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_change_order);
        edt_Operation = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_change_Operation);
        edt_LineSeat = (MyEditTextDel) vChangeMaterialFragment.findViewById(R.id.edt_change_lineseat);
        edt_OrgMaterial = (MyEditTextDel) vChangeMaterialFragment.findViewById(R.id.edt_change_OrgMaterial);
        edt_ChgMaterial = (MyEditTextDel) vChangeMaterialFragment.findViewById(R.id.edt_change_ChgMaterial);
        tv_Result = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_Result);
        tv_lastInfo = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_LastInfo);
        tv_Remark = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_Remark);

        tv_change_order.setText(bundle.getString("orderNum"));
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
        edt_OrgMaterial.setOnEditorActionListener(this);
        edt_ChgMaterial.setOnEditorActionListener(this);
        edt_LineSeat.setOnFocusChangeListener(this);
        edt_OrgMaterial.setOnFocusChangeListener(this);
        edt_ChgMaterial.setOnFocusChangeListener(this);

        //点击结果后换下一个料
        tv_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNextMaterial();
                clearResultRemark();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {

            case R.id.edt_change_lineseat:
                if (hasFocus) {
                    /* todo
                    if (!globalData.isUpdateProgram()){
                        checkType = 0;
                        //判断是否首次全检
                        getFirstCheckAllResult();
                        Log.d(TAG,"globalData-OperType:"+globalData.getOperType());
                    }
                    */
                }
                break;

            case R.id.edt_change_OrgMaterial:
                if (hasFocus) {
                    if (TextUtils.isEmpty(edt_LineSeat.getText())) {
                        edt_OrgMaterial.setCursorVisible(false);
                        edt_LineSeat.setText("");
                        edt_LineSeat.requestFocus();
                    } else {
                        edt_OrgMaterial.setCursorVisible(true);
                    }
                }
                break;

            case R.id.edt_change_ChgMaterial:
                if (hasFocus) {
                    if (TextUtils.isEmpty(edt_LineSeat.getText())) {
                        edt_ChgMaterial.setCursorVisible(false);
                        edt_LineSeat.setText("");
                        edt_LineSeat.requestFocus();
                    } else if (TextUtils.isEmpty(edt_OrgMaterial.getText())) {
                        edt_ChgMaterial.setCursorVisible(false);
                        edt_OrgMaterial.setText("");
                        edt_OrgMaterial.requestFocus();
                    } else {
                        edt_ChgMaterial.setCursorVisible(true);
                    }
                }
                break;
        }
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG, "initData");
        curChangeMaterialId = -1;
        //填充数据
        lChangeMaterialItem.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();

        for (MaterialItem materialItem : materialItems) {
            MaterialItem feedMaterialItem = new MaterialItem(globalData.getWork_order(), globalData.getBoard_type(),
                    globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
                    "", "", "", "");
            lChangeMaterialItem.add(feedMaterialItem);
        }

    }

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
                            String strValue = String.valueOf(((EditText) textView).getText());
                            strValue = strValue.replaceAll("\r", "");
                            Log.i(TAG, "strValue:" + strValue);
                            textView.setText(strValue);

                            //将扫描的内容更新至列表中
                            switch (textView.getId()) {
                                case R.id.edt_change_lineseat:
                                    changeNextMaterial();
                                    strValue = globalFunc.getLineSeat(strValue);
                                    edt_LineSeat.setText(strValue);
                                    //站位
                                    for (int j = 0; j < lChangeMaterialItem.size(); j++) {
                                        MaterialItem materialItem = lChangeMaterialItem.get(j);
                                        if (materialItem.getOrgLineSeat().equalsIgnoreCase(strValue)) {
                                            curChangeMaterialId = j;
                                            materialItem.setScanLineSeat(strValue);
                                            //保存料号、和位置
                                            materialList.add(materialItem.getOrgMaterial());
                                            materialIndex.add(j);
                                        }
                                    }
                                    if (curChangeMaterialId < 0) {
                                        curLineSeat = strValue;
                                        displayResult(1, "", "排位表不存在此站位！", 0);
                                        //清空列表
                                        materialIndex.clear();
                                        materialList.clear();
                                        return true;
                                    }
                                    curLineSeat = strValue;
                                    edt_OrgMaterial.requestFocus();
                                    break;

                                case R.id.edt_change_OrgMaterial://站位正确后才进入这里
                                    //线上料号的时间戳
                                    orgTimestamp = globalFunc.getSerialNum(strValue);
                                    //料号
                                    strValue = globalFunc.getMaterial(strValue);
                                    textView.setText(strValue);
                                    curOrgMaterial = strValue;
                                    //初始化换料位置
                                    curChangeMaterialId = -1;
                                    //先获取扫描到的站位
                                    String scanSeatNo = curLineSeat;
                                    Log.d(TAG, "scanSeatNo-" + scanSeatNo);
                                    //判断扫到的料号是否等于站位的原始料号
                                    for (int jj = 0; jj < materialList.size(); jj++) {
                                        if (materialList.get(jj).equalsIgnoreCase(curOrgMaterial)) {
                                            curChangeMaterialId = materialIndex.get(jj);
                                        }
                                    }
                                    //扫描到的料号不存在表中
                                    if (curChangeMaterialId < 0) {
                                        displayResult(1, scanSeatNo, "料号与站位不对应！", 1);
                                        materialIndex.clear();
                                        materialList.clear();
                                        return true;
                                    }
                                    edt_ChgMaterial.requestFocus();
                                    break;

                                case R.id.edt_change_ChgMaterial://站位且线上料号正确后才进入这里
                                    //更换料号的流水号
                                    chgTimestamp = globalFunc.getSerialNum(strValue);
                                    //更换料号
                                    strValue = globalFunc.getMaterial(strValue);
                                    textView.setText(strValue);
                                    curChgMaterial = strValue;
                                    //初始化换料位置
                                    curChangeMaterialId = -1;
                                    //先获取扫描到的站位
                                    String scanSeatNum = curLineSeat;
                                    Log.d(TAG, "scanSeatNum-" + scanSeatNum);

                                    //时间相同、并且料号也相同(防呆)
                                    if ((chgTimestamp.equalsIgnoreCase(orgTimestamp)) && (curChgMaterial.equals(curOrgMaterial))) {
                                        curChangeMaterialId = -2;
                                    } else {
                                        //不相等,判断扫到的料号是否等于站位的原始料号
                                        for (int kk = 0; kk < materialList.size(); kk++) {
                                            if (materialList.get(kk).equalsIgnoreCase(curChgMaterial)) {
                                                curChangeMaterialId = materialIndex.get(kk);
                                            }
                                        }
                                    }
                                    //扫描到的料号不存在表中
                                    if (curChangeMaterialId < 0) {
                                        if (curChangeMaterialId == -1) {
                                            displayResult(1, scanSeatNum, "料号与站位不对应！", 1);
                                        } else if (curChangeMaterialId == -2) {
                                            displayResult(1, scanSeatNum, "不能扫同一个料盘", 1);
                                        }
                                        materialIndex.clear();
                                        materialList.clear();
                                        return true;
                                    }
                                    //扫到的料号在站位表中
                                    if (edt_ChgMaterial.getText().toString().equals(edt_OrgMaterial.getText().toString())) {
                                        displayResult(0, lChangeMaterialItem.get(curChangeMaterialId).getOrgLineSeat(), "换料成功!", 1);
                                    } else {
                                        displayResult(0, lChangeMaterialItem.get(curChangeMaterialId).getOrgLineSeat(), "主替料换料成功!", 1);
                                    }
                                    //清空该站位的料号(包括替换料)、和位置
                                    materialIndex.clear();
                                    materialList.clear();
                                    edt_LineSeat.requestFocus();
                                    break;
                            }
                        } else {
                            showInfo("提示", "IPQC未做首次全检", "");
                        }
                    } else {
                        globalFunc.showInfo("警告", "请检查网络连接是否正常!", "请连接网络!");
                        clearAndSetFocus();
                    }
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    //判断是否全部进行了首次全检
    private void getFirstCheckAllResult(int condition) {
        Log.d(TAG, "getFirstCheckAllResult");
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

                    changeHandler.sendMessage(message);
                }
            }).start();
        } else {
            //工位检测功能未打开
            first_checkAll_result = true;
        }
    }

    /**
     * @param i
     */
    @SuppressLint("SetTextI18n")
    private void displayResult(int i, String orgLineSeat, String remark, int logType) {
        Log.i(TAG, "displayResult");
        String Result = "";
        switch (i) {
            case 0:
                tv_Result.setBackgroundColor(Color.GREEN);
                tv_Remark.setTextColor(Color.argb(255, 102, 153, 0));
                Result = "PASS";
                break;
            case 1:
                tv_Result.setBackgroundColor(Color.RED);
                tv_Remark.setTextColor(Color.RED);
                Result = "FAIL";

                break;
        }
        tv_Result.setText(Result);
        tv_Remark.setText(remark);
        tv_lastInfo.setText("扫描结果: 站位:" + curLineSeat
                + "\r\n原始料号:" + curOrgMaterial
                + "\r\n替换料号:" + curChgMaterial);

        globalData.setOperType(Constants.CHANGEMATERIAL);
        globalData.setUpdateType(Constants.CHANGEMATERIAL);
        // TODO: 2018/4/1
        MaterialItem materialItem = new MaterialItem(
                globalData.getWork_order(), globalData.getBoard_type(), globalData.getLine(), -1, (byte) 0,
                orgLineSeat, String.valueOf(edt_OrgMaterial.getText()),
                String.valueOf(edt_LineSeat.getText()), String.valueOf(edt_ChgMaterial.getText()),
                Result, remark);

        //添加操作日志
        globalFunc.AddDBLog(globalData, materialItem);

        //扫描的站位不在站位表上,不显示
        if (logType == 1) {
            //添加显示换料日志
            globalFunc.updateVisitLog(globalData, materialItem);
        }
        clearAndSetFocus();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 测试下一项
     */
    private void changeNextMaterial() {
        Log.i(TAG, "changeNextMaterial");
        tv_Result.setBackgroundColor(Color.TRANSPARENT);
        clearAndSetFocus();
        clearResultRemark();
    }

    private void clearAndSetFocus() {
        edt_LineSeat.setText("");
        edt_OrgMaterial.setText("");
        edt_ChgMaterial.setText("");
        edt_LineSeat.requestFocus();

        curLineSeat = "";
        curOrgMaterial = "";
        curChgMaterial = "";
        curChangeMaterialId = -1;
    }

    private void clearResultRemark() {
        tv_Result.setText("");
        tv_Remark.setText("");
    }

    //IPQC未做首次全检
    private void showInfo(String title, String message, String tip) {
        //对话框所有控件id
        int itemResIds[] = new int[]{R.id.dialog_title_view,
                R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust, R.id.tv_alert_msg};
        //标题和内容
        String titleMsg[] = new String[]{title, message, tip};
        //内容的样式
        int msgStype[] = new int[]{22, Color.RED, Color.argb(255, 219, 201, 36)};

        infoDialog = new InfoDialog(getActivity(),
                R.layout.info_dialog_layout, itemResIds, titleMsg, msgStype);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.info_trust:
                        dialog.dismiss();
                        clearAndSetFocus();
                        checkType = 1;
                        getFirstCheckAllResult(0);
                        break;
                }
            }
        });
        infoDialog.show();
    }

}
