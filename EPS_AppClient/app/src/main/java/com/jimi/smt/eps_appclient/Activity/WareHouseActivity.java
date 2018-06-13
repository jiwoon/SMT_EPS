package com.jimi.smt.eps_appclient.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.Adapter.WareHouseAdapter;
import com.jimi.smt.eps_appclient.Dao.GreenDaoUtil;
import com.jimi.smt.eps_appclient.Dao.Ware;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Service.RefreshCacheService;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EvenBusTest;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.ProgramItemVisit;
import com.jimi.smt.eps_appclient.Views.InfoDialog;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 类名:WareHouseActivity
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/19 19:29
 * 描述: 仓库 Activity
 */
public class WareHouseActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {

    private final String TAG = "WareHouseActivity";

    private String curOrderNum;//当前工单号
    private String curOperatorNUm;//当前操作员
    private GlobalData globalData;
    private List<MaterialItem> wareHouseMaterialItems = new ArrayList<MaterialItem>();//料号表
    private ListView lv_ware_materials;//所有料号列表
    private EditText et_ware_scan_material;//扫描的料号
    private WareHouseAdapter wareHouseAdapter;
    private InfoDialog infoDialog;//弹出料号对应站位
    private InfoDialog wareResultDialog;//弹出发料结果
    private int sucIssueCount = 0;//成功发料个数
    private int allCount = 0;//总数
    private GlobalFunc globalFunc;
    private static ProgressDialog progressDialog;

    private final int DISSMIASS_DIALOG = 120;//取消站位弹出窗
    //上料本地数据表
    private List<Ware> wareList = new ArrayList<Ware>();
    //是否恢复缓存
    private boolean isRestoreCache = false;

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private static final int doEXIT = 121;//退出
    private static final int GET_PROGRAM_VISITS = 122;//获取料号数据
    private static final int NET_MATERIAL_FALL = 123;//未获取到料号

    @SuppressLint("HandlerLeak")
    private Handler mWareHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            isExit = false;

            switch (msg.what) {
                case doEXIT:
                    isExit = false;
                    break;
                case NET_MATERIAL_FALL:
                    //获取料号表失败
                    Toast.makeText(getApplicationContext(), "获取料号表失败", Toast.LENGTH_SHORT).show();
                    break;
                case GET_PROGRAM_VISITS:
                    //取消弹出窗
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
//                    initViews(savedInstanceState);
//                    initData();
                    break;
            }

        }
    };

    /*
    private Handler dissmissDialogHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DISSMIASS_DIALOG:
                    if ((infoDialog != null) && infoDialog.isShowing()){
                        //取消窗口
                        infoDialog.dismiss();
                    }
                    break;
            }
        }
    };
    */
