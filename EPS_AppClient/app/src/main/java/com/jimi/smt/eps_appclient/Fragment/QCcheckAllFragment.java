package com.jimi.smt.eps_appclient.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.jimi.smt.eps_appclient.Activity.QCActivity;
import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
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

public class QCcheckAllFragment extends Fragment implements TextView.OnEditorActionListener {

    private final String TAG = this.getClass().getSimpleName();
    private View qcCheckAllView;
    private MyEditTextDel edt_ScanMaterial;
    private ListView lv_qcCheckAll;
    private List<MaterialItem> qcCheckAllMaterials = new ArrayList<MaterialItem>();
    //IPQC全检纪录
    private List<QcCheckAll> qcCheckAllList = new ArrayList<QcCheckAll>();
    //长按时选择的行
    private int selectRow = -1;
    private int checkType = 0;
    private GlobalData globalData;
    private GlobalFunc globalFunc;
    private int user_type;
    private QCActivity mQcActivity;
    private boolean isRestoreCache = false;
    private boolean first_checkAll_result = false;
    private boolean feed_result;
    private static final int PROGRAM_UPDATE = 302;//站位表更新
    private static final int FEED_TRUE = 303;//已上
    private static final int FEED_FALSE = 304;//未上
    private static final int FEED_TRUE_OPERATING = 307;//扫料号的时候检查已上
    private static final int FIRST_CHECKALL_TRUE = 305;//首对
    private static final int FIRST_CHECKALL_FALSE = 306;//首错
    private static final int OPERATING = 308;
    private static final int RESET = 309;
    private int curCheckId;
    private MaterialAdapter materialAdapter;
    private InfoDialog resultInfoDialog;
    private InputDialog inputDialog;
    private boolean mHidden;
    private LoadingDialog loadingDialog;

