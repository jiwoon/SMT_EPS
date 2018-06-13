package com.jimi.smt.eps_appclient.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Activity.FactoryLineActivity;
import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Dao.FLCheckAll;
import com.jimi.smt.eps_appclient.Dao.GreenDaoUtil;
import com.jimi.smt.eps_appclient.Dao.QcCheckAll;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.HttpUtils;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EvenBusTest;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;
import com.jimi.smt.eps_appclient.Views.InputDialog;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;
import com.jimi.smt.eps_appclient.Views.MyEditTextDel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class CheckAllMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    private LoadingDialog loadingDialog;
    //全局变量
    private GlobalData globalData;
    //上料视图
    private View vCheckAllMaterialFragment;
    private MyEditTextDel edt_ScanMaterial;
    //上料列表
    private ListView lv_CheckAllMaterial;
    private MaterialAdapter materialAdapter;
    //当前检料时用到的排位料号表
    private List<MaterialItem> lCheckAllMaterialItem = new ArrayList<MaterialItem>();
    //操作员全检纪录
    private List<FLCheckAll> flCheckAllList = new ArrayList<FLCheckAll>();
    //IPQC全检纪录
    private List<QcCheckAll> qcCheckAllList = new ArrayList<QcCheckAll>();
    //是否恢复缓存
    private boolean isRestoreCache = false;
    //当前检料项
    private int curCheckId = 0;
    //长按时选择的行
    private int selectRow = -1;
    private GlobalFunc globalFunc;
    //用户类型
    private int user_type;
    private boolean first_checkAll_result;
    private boolean feed_result;
    private static final int PROGRAM_UPDATE = 105;//站位表更新
    private static final int FIRST_CHECKALL_FALSE = 106;//首错
    private static final int RESET = 107;//重置
    private static final int RESET_SHOWTIP = 108;//重置并提示
    private static final int OPERATING = 109;//操作
    private int checkType;//0 首次访问数据库 ; 1 非首次访问数据库
    private InputDialog inputDialog;
    private InfoDialog resultInfoDialog;
    private boolean mHidden = false;
    private FactoryLineActivity factoryLineActivity;

    @SuppressLint("HandlerLeak")
    private Handler checkAllHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //取消弹出窗
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
            switch (msg.what) {

                case PROGRAM_UPDATE:
                    //站位表更新
                    showInfo("提示", "站位表更新!", "IPQC未做首次全检", 1);
                    break;

                case FIRST_CHECKALL_FALSE://首错
                    Log.d(TAG, "FIRST_CHECKALL_FALSE - " + checkType);
                    edt_ScanMaterial.setText("");
                    if (checkType == 0) {
                        showInfo("提示", "IPQC未做首次全检", "", 1);
                    }
                    break;

                case RESET_SHOWTIP:
                    Log.d(TAG, "RESET_SHOWTIP - " + checkType);
                    edt_ScanMaterial.setText("");
                    if (checkType == 0) {
                        showInfo("提示", "IPQC未做首次全检", "", 1);
                    }
                    clearMaterialInfo();
                    break;

                case RESET://重置
                    clearMaterialInfo();
                    break;

                case OPERATING://操作
                    beginOperate(msg.arg2, (String) msg.obj, msg.arg1);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vCheckAllMaterialFragment = inflater.inflate(R.layout.checkallmaterial_layout, container, false);
        //获取工单号和操作员
        Intent intent = getActivity().getIntent();
        savedInstanceState = intent.getExtras();
        if (savedInstanceState != null) {
            globalData.setOperator(savedInstanceState.getString("operatorNum"));
        }
        //判断是否首检或上料 todo
        checkType = 0;
        if (factoryLineActivity.updateDialog != null && factoryLineActivity.updateDialog.isShowing()) {
            factoryLineActivity.updateDialog.cancel();
            factoryLineActivity.updateDialog.dismiss();
        }
        getFCAResultAndIsReseted(0, "", -1);
        initViews(savedInstanceState);
        initData();//初始化数据
        initEvents();
        return vCheckAllMaterialFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        //注册订阅
        EventBus.getDefault().register(this);
        globalData = (GlobalData) getActivity().getApplication();
        globalFunc = new GlobalFunc(getActivity().getApplicationContext());
        user_type = globalData.getUserType();
        Log.d(TAG, "用户类型UserType：" + globalData.getUserType());
        factoryLineActivity = (FactoryLineActivity) getActivity();
        if (Constants.isCache) {
            //查询本地数据库是否存在缓存
            List<FLCheckAll> flCheckAlls = new GreenDaoUtil().queryFLCheckRecord(globalData.getOperator(),
                    globalData.getWork_order(), globalData.getLine(), globalData.getBoard_type());
            if (flCheckAlls != null && flCheckAlls.size() > 0) {
                flCheckAllList.addAll(flCheckAlls);
                isRestoreCache = true;
            } else {
                boolean result = new GreenDaoUtil().deleteAllFLCheck();
                Log.d(TAG, "deleteAllFLCheck - " + result);
                isRestoreCache = false;
            }
        }
    }

    //监听订阅的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenBusTest event) {
        Log.d(TAG, "onEventMainThread - " + event.getUpdated());
        if (event.getUpdated() == 0) {
            if (Constants.isCache) {
                if (inputDialog != null && inputDialog.isShowing()) {
                    inputDialog.cancel();
                    inputDialog.dismiss();
                    selectRow = -1;
                }
                if (resultInfoDialog != null && resultInfoDialog.isShowing()) {
                    resultInfoDialog.cancel();
                    resultInfoDialog.dismiss();
                }
                if (event.getFlCheckAllList() != null && event.getFlCheckAllList().size() > 0) {
                    flCheckAllList.clear();
                    flCheckAllList.addAll(event.getFlCheckAllList());
                    curCheckId = 0;
                    lCheckAllMaterialItem.clear();
                    for (int i = 0; i < flCheckAllList.size(); i++) {
                        FLCheckAll flCheckAll = flCheckAllList.get(i);
                        MaterialItem materialItem = new MaterialItem(flCheckAll.getOrder(), flCheckAll.getBoard_type(), flCheckAll.getLine(),
                                flCheckAll.getSerialNo(), flCheckAll.getAlternative(), flCheckAll.getOrgLineSeat(), flCheckAll.getOrgMaterial(),
                                flCheckAll.getScanLineSeat(), flCheckAll.getScanMaterial(), flCheckAll.getResult(), flCheckAll.getRemark());
                        lCheckAllMaterialItem.add(materialItem);
                        if ((flCheckAll.getResult().equalsIgnoreCase("PASS")) || (flCheckAll.getResult().equalsIgnoreCase("FAIL"))) {
                            if (i == flCheckAllList.size() - 1) {
                                curCheckId = i;
                            } else {
                                curCheckId = i + 1;
                            }

                        }
                    }
                }
                //更新显示
                materialAdapter.notifyDataSetChanged();
                edt_ScanMaterial.requestFocus();
                Log.d(TAG, "mHidden - " + mHidden);
                Log.d(TAG, "isUpdateProgram - " + globalData.isUpdateProgram());
                //提示首检或上料
                if (!mHidden) {
                    checkType = 0;
                    if (factoryLineActivity.updateDialog != null && factoryLineActivity.updateDialog.isShowing()) {
                        factoryLineActivity.updateDialog.cancel();
                        factoryLineActivity.updateDialog.dismiss();
                    }
                    getFCAResultAndIsReseted(1, "", -1);

                }
            }
        }
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
        if (!mHidden) {
            //判断是否首检或上料 todo
            checkType = 0;
            if (factoryLineActivity.updateDialog != null && factoryLineActivity.updateDialog.isShowing()) {
                factoryLineActivity.updateDialog.cancel();
                factoryLineActivity.updateDialog.dismiss();
            }
            getFCAResultAndIsReseted(0, "", -1);
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
        //注销订阅
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_checkAll_order = (TextView) vCheckAllMaterialFragment.findViewById(R.id.tv_checkAll_order);
        TextView edt_Operation = (TextView) vCheckAllMaterialFragment.findViewById(R.id.tv_checkAll_Operation);
        edt_ScanMaterial = (MyEditTextDel) vCheckAllMaterialFragment.findViewById(R.id.edt_material);
        lv_CheckAllMaterial = (ListView) vCheckAllMaterialFragment.findViewById(R.id.checkall_list_view);
        tv_checkAll_order.setText(bundle.getString("orderNum"));
        edt_Operation.setText(bundle.getString("operatorNum"));
        edt_ScanMaterial.requestFocus();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG, "initEvents");
        edt_ScanMaterial.setOnEditorActionListener(this);
        edt_ScanMaterial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    /*
                    //操作员1，ipqc 2 todo
                    if (!globalData.isUpdateProgram()){
                        checkType = 0;
                        if (user_type == 1){
                            getFirstCheckAllResult();
                        }else if (user_type == 2){
                            getFeedResult();
                        }
                    }
                    */
                }
            }
        });
        //长按弹出框
        lv_CheckAllMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int row, long l) {
                //若未扫过不弹出框
                if (isFirstScaned()) {
                    //弹出对话框
                    selectRow = row;
                    showLongClickDialog("请重新扫描新的料号");
                }
                return true;
            }
        });
    }

    //是否扫过了
    private boolean isFirstScaned() {
        boolean firstScaned = false;
        for (MaterialItem materialItem : lCheckAllMaterialItem) {
            if (!(materialItem.getResult().equalsIgnoreCase(""))) {
                firstScaned = true;
                Log.d(TAG, "isFirstScaned::" + true);
                break;
            }
        }
        Log.d(TAG, "isFirstScaned::" + firstScaned);
        return firstScaned;
    }

    //弹出长按对话框
    private void showLongClickDialog(String title) {
        inputDialog = new InputDialog(getActivity(),
                R.layout.input_dialog_layout, new int[]{R.id.input_dialog_title, R.id.et_input}, title);
        inputDialog.show();
        inputDialog.setOnDialogEditorActionListener(new InputDialog.OnDialogEditorActionListener() {
            @Override
            public boolean OnDialogEditorAction(InputDialog inputDialog, final TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == event.KEYCODE_ENTER)) {

                    switch (event.getAction()) {
                        //按下
                        case KeyEvent.ACTION_UP:
                            //先判断是否联网
                            if (globalFunc.isNetWorkConnected()) {
                                if (!first_checkAll_result) {
                                    inputDialog.dismiss();
                                    showInfo("提示", "IPQC未做首次全检", "", 1);
                                } else {
                                    //3 todo
                                    switch (v.getId()) {
                                        case R.id.et_input:
                                            //扫描内容
                                            String scanValue = String.valueOf(((EditText) v).getText());
                                            scanValue = globalFunc.getMaterial(scanValue);
                                            v.setText(scanValue);
                                            checkType = 0;
                                            getFCAResultAndIsReseted(3, scanValue, selectRow);
                                            /*
                                            MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(selectRow);
                                            checkAllMaterialItem.setScanMaterial(scanValue);
                                            //调用全捡方法
                                            checkAllItems(checkAllMaterialItem, selectRow, scanValue);
                                            materialAdapter.notifyDataSetChanged();
                                            //增加数据库日志
                                            new GlobalFunc().AddDBLog(globalData, lCheckAllMaterialItem.get(selectRow));
                                            //判断全是否全部正确
                                            showCheckAllMaterialResult(1);
                                            inputDialog.dismiss();
                                            edt_ScanMaterial.requestFocus();
                                            */
                                            break;
                                    }
                                }
                            } else {
                                globalFunc.showInfo("警告", "请检查网络连接是否正常!", "请连接网络!");
                                v.setText("");
                            }
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
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
        curCheckId = 0;
        lCheckAllMaterialItem.clear();
        if (!isRestoreCache) {
            //不存在缓存
            List<MaterialItem> materialItems = globalData.getMaterialItems();
            for (MaterialItem materialItem : materialItems) {
                MaterialItem feedMaterialItem = new MaterialItem(globalData.getWork_order(), globalData.getBoard_type(),
                        globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(),
                        materialItem.getOrgMaterial(), "", "", "", "");
                lCheckAllMaterialItem.add(feedMaterialItem);

                if (Constants.isCache) {
                    //操作员
                    FLCheckAll flCheckAll = new FLCheckAll(null, globalData.getWork_order(), globalData.getOperator(),
                            globalData.getBoard_type(), globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(),
                            materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
                    flCheckAllList.add(flCheckAll);
                }
            }
            //保存到数据库中
            if (Constants.isCache) {
                //操作员
                boolean cacheResult = new GreenDaoUtil().insertMultiFLCheckMaterial(flCheckAllList);
                Log.d(TAG, "insertMultiFLCheckMaterial - " + cacheResult);
            }
        } else {
            //存在缓存
            for (int i = 0; i < flCheckAllList.size(); i++) {
                FLCheckAll flCheckAll = flCheckAllList.get(i);
                MaterialItem materialItem = new MaterialItem(flCheckAll.getOrder(), flCheckAll.getBoard_type(), flCheckAll.getLine(),
                        flCheckAll.getSerialNo(), flCheckAll.getAlternative(), flCheckAll.getOrgLineSeat(), flCheckAll.getOrgMaterial(),
                        flCheckAll.getScanLineSeat(), flCheckAll.getScanMaterial(), flCheckAll.getResult(), flCheckAll.getRemark());
                lCheckAllMaterialItem.add(materialItem);
                if ((flCheckAll.getResult().equalsIgnoreCase("PASS")) || (flCheckAll.getResult().equalsIgnoreCase("FAIL"))) {
                    if (i == flCheckAllList.size() - 1) {
                        curCheckId = i;
                    } else {
                        curCheckId = i + 1;
                    }
                }
            }
            //todo 需要更新全局变量为本地数据库的
            globalData.setMaterialItems(lCheckAllMaterialItem);
        }

        //设置Adapter
        materialAdapter = new MaterialAdapter(getActivity(), lCheckAllMaterialItem);
        lv_CheckAllMaterial.setAdapter(materialAdapter);
    }

    /**
     * @param textView 　触发的控件
     * @param i
     * @param keyEvent 　key事件
     * @return
     */
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        //回车键
        if (i == EditorInfo.IME_ACTION_SEND ||
                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
            switch (keyEvent.getAction()) {
                //按下
                case KeyEvent.ACTION_UP:
                    //先判断是否联网
                    if (globalFunc.isNetWorkConnected()) {
                        switch (textView.getId()) {
                            case R.id.edt_material:
                                //扫描内容
                                String scanValue = String.valueOf(((EditText) textView).getText());
                                scanValue = scanValue.replaceAll("\r", "");
                                Log.i(TAG, "scan Value:" + scanValue);
                                //料号,若为二维码则提取@@前的料号
                                scanValue = globalFunc.getMaterial(scanValue);
                                textView.setText(scanValue);
                                checkType = 0;
                                getFCAResultAndIsReseted(2, scanValue, curCheckId);
                                break;
                        }
                        /*if (!first_checkAll_result) {
                            showInfo("提示", "IPQC未做首次全检", "", 1);
                        }
                        else if ((user_type == 2) && (!feed_result)){
                            showInfo("提示","操作员未完成上料!","",2);
                        }
                        else {
                            //扫描内容
                            String scanValue = String.valueOf(((EditText) textView).getText());
                            scanValue = scanValue.replaceAll("\r", "");
                            Log.i(TAG, "scan Value:" + scanValue);
                            textView.setText(scanValue);

                            //将扫描的内容更新至列表中
                            MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(curCheckId);

                            switch (textView.getId()) {
                                case R.id.edt_material:
                                    //料号,若为二维码则提取@@前的料号
                                    scanValue = globalFunc.getMaterial(scanValue);
                                    textView.setText(scanValue);
                                    //当前操作的站位
                                    String curCheckLineSeat = checkAllMaterialItem.getOrgLineSeat();
                                    //相同站位的索引数组
                                    ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();
                                    //当前操作的位置
                                    int curOperateIndex = curCheckId;
                                    curCheckId = curCheckId - 1;
                                    *//*
                                    //向上遍历所有相同站位的位置
                                    for (int j = curOperateIndex-1; j >= 0; j--){
                                        if (lCheckAllMaterialItem.get(j).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                            sameLineSeatIndexs.add(j);
                                        }else {
                                            break;
                                        }
                                    }
                                    //向下遍历所有相同的站位位置
                                    for (int k = curOperateIndex; k < lCheckAllMaterialItem.size();k++){
                                        if (lCheckAllMaterialItem.get(k).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                            sameLineSeatIndexs.add(k);
                                            //将检查的位置往后移
                                            curCheckId ++;
                                        }
                                    }
                                    *//*

                                    // TODO: 2018/4/20
                                    //遍历所有相同站位的位置
                                    for (int m = 0; m < lCheckAllMaterialItem.size(); m++) {
                                        if (lCheckAllMaterialItem.get(m).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)) {
                                            sameLineSeatIndexs.add(m);
                                            if (m > curCheckId) {
                                                //将检查的位置往后移
                                                curCheckId++;
                                            }
                                        }
                                    }

                                    //根据站位索引数组检查料号与扫到的料号比对
                                    if (sameLineSeatIndexs.size() == 1) {
                                        //只有一个站位
                                        MaterialItem singleMaterialItem = lCheckAllMaterialItem.get(sameLineSeatIndexs.get(0));
                                        singleMaterialItem.setScanMaterial(scanValue);
                                        if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())) {
                                            singleMaterialItem.setResult("PASS");
                                            singleMaterialItem.setRemark("站位和料号正确");
                                        } else {
                                            singleMaterialItem.setResult("FAIL");
                                            singleMaterialItem.setRemark("料号与站位不对应");
                                        }
                                        //保存本地数据库
                                        cacheCheckResult(sameLineSeatIndexs.get(0), singleMaterialItem);
                                        //更新显示日志
                                        updateVisitLog(singleMaterialItem);
                                    } else if (sameLineSeatIndexs.size() > 1) {
                                        //多个相同的站位,即有替换料
                                        checkMultiItem(sameLineSeatIndexs, scanValue);
                                    }

                                    materialAdapter.notifyDataSetChanged();

                                    //增加数据库日志
                                    globalData.setOperType(Constants.CHECKALLMATERIAL);
                                    // TODO: 2018/4/20 站位有多个料号
                                    globalFunc.AddDBLog(globalData, lCheckAllMaterialItem.get(curOperateIndex));
                                    //清空站位
                                    sameLineSeatIndexs.clear();
                                    //检查下一个料
                                    checkNextMaterial();
                                    break;
                            }
                        }*/
                    } else {
                        globalFunc.showInfo("警告", "请检查网络连接是否正常!", "请连接网络!");
                        clearLineSeatMaterialScan();
                    }
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    private void beginOperate(int checkIndex, String scanValue, int condition) {
        Log.d(TAG, "beginOperate - first_checkAll_result - " + first_checkAll_result);
        Log.d(TAG, "curCheckId - " + curCheckId);
        Log.d(TAG, "checkIndex - " + checkIndex);
        //将扫描的内容更新至列表中
        MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(checkIndex);
        //当前操作的站位
        String curCheckLineSeat = checkAllMaterialItem.getOrgLineSeat();
        //相同站位的索引数组
        ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();

        if (condition == 2) {//正常的全检操作
            //当前操作的位置
//            int curOperateIndex = curCheckId;
            curCheckId = curCheckId - 1;

            //遍历所有相同站位的位置
            for (int m = 0; m < lCheckAllMaterialItem.size(); m++) {
                if (lCheckAllMaterialItem.get(m).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)) {
                    sameLineSeatIndexs.add(m);
                    if (m > curCheckId) {
                        //将检查的位置往后移
                        curCheckId++;
                    }
                }
            }
        } else if (condition == 3) {//长按弹窗的全检操作
            //相同站位的索引数组
            for (int i = 0; i < lCheckAllMaterialItem.size(); i++) {
                if (lCheckAllMaterialItem.get(i).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)) {
                    sameLineSeatIndexs.add(i);
                }
            }
        }

        //根据站位索引数组检查料号与扫到的料号比对
        if (sameLineSeatIndexs.size() == 1) {
            //只有一个站位
            MaterialItem singleMaterialItem = lCheckAllMaterialItem.get(sameLineSeatIndexs.get(0));
            singleMaterialItem.setScanMaterial(scanValue);
            if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())) {
                singleMaterialItem.setResult("PASS");
                singleMaterialItem.setRemark("站位和料号正确");
            } else {
                singleMaterialItem.setResult("FAIL");
                singleMaterialItem.setRemark("料号与站位不对应");
            }
            //保存本地数据库
            cacheCheckResult(sameLineSeatIndexs.get(0), singleMaterialItem);
            //更新显示日志
            updateVisitLog(singleMaterialItem);
        } else if (sameLineSeatIndexs.size() > 1) {
            //多个相同的站位,即有替换料
            checkMultiItem(sameLineSeatIndexs, scanValue);
        }

        materialAdapter.notifyDataSetChanged();

        //增加数据库日志
        globalData.setOperType(Constants.CHECKALLMATERIAL);
        globalFunc.AddDBLog(globalData, lCheckAllMaterialItem.get(checkIndex));

        //清空站位
        sameLineSeatIndexs.clear();
        if (condition == 2) {//正常的全检操作
            //检查下一个料
            checkNextMaterial();
        } else if (condition == 3) {//长按弹窗的全检操作
            //判断全是否全部正确
            showCheckAllMaterialResult(1);
            if (inputDialog != null && inputDialog.isShowing()) {
                inputDialog.dismiss();
            }
            edt_ScanMaterial.requestFocus();
        }
    }

    private void checkMultiItem(ArrayList<Integer> integers, String mScanValue) {
        //多个相同的站位,即有替换料
        boolean result = false;
        for (int i = 0; i < integers.size(); i++) {
            MaterialItem multiMaterialItem = lCheckAllMaterialItem.get(integers.get(i));
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())) {
                result = true;
            }
        }
        if (result) {
            for (int j = 0; j < integers.size(); j++) {
                MaterialItem innerMaterialItem = lCheckAllMaterialItem.get(integers.get(j));
                innerMaterialItem.setResult("PASS");
                innerMaterialItem.setRemark("主替有一项成功");
                //保存本地数据库
                cacheCheckResult(integers.get(j), innerMaterialItem);
                //更新显示日志
                updateVisitLog(innerMaterialItem);
            }
        } else {
            for (int j = 0; j < integers.size(); j++) {
                MaterialItem innerMaterialItem = lCheckAllMaterialItem.get(integers.get(j));
                innerMaterialItem.setResult("FAIL");
                innerMaterialItem.setRemark("料号与站位不对应");
                //保存本地数据库
                cacheCheckResult(integers.get(j), innerMaterialItem);
                //更新显示日志
                updateVisitLog(innerMaterialItem);
            }
        }
    }

    private void checkAllItems(MaterialItem materialItem, int curCheckIndex, String mScanValue) {
        //当前操作的站位
        String curCheckLineSeat = materialItem.getOrgLineSeat();
        //相同站位的索引数组
        ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();
        // TODO: 2018/4/20
        //遍历所有相同站位的位置
        for (int i = 0; i < lCheckAllMaterialItem.size(); i++) {
            if (lCheckAllMaterialItem.get(i).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)) {
                sameLineSeatIndexs.add(i);
            }
        }
        //根据站位索引数组检查料号与扫到的料号比对
        if (sameLineSeatIndexs.size() == 1) {
            //只有一个站位
            MaterialItem singleMaterialItem = lCheckAllMaterialItem.get(sameLineSeatIndexs.get(0));
            singleMaterialItem.setScanMaterial(mScanValue);
            if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())) {
                singleMaterialItem.setResult("PASS");
                singleMaterialItem.setRemark("站位和料号正确");
            } else {
                singleMaterialItem.setResult("FAIL");
                singleMaterialItem.setRemark("料号与站位不对应");
            }
            //保存本地数据库
            cacheCheckResult(sameLineSeatIndexs.get(0), singleMaterialItem);
            //更新显示日志
            updateVisitLog(singleMaterialItem);
        } else if (sameLineSeatIndexs.size() > 1) {
            //多个相同的站位,即有替换料
            checkMultiItem(sameLineSeatIndexs, mScanValue);
        }

    }

    //更新上料缓存
    private void cacheCheckResult(int index, MaterialItem materialItem) {
        if (Constants.isCache) {
            //保存缓存
            FLCheckAll flCheckAll = flCheckAllList.get(index);
            flCheckAll.setScanLineSeat(materialItem.getScanLineSeat());
            flCheckAll.setScanMaterial(materialItem.getScanMaterial());
            flCheckAll.setResult(materialItem.getResult());
            flCheckAll.setRemark(materialItem.getRemark());
            new GreenDaoUtil().updateFLCheck(flCheckAll);
        }
    }

    // TODO: 2018/4/27
    //更新显示日志
    private void updateVisitLog(MaterialItem materialItem) {
        Log.d(TAG, "updateVisitLog-first_checkAll_result-" + first_checkAll_result);
        globalData.setUpdateType(Constants.CHECKALLMATERIAL);
//        globalFunc.updateVisitLog(globalData, materialItem);
        HttpUtils.getHttpUtils().operate(materialItem, globalData.getUpdateType());
    }

    //判断是否全部进行了首次全检
    private void getFCAResultAndIsReseted(int condition, String scanValue, int checkIndex) {
        Log.d(TAG, "getFCAResultAndIsReseted - " + condition);
        //判断工位检测功能是否打开
        if (Constants.isCheckWorkType) {
            //工位检测功能打开
            if (!first_checkAll_result) {
                loadingDialog = new LoadingDialog(getActivity(), "正在加载...");
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    */
                    //检测是否首检
                    boolean firstResult = new DBService().isOrderFirstCheckAll(globalData.getLine(),
                            globalData.getWork_order(), globalData.getBoard_type());
                    first_checkAll_result = firstResult;
                    //检测是否重置
                    int checkReset = new DBService().isProgramVisitReseted(globalData.getLine(), globalData.getWork_order(),
                            globalData.getBoard_type(), Constants.CHECKALLMATERIAL);
                    //本地数据
                    boolean reseted = true;
                    for (MaterialItem materialItem : lCheckAllMaterialItem) {
                        if (!materialItem.getResult().equalsIgnoreCase("")) {
                            reseted = false;
                        }
                    }
                    Message message = Message.obtain();
                    message.obj = scanValue;
                    message.arg1 = condition;
                    message.arg2 = checkIndex;
                    Log.d(TAG, "getFCAResultAndIsReseted - " + firstResult);
                    Log.d(TAG, "getFCAResultAndIsReseted - checkReset - " + checkReset);
                    Log.d(TAG, "getFCAResultAndIsReseted - reseted - " + reseted);
                    if (condition == 0) {
                        if ((!firstResult)) {
                            message.what = FIRST_CHECKALL_FALSE;
                            if ((checkReset == 1) && (!reseted)) {
                                message.what = RESET_SHOWTIP;
                            }
                        }
                    } else if (condition == 1) {
                        message.what = PROGRAM_UPDATE;
                    } else if (condition == 2 || condition == 3) {
                        if (firstResult) {
                            if (checkReset == 0) {
                                //未重置,操作
                                message.what = OPERATING;
                            } else if (checkReset == 1) {
                                //重置
                                if (!reseted) {
                                    message.what = RESET;
                                } else {
                                    //操作
                                    message.what = OPERATING;
                                }
                            }
                        } else {
                            message.what = FIRST_CHECKALL_FALSE;
                            if ((checkReset == 1) && (!reseted)) {
                                message.what = RESET_SHOWTIP;
                            }
                        }
                    }
                    checkAllHandler.sendMessage(message);
                }
            }).start();
        } else {
            //工位检测功能未打开
            first_checkAll_result = true;
        }
    }

    //IPQC未做首次全检
    private void showInfo(String title, String message, String tip, final int userType) {
        /*
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.cancel();
            loadingDialog.dismiss();
        }
        */
        //对话框所有控件id
        int itemResIds[] = new int[]{R.id.dialog_title_view,
                R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust, R.id.tv_alert_msg};
        //标题和内容
        String titleMsg[];
        //内容的样式
        int msgStype[];

        if (userType == 3) {
            msgStype = new int[]{22, Color.argb(255, 102, 153, 0)};
            titleMsg = new String[]{title, message};
        } else {
            msgStype = new int[]{22, Color.RED, Color.argb(255, 219, 201, 36)};
            titleMsg = new String[]{title, message, tip};
        }

        InfoDialog infoDialog = new InfoDialog(getActivity(),
                R.layout.info_dialog_layout, itemResIds, titleMsg, msgStype);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.info_trust:
                        dialog.dismiss();
                        /*
                        if (loadingDialog != null && loadingDialog.isShowing()){
                            loadingDialog.cancel();
                            loadingDialog.dismiss();
                        }
                        */
                        clearLineSeatMaterialScan();
                        checkType = 1;
                        //操作员1，ipqc 2
//                        if (userType == 1){
//                        getFCAResultAndIsReseted(0);
//                        }
                        /*else if (userType == 2){
                            getFeedResult(0);
                        }*/
                        break;
                }
            }
        });
        infoDialog.show();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 上下一个料
     */
    private void checkNextMaterial() {
        lv_CheckAllMaterial.setSelection(curCheckId);
        if (curCheckId < lCheckAllMaterialItem.size() - 1) {
            curCheckId++;
            clearLineSeatMaterialScan();
        } else {
            showCheckAllMaterialResult(0);
        }
        Log.i(TAG, "checkNextMaterial:" + curCheckId);
    }

    private void showCheckAllMaterialResult(int showType) {
        boolean checkResult = true;
        String[] titleMsg;
        int[] msgStyle;
        for (MaterialItem checkMaterialItem : lCheckAllMaterialItem) {
            if (!(checkMaterialItem.getResult().equals("PASS"))) {
                checkResult = false;
                break;
            }
        }
        if ((showType == 0) || (checkResult && (showType == 1))) {

            if (checkResult) {
                titleMsg = new String[]{"全检完成", "PASS"};
                msgStyle = new int[]{66, Color.argb(255, 102, 153, 0)};
            } else {
                titleMsg = new String[]{"全检失败,请检查!", "FAIL"};
                msgStyle = new int[]{66, Color.RED};
            }
            showResultInfo(titleMsg, msgStyle, checkResult);
        }
    }


    //弹出消息窗口
    private boolean showResultInfo(String[] titleMsg, int[] msgStyle, final boolean result) {
        /*
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.cancel();
            loadingDialog.dismiss();
        }
        */
        //对话框所有控件id
        int itemResIds[] = new int[]{R.id.dialog_title_view,
                R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust};

        resultInfoDialog = new InfoDialog(getActivity(),
                R.layout.info_dialog_layout, itemResIds, titleMsg, msgStyle);

        resultInfoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.info_trust:
                        // TODO: 2018/4/12
                        //获取全检结果
//                        checkType = 1;
//                        getFCAResultAndIsReseted(0);
                        dialog.dismiss();
                        if (result) {
//                            setFirst_checkAll_result(true);
//                            first_checkAll_result = true;
                            //清除本地数据库全检纪录
//                            if (user_type == 1){

//                            }
                            /*else if (user_type == 2){
                                boolean result = new GreenDaoUtil().updateAllQcCheck(qcCheckAllList);
                                Log.d(TAG,"updateAllQcCheck - "+result);
                            }*/
                            clearMaterialInfo();
                        } else {
                            edt_ScanMaterial.setText("");
                            edt_ScanMaterial.requestFocus();
                        }
                        break;
                }
            }
        });
        resultInfoDialog.show();

        return true;
    }


    /**
     * @method resetTestPar
     * @author connie
     * @time 2017-9-26
     * @describe 清空之前的全检信息进入下一轮全检
     */
    private void clearMaterialInfo() {
        Log.d(TAG, "clearMaterialInfo");
        clearLineSeatMaterialScan();
        curCheckId = 0;
        clearCheckAllDisplay();
        boolean result = new GreenDaoUtil().updateAllFLCheck(flCheckAllList);
        Log.d(TAG, "updateAllFLCheck - " + result);
    }

    //清除全检结果
    private void clearCheckAllDisplay() {
        for (int i = 0; i < lCheckAllMaterialItem.size(); i++) {
            MaterialItem materialItem = lCheckAllMaterialItem.get(i);
            materialItem.setScanLineSeat("");
            materialItem.setScanMaterial("");
            materialItem.setResult("");
            materialItem.setRemark("");
            if (Constants.isCache) {
//                if (user_type == 1){
                FLCheckAll flCheckAll = flCheckAllList.get(i);
                flCheckAll.setScanLineSeat("");
                flCheckAll.setScanMaterial("");
                flCheckAll.setResult("");
                flCheckAll.setRemark("");
//                }
                /*else if (user_type == 2){
                    QcCheckAll qcCheckAll = qcCheckAllList.get(i);
                    qcCheckAll.setScanLineSeat("");
                    qcCheckAll.setScanMaterial("");
                    qcCheckAll.setResult("");
                    qcCheckAll.setRemark("");
                }*/
            }
        }
        materialAdapter.notifyDataSetChanged();
    }

    private void clearLineSeatMaterialScan() {
        edt_ScanMaterial.setText("");
        edt_ScanMaterial.requestFocus();
    }

    private boolean isFeed_result() {
        return feed_result;
    }

    private void setFeed_result(boolean feed_result) {
        this.feed_result = feed_result;
    }

    private boolean isFirst_checkAll_result() {
        return first_checkAll_result;
    }

    private void setFirst_checkAll_result(boolean first_checkAll_result) {
        this.first_checkAll_result = first_checkAll_result;
    }
}