/*

    private WareHandler mWareHandler;

    private class WareHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case doEXIT:
                    isExit = false;
                    break;
                case GET_PROGRAM_VISITS:
                    //取消弹出窗
                    if (progressDialog != null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
//                    initViews(savedInstanceState);
                    initData();
                    break;
            }
        }
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("lifecycle-", TAG + "--onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ware_house);
        //使屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //开启服务
        startService(new Intent(this, RefreshCacheService.class));
        //注册订阅
        EventBus.getDefault().register(this);
        //全局变量
        globalData = (GlobalData) getApplication();
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        curOrderNum = savedInstanceState.getString("orderNum");
        curOperatorNUm = savedInstanceState.getString("operatorNum");
        globalData.setOperator(curOperatorNUm);
        globalData.setOperType(Constants.STORE_ISSUE);
        globalData.setUpdateType(Constants.STORE_ISSUE);
        globalFunc = new GlobalFunc(WareHouseActivity.this);

        if (Constants.isCache) {
            //查询本地数据库是否存在缓存
            List<Ware> wares = new GreenDaoUtil().queryWareRecord(globalData.getOperator(), globalData.getWork_order()
                    , globalData.getLine(), globalData.getBoard_type());

            //数据库存在缓存数据
            if (wares.size() != 0) {
                //保存缓存
                wareList.addAll(wares);
                isRestoreCache = true;
            } else {
                //不存在缓存数据,删除之前的数据
                boolean result = new GreenDaoUtil().deleteAllWareData();
                Log.d(TAG, "deleteAllWareData - " + result);
                isRestoreCache = false;
            }
        }

        //初始化页面
        initViews();
        initData();

    }


    //监听订阅的消息 todo
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenBusTest event) {
        Log.d(TAG, "onEventMainThread - " + event.getUpdated());
        if (event.getUpdated() == 0) {
            if (Constants.isCache) {
                showUpdateDialog();
                if (event.getWareList() != null && event.getWareList().size() > 0) {
                    //更新页面
                    wareList.clear();
                    wareList.addAll(event.getWareList());
                    sucIssueCount = 0;
                    //填充数据
                    wareHouseMaterialItems.clear();
                    for (Ware ware : wareList) {
                        MaterialItem materialItem = new MaterialItem(ware.getOrder(), ware.getBoard_type(), ware.getLine(),
                                ware.getSerialNo(), ware.getAlternative(), ware.getOrgLineSeat(), ware.getOrgMaterial(),
                                ware.getScanLineSeat(), ware.getScanMaterial(), ware.getResult(), ware.getRemark());
                        wareHouseMaterialItems.add(materialItem);
                        //获取成功发料次数
                        if (ware.getResult().equalsIgnoreCase("PASS")) {
                            sucIssueCount++;
                        }
                    }
                    allCount = wareHouseMaterialItems.size();
                    //更新显示
                    wareHouseAdapter.notifyDataSetChanged();
                    //重新开始扫描料号
                    et_ware_scan_material.requestFocus();
                    et_ware_scan_material.setText("");
                }
            }

        }
    }

    private void showUpdateDialog() {
        if (wareResultDialog != null && wareResultDialog.isShowing()) {
            wareResultDialog.cancel();
            wareResultDialog.dismiss();
        }
        Log.d(TAG, "showUpdateDialog");
        final LoadingDialog loadingDialog = new LoadingDialog(this, "站位表更新...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle-", TAG + "--onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("lifecycle-", TAG + "--onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifecycle-", TAG + "--onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle-", TAG + "--onStop");
    }

    @Override
    protected void onDestroy() {
        //注销订阅
        EventBus.getDefault().unregister(this);
        //关闭服务
        stopService(new Intent(this, RefreshCacheService.class));
        super.onDestroy();
        Log.i("lifecycle-", TAG + "--onDestroy");
    }

    //从program_item_visit表中获取发料数据 // TODO: 2018/2/7  
    private void getMaterialItemsVisit(final String programID) {
        progressDialog = ProgressDialog.show(this, "请稍候!", "正在加载工单...", true);
        progressDialog.setCanceledOnTouchOutside(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ProgramItemVisit> itemVisits = new DBService().getProgramItemVisits(globalData.getLine(),
                        globalData.getWork_order(), globalData.getBoard_type());
                Message message = Message.obtain();
                if (itemVisits.size() < 0) {
                    message.what = NET_MATERIAL_FALL;
                } else {
                    globalData.setProgramItemVisits(itemVisits);//保存料号
                    message.what = GET_PROGRAM_VISITS;
                }
                mWareHandler.sendMessage(message);
            }
        }).start();
    }

    //初始化数据 todo
    private void initData() {
        sucIssueCount = 0;
        wareHouseMaterialItems.clear();
        //没有数据库缓存
        if (!isRestoreCache) {
            List<MaterialItem> materialItems = globalData.getMaterialItems();
            //填充数据
            for (MaterialItem materialItem : materialItems) {
                MaterialItem feedMaterialItem = new MaterialItem(globalData.getWork_order(), globalData.getBoard_type(),
                        globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(),
                        materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(),
                        "", "", "", "");
                wareHouseMaterialItems.add(feedMaterialItem);

                //保存到数据库中
                if (Constants.isCache) {
                    Ware ware = new Ware(null, globalData.getWork_order(), globalData.getOperator(), globalData.getBoard_type(),
                            globalData.getLine(), materialItem.getSerialNo(), materialItem.getAlternative(), materialItem.getOrgLineSeat(),
                            materialItem.getOrgMaterial(), "", "", "", "");
                    wareList.add(ware);
                }
            }
            //保存到数据库中
            if (Constants.isCache) {
                boolean cacheResult = new GreenDaoUtil().insertMultiWareMaterial(wareList);
                Log.d(TAG, "cacheResult - " + cacheResult);
            }

        }
        //存在缓存
        else {
            for (Ware ware : wareList) {
                MaterialItem materialItem = new MaterialItem(ware.getOrder(), ware.getBoard_type(), ware.getLine(),
                        ware.getSerialNo(), ware.getAlternative(), ware.getOrgLineSeat(), ware.getOrgMaterial(),
                        ware.getScanLineSeat(), ware.getScanMaterial(), ware.getResult(), ware.getRemark());
                wareHouseMaterialItems.add(materialItem);
                //获取成功发料次数
                if (ware.getResult().equalsIgnoreCase("PASS")) {
                    sucIssueCount++;
                }
            }
            //todo 需要更新全局变量为本地数据库的
            globalData.setMaterialItems(wareHouseMaterialItems);
        }

        allCount = wareHouseMaterialItems.size();
        //设置Adapter
        wareHouseAdapter = new WareHouseAdapter(getApplicationContext(), wareHouseMaterialItems);
        lv_ware_materials.setAdapter(wareHouseAdapter);

    }

    //初始化页面
    private void initViews() {
        Log.i(TAG, "curOderNum::" + curOrderNum + " -- curOperatorNUm::" + curOperatorNUm);
        ImageView iv_ware_back = (ImageView) findViewById(R.id.iv_ware_back);
        TextView tv_ware_order = (TextView) findViewById(R.id.tv_ware_order);
        TextView tv_ware_operator = (TextView) findViewById(R.id.tv_ware_operator);
        et_ware_scan_material = (EditText) findViewById(R.id.et_ware_scan_material);
        lv_ware_materials = (ListView) findViewById(R.id.lv_ware_materials);
        iv_ware_back.setOnClickListener(this);
        et_ware_scan_material.setOnEditorActionListener(this);
        tv_ware_order.setText(curOrderNum);
        tv_ware_operator.setText(curOperatorNUm);
    }

    //按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ware_back:
                exit();
                break;
        }
    }

    //物理返回键
    @Override
    public void onBackPressed() {
        exit();
    }

    //扫料号的输入事件
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == event.KEYCODE_ENTER)) {
            //先判断是否联网
            if (globalFunc.isNetWorkConnected()) {
                Log.d(TAG, "onEditorAction::" + v.getText());
                Log.d(TAG, "event.getAction()::" + event.getAction());

                if (!TextUtils.isEmpty(v.getText().toString().trim())) {
                    //扫描的内容
                    String scanMaterial = String.valueOf(((EditText) v).getText());
                    scanMaterial = scanMaterial.replaceAll("\r", "");
                    Log.i(TAG, "sacnMaterial=" + scanMaterial);
                    //料号,若为二维码则提取@@前的料号
                    //提取有效料号
                    scanMaterial = globalFunc.getMaterial(scanMaterial);
                    v.setText(scanMaterial);
                    //未扫描过料号的站位及结果
                    HashSet<String> lineSeatHashSet = new HashSet<String>();
                    //未扫描过料号的站位
                    ArrayList<String> lineSeatList = new ArrayList<String>();
                    //已经扫描过料号的站位及结果
                    HashSet<String> scanedSeatSet = new HashSet<String>();

                    for (int i = 0; i < wareHouseMaterialItems.size(); i++) {
                        MaterialItem materialItem = wareHouseMaterialItems.get(i);

                        if (materialItem.getOrgMaterial().equalsIgnoreCase(scanMaterial)) {
                            int serialNo = materialItem.getSerialNo();
                            if (!materialItem.getResult().equalsIgnoreCase("PASS")) {
                                lineSeatHashSet.add("(" + String.valueOf(serialNo) + ")" + " " + materialItem.getOrgLineSeat());
                                lineSeatList.add(materialItem.getOrgLineSeat());
                            } else {
                                scanedSeatSet.add("(" + String.valueOf(serialNo) + ")" + " " + materialItem.getOrgLineSeat() + "(已经发料)");
                            }
                        }
                    }

                    //未扫过料的站位
                    ArrayList<String> lineSeatAl = new ArrayList<String>();
                    lineSeatAl.addAll(lineSeatHashSet);

                    //已经扫描过料号的站位
                    ArrayList<String> wareSeatList = new ArrayList<String>();
                    wareSeatList.addAll(scanedSeatSet);

                    //arrayLists的外部长度等于lineSeatList的长度
                    ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<ArrayList<Integer>>();
                    for (int k = 0; k < lineSeatList.size(); k++) {
                        ArrayList<Integer> lineSeatIndex = new ArrayList<Integer>();
                        for (int j = 0; j < wareHouseMaterialItems.size(); j++) {
                            MaterialItem innerItem = wareHouseMaterialItems.get(j);
                            if (innerItem.getOrgLineSeat().equalsIgnoreCase(lineSeatList.get(k))) {
                                innerItem.setScanMaterial(scanMaterial);
                                innerItem.setResult("PASS");
                                lineSeatIndex.add(j);
                                //成功次数加1
                                sucIssueCount++;
                            }
                        }
                        arrayLists.add(lineSeatIndex);
                    }

                    //弹出站位
                    showInfo("料号:" + scanMaterial, lineSeatAl, wareSeatList, 1);
                    /*
                    //启动子线程
                    mDissMissThread=new DissMissThread();
                    mDissMissThread.start();
                    */
                    //写日志
                    setOperateLog(arrayLists, scanMaterial);
                    //刷新数据
                    wareHouseAdapter.notifyDataSetChanged();
                }
            } else {
                showInfo("警告", null, null, 2);
            }
        }
        return false;
    }

    /**
     * 弹出提示站位窗口
     *
     * @param title
     * @param lineSeatList 未发料的站位
     * @param wareSeatList 已发料的站位
     * @param type
     */
    private void showInfo(String title, ArrayList<String> lineSeatList, ArrayList<String> wareSeatList, int type) {
        //未发料的站位
        StringBuilder message = new StringBuilder();
        //已经扫描过料号的站位
        StringBuilder wareSeatStr = new StringBuilder();
        //内容的样式
        int msgStype[];
        //标题和内容
        String titleMsg[];
        if ((lineSeatList != null) && (lineSeatList.size() > 0)) {
           /*
            if (lineSeatList.get(0).contains("已经扫描发料")){
                message = lineSeatList.get(0);
                msgStype=new int[]{23, Color.argb(255,219,201,36)};
            }else {
                */
            //添加所有站位
            message = new StringBuilder("站位:");
            for (String lineSeat : lineSeatList) {
                message.append("\n").append(lineSeat);
            }
            if (wareSeatList == null || wareSeatList.size() <= 0) {
                //只存在未扫描过站位,
                msgStype = new int[]{23, Color.argb(255, 102, 153, 0)};
                titleMsg = new String[]{title, message.toString()};
            } else {
                //同时存在已经扫描过料号的站位
                for (String wareSeat : wareSeatList) {
                    wareSeatStr.append("\n").append(wareSeat);
                }
                msgStype = new int[]{23, Color.argb(255, 102, 153, 0), Color.argb(255, 219, 201, 36)};
                titleMsg = new String[]{title, message.toString(), wareSeatStr.toString()};
            }

//            }
        } else {
            if (wareSeatList != null && wareSeatList.size() > 0) {
                //只存在已经扫描过料号的站位
                for (String wareSeat : wareSeatList) {
                    wareSeatStr.append("\n").append(wareSeat);
                }
                msgStype = new int[]{23, Color.argb(255, 219, 201, 36)};
                titleMsg = new String[]{title, wareSeatStr.toString()};
            } else {
                if (type == 1) {
                    //写日志
                    setOperateLog(null, title);
                    //站位不存在
                    message = new StringBuilder("不存在该料号的站位!");
                } else if (type == 2) {
                    //网络未连接
                    message = new StringBuilder("请检查网络是否连接!");
                }
                titleMsg = new String[]{title, message.toString()};
                //内容的样式
                msgStype = new int[]{22, Color.RED};
            }
        }

        //对话框所有控件id
        int itemResIds[] = new int[]{R.id.dialog_title_view,
                R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust, R.id.tv_alert_msg};

        infoDialog = new InfoDialog(this,
                R.layout.info_dialog_layout, itemResIds, titleMsg, msgStype);
        //确定按钮点击事件
        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.info_trust:
                        dialog.dismiss();
                        break;
                }
            }
        });
        //弹出窗取消事件监听
        infoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                et_ware_scan_material.requestFocus();
                et_ware_scan_material.setText("");
                /*
                //停止线程
                if ((mDissMissThread != null) && (!mDissMissThread.isInterrupted())){
                    mDissMissThread.interrupt();
                    mDissMissThread=null;
                }
                */
                //显示最终结果
                showIssueResult();
            }
        });
        infoDialog.show();

    }

    //显示最终发料结果
    private void showIssueResult() {
        Log.d(TAG, "sucIssueCount-" + sucIssueCount
                + "\nwareHouseMaterialItems-" + wareHouseMaterialItems.size() + "\nallCount-" + allCount);
        if (sucIssueCount >= wareHouseMaterialItems.size() && sucIssueCount >= allCount) {
            //显示最终结果
            boolean result = true;
            for (MaterialItem materialItem : wareHouseMaterialItems) {
                if (!materialItem.getResult().equalsIgnoreCase("PASS")) {
                    result = false;
                    break;
                }
            }
            //弹出发料结果
            String titleMsg[];
            int msgStyle[];
            if (result) {
                titleMsg = new String[]{"发料结果", "PASS"};
                msgStyle = new int[]{66, Color.argb(255, 102, 153, 0)};
            } else {
                titleMsg = new String[]{"发料失败，请检查!", "FAIL"};
                msgStyle = new int[]{66, Color.RED};
            }
            showIssueInfo(titleMsg, msgStyle, result, 1);
        }
    }

    //发料结果
    private void showIssueInfo(String[] titleMsg, int[] msgStyle, final boolean result, final int resultType) {
        //对话框所有控件id
        int itemResIds[] = new int[]{R.id.dialog_title_view,
                R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust};

        wareResultDialog = new InfoDialog(this,
                R.layout.info_dialog_layout, itemResIds, titleMsg, msgStyle);

        wareResultDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.info_trust:
                        if (result) {
                            dialog.dismiss();
                            et_ware_scan_material.requestFocus();
                            et_ware_scan_material.setText("");
                            // TODO: 2018/4/8
                            boolean result = new GreenDaoUtil().updateAllWare(wareList);
                            Log.d(TAG, "updateAllFeed - " + result);
                            clearWareDisplay();
//                            initData();
                        } else {
                            dialog.dismiss();
                            et_ware_scan_material.requestFocus();
                            et_ware_scan_material.setText("");
                            if (resultType == 1) {
                                //将未成功数加到
                                for (MaterialItem feedMaterialItem : wareHouseMaterialItems) {
                                    if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                                        allCount++;
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        });
        wareResultDialog.show();
    }

    //清除发料页面结果
    private void clearWareDisplay() {
        sucIssueCount = 0;
        allCount = 0;
        for (int i = 0; i < wareHouseMaterialItems.size(); i++) {
            MaterialItem materialItem = wareHouseMaterialItems.get(i);
            materialItem.setScanLineSeat("");
            materialItem.setScanMaterial("");
            materialItem.setResult("");
            materialItem.setRemark("");
            if (Constants.isCache) {
                Ware ware = wareList.get(i);
                ware.setScanLineSeat("");
                ware.setScanMaterial("");
                ware.setResult("");
                ware.setRemark("");
            }
            allCount++;
        }
        wareHouseAdapter.notifyDataSetChanged();
    }

    //写日志并显示 
    // TODO: 2018/4/1  
    private void setOperateLog(ArrayList<ArrayList<Integer>> integerLists, String scanMaterial) {
        if (integerLists != null) {
            if (integerLists.size() > 0) {
                Log.d(TAG, "setOperateLog::" + integerLists.size());
                ArrayList<Integer> lineSeatIndex = new ArrayList<Integer>();
                for (int m = 0; m < integerLists.size(); m++) {
                    lineSeatIndex.clear();
                    lineSeatIndex.addAll(integerLists.get(m));
                    //当前料的序号
                    int curSerialNo = -1;
                    //当前料是主料或替料
                    byte alternative = 0;
                    for (int k = 0; k < lineSeatIndex.size(); k++) {
                        MaterialItem innerItem = wareHouseMaterialItems.get(lineSeatIndex.get(k));
                        //获取当前扫的料号序号、主替料
                        if (innerItem.getOrgMaterial().equalsIgnoreCase(scanMaterial)) {
                            curSerialNo = innerItem.getSerialNo();
                            alternative = innerItem.getAlternative();
                        }
                    }
                    for (int n = 0; n < lineSeatIndex.size(); n++) {
                        Log.d(TAG, "lineSeatIndex::" + lineSeatIndex.get(n));
                        MaterialItem innerItem = wareHouseMaterialItems.get(lineSeatIndex.get(n));
                        if (lineSeatIndex.size() > 1) {
                            //当前扫的料
                            if (innerItem.getSerialNo() == curSerialNo) {
                                innerItem.setRemark("发料成功");
                            }
                            //该站位的其他料
                            else {
                                if (alternative == 0) {
                                    //当前扫的料是主料
                                    innerItem.setRemark("主料" + curSerialNo + "发料成功");
                                } else if (alternative == 1) {
                                    //当前扫的料是替料
                                    innerItem.setRemark("替料" + curSerialNo + "发料成功");
                                }
                            }
                        } else {
                            innerItem.setRemark("发料成功");
                        }
                        //保存本地数据库缓存
                        cacheWareResult(lineSeatIndex.get(n), innerItem);
                        //添加日志
                        globalFunc.AddDBLog(globalData, innerItem);
                        //更新显示日志
                        globalFunc.updateVisitLog(globalData, innerItem);
                        //置顶
                        lv_ware_materials.setSelection(lineSeatIndex.get(n));
                        //刷新数据
                        wareHouseAdapter.notifyDataSetChanged();
                    }
                }
            }
        } else {
            Byte aByte = 0;
            MaterialItem failMaterialItem = new MaterialItem(globalData.getWork_order(), globalData.getBoard_type(), globalData.getLine(),
                    -1, aByte, "", "", "", scanMaterial, "FAIL", "不存在该料号的站位!");
            globalFunc.AddDBLog(globalData, failMaterialItem);
        }
    }

    //更新本地数据库发料缓存
    private void cacheWareResult(int index, MaterialItem materialItem) {
        if (Constants.isCache) {
            //保存缓存
            Ware ware = wareList.get(index);
            ware.setScanLineSeat(materialItem.getScanLineSeat());
            ware.setScanMaterial(materialItem.getScanMaterial());
            ware.setResult(materialItem.getResult());
            ware.setRemark(materialItem.getRemark());
            new GreenDaoUtil().updateWare(ware);
        }
    }

    /*
    //定时取消窗口子线程
    protected class DissMissThread extends Thread{
        @Override
        public void run() {
            while (!isInterrupted()){
                if ((infoDialog != null) && (infoDialog.isShowing())){
                    try {
                        Thread.sleep(5000);//休眠5s
                        //发送消息取消窗口
                        Message message=Message.obtain();
                        message.what=DISSMIASS_DIALOG;
                        dissmissDialogHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        //捕获到阻塞异常，退出
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }
*/
    //返回主页
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            Message message = Message.obtain();
            message.what = doEXIT;
            mWareHandler.sendMessageDelayed(message, 2000);
//            mWareHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }

}