    @SuppressLint("HandlerLeak")
    private Handler qcCheckHandler = new Handler() {
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
                    if (checkType == 1) {
                        showInfo("站位表更新!", "操作员未完成上料!", 2, 0);
                    }
                    break;

                case FEED_TRUE://已上
                    getFirstCheckAllResult(0);
                    break;

                case FEED_FALSE://未上
                    edt_ScanMaterial.setText("");
                    if (checkType == 0) {
                        showInfo("操作员未完成上料!", "", 2, 0);
                    }
                    //检测是否重置,但不操作
                    isResteted(-1, "", 0);
                    break;

                case FIRST_CHECKALL_TRUE://已首
                    break;

                case FIRST_CHECKALL_FALSE://未首
//                    if (checkType == 0){
                    showInfo("将进行首次全检", "", 3, 5);
//                    }
                    break;
                case FEED_TRUE_OPERATING://扫料号的时候检查已上
                    //检测是否重置,同时操作
                    isResteted(msg.arg2, (String) msg.obj, msg.arg1);
                    break;
                case OPERATING://操作
                    beginOperate(msg.arg2, (String) msg.obj, msg.arg1);
                    Log.d(TAG, "isResteted - 未重置");
                    break;
                case RESET://重置
                    clearMaterialInfo();
                    Log.d(TAG, "isResteted - 重置了");
                    break;

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册订阅
        EventBus.getDefault().register(this);
        globalData = (GlobalData) getActivity().getApplication();
        globalFunc = new GlobalFunc(globalData);
        user_type = globalData.getUserType();
        mQcActivity = (QCActivity) getActivity();
        if (Constants.isCache) {
            //查询本地数据库是否存在缓存
            List<QcCheckAll> qcCheckAlls = new GreenDaoUtil().queryQcCheckRecord(globalData.getOperator(), globalData.getWork_order(), globalData.getLine(), globalData.getBoard_type());
            if (qcCheckAlls != null && qcCheckAlls.size() > 0) {
                qcCheckAllList.addAll(qcCheckAlls);
                isRestoreCache = true;
            } else {
                boolean result = new GreenDaoUtil().deleteAllQcCheck();
                Log.d(TAG, "deleteAllQcCheck - " + result);
                isRestoreCache = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        qcCheckAllView = inflater.inflate(R.layout.checkallmaterial_layout, container, false);
        Intent intent = getActivity().getIntent();
        savedInstanceState = intent.getExtras();
        if (mQcActivity.updateDialog != null && mQcActivity.updateDialog.isShowing()) {
            mQcActivity.updateDialog.cancel();
            mQcActivity.updateDialog.dismiss();
        }
        getFeedResult(0, "", -1);
        initView(savedInstanceState);
        initData();
        return qcCheckAllView;
    }

    //监听订阅的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenBusTest event) {
        Log.d(TAG, "onEventMainThread - update - " + event.getUpdated());
        if (Constants.isCache) {
            if (event.getUpdated() == 0) {
                //更新或重置
                if (inputDialog != null && inputDialog.isShowing()) {
                    inputDialog.cancel();
                    inputDialog.dismiss();
                    selectRow = -1;
                }
                if (resultInfoDialog != null && resultInfoDialog.isShowing()) {
                    resultInfoDialog.cancel();
                    resultInfoDialog.dismiss();
                }
                if (event.getQcCheckAllList() != null && event.getQcCheckAllList().size() > 0) {
                    qcCheckAllList.clear();
                    qcCheckAllList.addAll(event.getQcCheckAllList());
                    curCheckId = 0;
                    qcCheckAllMaterials.clear();
                    for (int i = 0; i < qcCheckAllList.size(); i++) {
                        QcCheckAll qcCheckAll = qcCheckAllList.get(i);
                        MaterialItem materialItem = new MaterialItem(qcCheckAll.getOrder(), qcCheckAll.getBoard_type(), qcCheckAll.getLine(),
                                qcCheckAll.getSerialNo(), qcCheckAll.getAlternative(), qcCheckAll.getOrgLineSeat(), qcCheckAll.getOrgMaterial(),
                                qcCheckAll.getScanLineSeat(), qcCheckAll.getScanMaterial(), qcCheckAll.getResult(), qcCheckAll.getRemark());
                        qcCheckAllMaterials.add(materialItem);
                        if ((qcCheckAll.getResult().equalsIgnoreCase("PASS")) || (qcCheckAll.getResult().equalsIgnoreCase("FAIL"))) {
                            if (i == qcCheckAllList.size() - 1) {
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
                //提示首检或上料
                if (!mHidden) {
                    checkType = 0;
                    if (mQcActivity.updateDialog != null && mQcActivity.updateDialog.isShowing()) {
                        mQcActivity.updateDialog.cancel();
                        mQcActivity.updateDialog.dismiss();
                    }
                    getFeedResult(1, "", -1);
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "onHiddenChanged-" + hidden);
        this.mHidden = hidden;
        if (!hidden) {
            //判断是否上料
            checkType = 0;
            /*if (user_type == 1){
                if (factoryLineActivity.updateDialog != null && factoryLineActivity.updateDialog.isShowing()){
                    factoryLineActivity.updateDialog.cancel();
                    factoryLineActivity.updateDialog.dismiss();
                }
                getFirstCheckAllResult(0);
            }else */
//            if (user_type == 2){
            if (mQcActivity.updateDialog != null && mQcActivity.updateDialog.isShowing()) {
                mQcActivity.updateDialog.cancel();
                mQcActivity.updateDialog.dismiss();
            }
            getFeedResult(0, "", -1);
//                getFirstCheckAllResult(0);
//            }

        }
    }

    private void initView(Bundle bundle) {
        TextView tv_checkAll_Operation = qcCheckAllView.findViewById(R.id.tv_checkAll_Operation);
        tv_checkAll_Operation.setText(bundle.getString("operatorNum"));
        TextView tv_checkAll_order = qcCheckAllView.findViewById(R.id.tv_checkAll_order);
        tv_checkAll_order.setText(bundle.getString("orderNum"));
        edt_ScanMaterial = qcCheckAllView.findViewById(R.id.edt_material);
        lv_qcCheckAll = qcCheckAllView.findViewById(R.id.checkall_list_view);
        edt_ScanMaterial.requestFocus();
        edt_ScanMaterial.setOnEditorActionListener(this);
        //长按弹出框
        lv_qcCheckAll.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        for (MaterialItem materialItem : qcCheckAllMaterials) {
            if (!(materialItem.getResult().equalsIgnoreCase(""))) {
                firstScaned = true;
                break;
            }
        }
        return firstScaned;
    }

    private void initData() {
        Log.i(TAG, "initData");
        //填充数据
        curCheckId = 0;
        qcCheckAllMaterials.clear();
        if (!isRestoreCache) {
            //不存在缓存
            List<MaterialItem> materialItems = globalData.getMaterialItems();
            for (MaterialItem materialItem : materialItems) {
                MaterialItem feedMaterialItem = new MaterialItem(globalData.getWork_order(), globalData.getBoard_type(),
                        globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(),
                        materialItem.getOrgMaterial(), "", "", "", "");
                qcCheckAllMaterials.add(feedMaterialItem);

                if (Constants.isCache) {
                    QcCheckAll qcCheckAll = new QcCheckAll(null, globalData.getWork_order(), globalData.getOperator(),
                            globalData.getBoard_type(), globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(),
                            materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
                    qcCheckAllList.add(qcCheckAll);

                }
            }
            //保存到数据库中
            if (Constants.isCache) {
                boolean cacheResult = new GreenDaoUtil().insertMultiQcCheckMaterial(qcCheckAllList);
                Log.d(TAG, "insertMultiQcCheckMaterial - " + cacheResult);
            }
        } else {
            //存在缓存
            for (int i = 0; i < qcCheckAllList.size(); i++) {
                QcCheckAll qcCheckAll = qcCheckAllList.get(i);
                MaterialItem materialItem = new MaterialItem(qcCheckAll.getOrder(), qcCheckAll.getBoard_type(), qcCheckAll.getLine(),
                        qcCheckAll.getSerialNo(), qcCheckAll.getAlternative(), qcCheckAll.getOrgLineSeat(), qcCheckAll.getOrgMaterial(),
                        qcCheckAll.getScanLineSeat(), qcCheckAll.getScanMaterial(), qcCheckAll.getResult(), qcCheckAll.getRemark());
                qcCheckAllMaterials.add(materialItem);
                if ((qcCheckAll.getResult().equalsIgnoreCase("PASS")) || (qcCheckAll.getResult().equalsIgnoreCase("FAIL"))) {
                    if (i == qcCheckAllList.size() - 1) {
                        curCheckId = i;
                    } else {
                        curCheckId = i + 1;
                    }
                }
            }

            //todo 需要更新全局变量为本地数据库的
            globalData.setMaterialItems(qcCheckAllMaterials);
        }

        //设置Adapter
        materialAdapter = new MaterialAdapter(getActivity(), qcCheckAllMaterials);
        lv_qcCheckAll.setAdapter(materialAdapter);
    }

    //判断是否全部上料
    private void getFeedResult(int condition, String scanValue, int checkIndex) {
        Log.d(TAG, "getFeedResult - " + condition);
        //判断工位检测功能是否打开
        if (Constants.isCheckWorkType) {
//            if (!feed_result){
            loadingDialog = new LoadingDialog(getActivity(), "正在加载...");
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
//            }
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
                    boolean feedResult = new DBService().isFeeded(globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type());
                    Message message = Message.obtain();
                    feed_result = feedResult;
                    message.arg1 = condition;
                    if (condition == 0) {
                        if (feedResult) {
                            message.what = FEED_TRUE;
                        } else {
                            message.what = FEED_FALSE;
                        }
                    } else if (condition == 1) {
                        if (!feedResult) {
                            message.what = PROGRAM_UPDATE;
                        }
                    } else if (condition == 2 || condition == 3) {
                        if (feedResult) {
                            message.what = FEED_TRUE_OPERATING;
                            message.arg2 = checkIndex;
                            message.obj = scanValue;
                        } else {
                            message.what = FEED_FALSE;
                        }
                    }

                    qcCheckHandler.sendMessage(message);
                }
            }).start();
        } else {
            //工位检测功能未打开
            feed_result = true;
            Message message = Message.obtain();
            message.what = FEED_TRUE;
            qcCheckHandler.sendMessage(message);
        }
    }

    //判断是否全部进行了首次全检
    private void getFirstCheckAllResult(int condition) {
        Log.d(TAG, "getFirstCheckAllResult");
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
                    boolean firstResult = new DBService().isOrderFirstCheckAll(globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type());
                    Message message = Message.obtain();
                    first_checkAll_result = firstResult;
/*

                    if (condition == 0){
                        if (firstResult){
//                        first_checkAll_result = true;
//                        message.what = FIRST_CHECKALL_TRUE;
                        }else {
//                        first_checkAll_result = false;
                            message.what = FIRST_CHECKALL_FALSE;
                        }
                    }else if (condition == 1){
                        message.what = PROGRAM_UPDATE;
                    }
*/

                    if (!firstResult) {
                        if (condition == 0) {
                            message.what = FIRST_CHECKALL_FALSE;
                        } else if (condition == 1) {
                            message.what = PROGRAM_UPDATE;
                        }
                    }

                    qcCheckHandler.sendMessage(message);
                }
            }).start();
        } else {
            //工位检测功能未打开
            first_checkAll_result = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        //注销订阅
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
                                // TODO: 2018/4/23
                                if (!feed_result) {
                                    inputDialog.dismiss();
                                    showInfo("操作员未完成上料!", "", 2, 0);
                                } else {
                                    switch (v.getId()) {
                                        case R.id.et_input:
                                            //扫描内容
                                            String scanValue = String.valueOf(((EditText) v).getText());
                                            scanValue = globalFunc.getMaterial(scanValue);
                                            v.setText(scanValue);
                                            checkType = 0;
                                            getFeedResult(3, scanValue, selectRow);

                                            // TODO: 2018/4/23
                                            /*
                                            MaterialItem checkAllMaterialItem = qcCheckAllMaterials.get(selectRow);
                                            checkAllMaterialItem.setScanMaterial(scanValue);
                                            //调用全捡方法
                                            checkAllItems(checkAllMaterialItem,selectRow,scanValue);
                                            materialAdapter.notifyDataSetChanged();
                                            //增加数据库日志
                                            new GlobalFunc().AddDBLog(globalData, qcCheckAllMaterials.get(selectRow));
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

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        //回车键
        if (i == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
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
                                getFeedResult(2, scanValue, curCheckId);
                                break;
                        }

                        /*
                        if (!feed_result){
                            showInfo("操作员未完成上料!","",2,0);
                        }
                        else {
                            //扫描内容
                            String scanValue = String.valueOf(((EditText) textView).getText());
                            scanValue = scanValue.replaceAll("\r", "");
                            Log.i(TAG, "scan Value:" + scanValue);
                            textView.setText(scanValue);

                            Log.d(TAG,"curCheckId - "+curCheckId);
                            //将扫描的内容更新至列表中
                            MaterialItem checkAllMaterialItem = qcCheckAllMaterials.get(curCheckId);

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

                                    //遍历所有相同站位的位置
                                    for (int m = 0; m < qcCheckAllMaterials.size(); m++){
                                        if (qcCheckAllMaterials.get(m).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                            sameLineSeatIndexs.add(m);
                                            if (m > curCheckId){
                                                //将检查的位置往后移
                                                curCheckId ++;
                                            }
                                        }
                                    }

                                    *//*
                                    //向上遍历所有相同站位的位置
                                    for (int j = curOperateIndex-1; j >= 0; j--){
                                        if (qcCheckAllMaterials.get(j).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                            sameLineSeatIndexs.add(j);
                                        }else {
                                            break;
                                        }
                                    }
                                    //向下遍历所有相同的站位位置
                                    for (int k = curOperateIndex; k < qcCheckAllMaterials.size();k++){
                                        if (qcCheckAllMaterials.get(k).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                            sameLineSeatIndexs.add(k);
                                            //将检查的位置往后移
                                            curCheckId ++;
                                        }
                                    }
                                    *//*

                                    //根据站位索引数组检查料号与扫到的料号比对
                                    if (sameLineSeatIndexs.size() == 1){
                                        //只有一个站位
                                        MaterialItem singleMaterialItem = qcCheckAllMaterials.get(sameLineSeatIndexs.get(0));
                                        singleMaterialItem.setScanMaterial(scanValue);
                                        if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())){
                                            singleMaterialItem.setResult("PASS");
                                            singleMaterialItem.setRemark("站位和料号正确");
                                        }else {
                                            singleMaterialItem.setResult("FAIL");
                                            singleMaterialItem.setRemark("料号与站位不对应");
                                        }
                                        //保存本地数据库
                                        cacheCheckResult(sameLineSeatIndexs.get(0),singleMaterialItem);
                                        //更新显示日志
                                        updateVisitLog(singleMaterialItem);
                                    }else if (sameLineSeatIndexs.size() > 1){
                                        //多个相同的站位,即有替换料
                                        checkMultiItem(sameLineSeatIndexs,scanValue);
                                    }

                                    materialAdapter.notifyDataSetChanged();

                                    //增加数据库日志
                                    if (first_checkAll_result){
                                        //全检
                                        globalData.setOperType(Constants.CHECKALLMATERIAL);
                                    }else {
                                        //首检
                                        globalData.setOperType(Constants.FIRST_CHECK_ALL);
                                    }
                                    globalFunc.AddDBLog(globalData, qcCheckAllMaterials.get(curOperateIndex));

                                    //清空站位
                                    sameLineSeatIndexs.clear();
                                    //检查下一个料
                                    checkNextMaterial();
                                    break;


                            }
                        }
                        */

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
        /*int checkReset;
        if (first_checkAll_result){
            checkReset = DBService.getDbService().isProgramVisitReseted(globalData.getLine(),globalData.getWork_order(),
                    globalData.getBoard_type(),Constants.CHECKALLMATERIAL);

        }else {
            checkReset = DBService.getDbService().isProgramVisitReseted(globalData.getLine(),globalData.getWork_order(),
                    globalData.getBoard_type(),Constants.FIRST_CHECK_ALL);
        }
        if (checkReset == 1){
            //重置了
            clearMaterialInfo();
            Log.d(TAG,"beginOperate - 重置了");
        }else {}*/
//        int reseted = isResteted();
//        if (reseted == 0){
        //未重置
        Log.d(TAG, "curCheckId - " + curCheckId);
        Log.d(TAG, "checkIndex - " + checkIndex);
        //将扫描的内容更新至列表中
        MaterialItem checkAllMaterialItem = qcCheckAllMaterials.get(checkIndex);
        //当前操作的站位
        String curCheckLineSeat = checkAllMaterialItem.getOrgLineSeat();
        //相同站位的索引数组
        ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();

        if (condition == 2) {//正常的全检操作
            //当前操作的位置
//            int curOperateIndex = curCheckId;
            curCheckId = curCheckId - 1;

            //遍历所有相同站位的位置
            for (int m = 0; m < qcCheckAllMaterials.size(); m++) {
                if (qcCheckAllMaterials.get(m).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)) {
                    sameLineSeatIndexs.add(m);
                    if (m > curCheckId) {
                        //将检查的位置往后移
                        curCheckId++;
                    }
                }
            }
        } else if (condition == 3) {//长按弹窗的全检操作
            //相同站位的索引数组
            for (int i = 0; i < qcCheckAllMaterials.size(); i++) {
                if (qcCheckAllMaterials.get(i).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)) {
                    sameLineSeatIndexs.add(i);
                }
            }
        }

        //根据站位索引数组检查料号与扫到的料号比对
        if (sameLineSeatIndexs.size() == 1) {
            //只有一个站位
            MaterialItem singleMaterialItem = qcCheckAllMaterials.get(sameLineSeatIndexs.get(0));
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
        if (first_checkAll_result) {
            //全检
            globalData.setOperType(Constants.CHECKALLMATERIAL);
        } else {
            //首检
            globalData.setOperType(Constants.FIRST_CHECK_ALL);
        }
        globalFunc.AddDBLog(globalData, qcCheckAllMaterials.get(checkIndex));

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
//        }

    }


    private void checkAllItems(MaterialItem materialItem, int curCheckIndex, String mScanValue) {
        //当前操作的站位
        String curCheckLineSeat = materialItem.getOrgLineSeat();
        //相同站位的索引数组
        ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();
        for (int i = 0; i < qcCheckAllMaterials.size(); i++) {
            if (qcCheckAllMaterials.get(i).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)) {
                sameLineSeatIndexs.add(i);
            }
        }
        /*
        //向上遍历所有相同站位的位置
        for (int j = curCheckIndex -1; j >= 0; j--){
            if (qcCheckAllMaterials.get(j).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                sameLineSeatIndexs.add(j);
            }else {
                break;
            }
        }
        //向下遍历所有相同的站位位置
        for (int k = curCheckIndex; k < qcCheckAllMaterials.size(); k++){
            if (qcCheckAllMaterials.get(k).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                sameLineSeatIndexs.add(k);
            }
        }
        */

        //根据站位索引数组检查料号与扫到的料号比对
        if (sameLineSeatIndexs.size() == 1) {
            //只有一个站位
            MaterialItem singleMaterialItem = qcCheckAllMaterials.get(sameLineSeatIndexs.get(0));
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

    //工单是否重置了
    private void isResteted(int checkIndex, String scanValue, int condition) {
        Log.d(TAG, "isResteted - " + condition);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int checkReset;
                if (first_checkAll_result) {
                    checkReset = new DBService().isProgramVisitReseted(globalData.getLine(), globalData.getWork_order(),
                            globalData.getBoard_type(), Constants.CHECKALLMATERIAL);

                } else {
                    checkReset = new DBService().isProgramVisitReseted(globalData.getLine(), globalData.getWork_order(),
                            globalData.getBoard_type(), Constants.FIRST_CHECK_ALL);
                }
                Log.d(TAG, "checkReset - " + checkReset);
                boolean reseted = true;
                for (MaterialItem materialItem : qcCheckAllMaterials) {
                    if (!materialItem.getResult().equalsIgnoreCase("")) {
                        reseted = false;
                    }
                }
                Message message = Message.obtain();
                if (checkReset == 1 && !reseted) {
                    //重置了
                    message.what = RESET;
                } else {
                    if (condition == 2 || condition == 3) {
                        message.what = OPERATING;
                        message.obj = scanValue;
                        message.arg1 = condition;
                        message.arg2 = checkIndex;
                    }
                }
                qcCheckHandler.sendMessage(message);
            }
        }).start();
    }

    //更新上料缓存
    private void cacheCheckResult(int index, MaterialItem materialItem) {
        if (Constants.isCache) {
            //保存缓存
            QcCheckAll qcCheckAll = qcCheckAllList.get(index);
            qcCheckAll.setScanLineSeat(materialItem.getScanLineSeat());
            qcCheckAll.setScanMaterial(materialItem.getScanMaterial());
            qcCheckAll.setResult(materialItem.getResult());
            qcCheckAll.setRemark(materialItem.getRemark());
            new GreenDaoUtil().updateQcCheck(qcCheckAll);
        }
    }

    // TODO: 2018/4/27
    //更新显示日志
    private void updateVisitLog(MaterialItem materialItem) {
        Log.d(TAG, "updateVisitLog-first_checkAll_result-" + first_checkAll_result);
        if (materialItem != null) {
            if (first_checkAll_result) {
                //非首次全检
                globalData.setUpdateType(Constants.CHECKALLMATERIAL);
                HttpUtils.getHttpUtils().operate(materialItem, globalData.getUpdateType());
            } else {
                //首次全检
                globalData.setUpdateType(Constants.FIRST_CHECK_ALL);
                globalFunc.updateVisitLog(globalData, materialItem);
            }
        }
    }

    private void checkMultiItem(ArrayList<Integer> integers, String mScanValue) {
        //多个相同的站位,即有替换料
        boolean result = false;
        for (int i = 0; i < integers.size(); i++) {
            MaterialItem multiMaterialItem = qcCheckAllMaterials.get(integers.get(i));
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())) {
                result = true;
            }
        }
        if (result) {
            for (int j = 0; j < integers.size(); j++) {
                MaterialItem innerMaterialItem = qcCheckAllMaterials.get(integers.get(j));
                innerMaterialItem.setResult("PASS");
                innerMaterialItem.setRemark("主替有一项成功");
                //保存本地数据库
                cacheCheckResult(integers.get(j), innerMaterialItem);
                //更新显示日志
                updateVisitLog(innerMaterialItem);
            }
        } else {
            for (int j = 0; j < integers.size(); j++) {
                MaterialItem innerMaterialItem = qcCheckAllMaterials.get(integers.get(j));
                innerMaterialItem.setResult("FAIL");
                innerMaterialItem.setRemark("料号与站位不对应");
                //保存本地数据库
                cacheCheckResult(integers.get(j), innerMaterialItem);
                //更新显示日志
                updateVisitLog(innerMaterialItem);
            }
        }


        /*
        for (int z = 0;z < integers.size();z++){
            MaterialItem multiMaterialItem = qcCheckAllMaterials.get(integers.get(z));
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())){
                for (int x = 0;x < integers.size();x++){
                    MaterialItem innerMaterialItem = qcCheckAllMaterials.get(integers.get(x));
                    innerMaterialItem.setScanMaterial(mScanValue);
                    if (x == z){
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }else {
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }
                    //保存本地数据库
                    cacheCheckResult(integers.get(x),innerMaterialItem);
                    //更新显示日志
                    updateVisitLog(innerMaterialItem);
                }
                //跳出循环
                return;
            }else {
                multiMaterialItem.setResult("FAIL");
                multiMaterialItem.setRemark("料号与站位不对应");
                //更新显示日志
                updateVisitLog(multiMaterialItem);
            }
        }
        */

    }

    //IPQC未做首次全检
    private void showInfo(String message, String tip, final int userType, int check_type) {
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
            titleMsg = new String[]{"提示", message};
        } else {
            msgStype = new int[]{22, Color.RED, Color.argb(255, 219, 201, 36)};
            titleMsg = new String[]{"提示", message, tip};
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
                        /*if (userType == 1){
                            getFirstCheckAllResult(0);
                        }else*/
//                        if (userType == 2){
//                        if (check_type == 0){
//                            getFeedResult(0,"",-1);
//                        }
//                        getFirstCheckAllResult(0);
//                        }
                        break;
                }
            }
        });
        infoDialog.show();
    }

    private void checkNextMaterial() {
        lv_qcCheckAll.setSelection(curCheckId);
        if (curCheckId < qcCheckAllMaterials.size() - 1) {
            curCheckId++;
            clearLineSeatMaterialScan();
        } else {
            showCheckAllMaterialResult(0);
        }
        Log.i(TAG, "checkNextMaterial:" + curCheckId);
    }

    private void clearLineSeatMaterialScan() {
        edt_ScanMaterial.setText("");
        edt_ScanMaterial.requestFocus();
    }

    private void showCheckAllMaterialResult(int showType) {
        boolean checkResult = true;
        String[] titleMsg;
        int[] msgStyle;
        for (MaterialItem checkMaterialItem : qcCheckAllMaterials) {
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
        int itemResIds[] = new int[]{R.id.dialog_title_view, R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust};

        resultInfoDialog = new InfoDialog(getActivity(), R.layout.info_dialog_layout, itemResIds, titleMsg, msgStyle);

        resultInfoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.info_trust:

                        // TODO: 2018/4/12
                        //获取全检结果
                        checkType = 1;
                        getFirstCheckAllResult(0);
                        dialog.dismiss();
                        if (result) {
                            clearMaterialInfo();
                            //将全检的时间设为当前时间
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int resetCheckAllTime = DBService.getDbService().resetCheckAllTime(globalData.getLine(), globalData.getWork_order(),
                                            globalData.getBoard_type());
                                    Log.d(TAG, "resetCheckAllTime - " + resetCheckAllTime);
                                }
                            }).start();
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


    private void clearMaterialInfo() {
        Log.d(TAG, "clearMaterialInfo");
        clearLineSeatMaterialScan();
        curCheckId = 0;
        clearCheckAllDisplay();
        //清除本地数据库全检纪录
        boolean result = new GreenDaoUtil().updateAllQcCheck(qcCheckAllList);
        Log.d(TAG, "updateAllQcCheck - " + result);
    }

    //清除全检结果
    private void clearCheckAllDisplay() {
        for (int i = 0; i < qcCheckAllMaterials.size(); i++) {
            MaterialItem materialItem = qcCheckAllMaterials.get(i);
            materialItem.setScanLineSeat("");
            materialItem.setScanMaterial("");
            materialItem.setResult("");
            materialItem.setRemark("");
            if (Constants.isCache) {
                QcCheckAll qcCheckAll = qcCheckAllList.get(i);
                qcCheckAll.setScanLineSeat("");
                qcCheckAll.setScanMaterial("");
                qcCheckAll.setResult("");
                qcCheckAll.setRemark("");
            }
        }
        materialAdapter.notifyDataSetChanged();
    }


}
