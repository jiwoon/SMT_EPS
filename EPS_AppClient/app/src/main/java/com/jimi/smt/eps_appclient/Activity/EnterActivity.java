package com.jimi.smt.eps_appclient.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.Adapter.EnterOrdersAdapter;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Func.UpdateAppService;
import com.jimi.smt.eps_appclient.GlobalData;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EpsAppVersion;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.Operator;
import com.jimi.smt.eps_appclient.Unit.Program;
import com.jimi.smt.eps_appclient.Views.InfoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名:EnterActivity
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/19 9:29
 * 描述: app入口
 */
public class EnterActivity extends Activity implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private final String TAG="EnterActivity";
    private EditText et_enter_operator;
    private ProgressDialog progressDialog;
    private GlobalData globalData;
    private String curOrderNum;//工单号
    private String curOperatorNum;//操作员
    private Operator scanOperator;//扫描到的操作员

    private final int SET_ORDER=8;//设置工单号
    private final int NET_MATERIAL_FALL=400;//获取料号表失败
    private final int OPERATOR_DISSMISS=110;//操作员离职
    private final int OPERATOR_NULL = 111;//操作员不存在
    private final int TO_WARE_HOUSE=0;//仓库
    private final int TO_FACTORY=1;//厂线
    private final int TO_QC=2;//QC
    private final int ADMIN=3;//管理员
    private final int UPDATE_APK = 5;//更新apk
    private List<Program> mProgramList=new ArrayList<Program>();//工单号列表

    private int oldCheckIndex = -1;//之前选中的
    private int curCheckIndex = -1;//现在选中的


    private Handler enterHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //取消弹出窗
            if (progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            switch (msg.what){
                case SET_ORDER:
                    initData(mProgramList);
                    break;
                case NET_MATERIAL_FALL:
                    //获取料号表失败
                    Toast.makeText(getApplicationContext(),"获取料号表失败",Toast.LENGTH_SHORT).show();
                    showInfo("警告", "请检查网络连接是否正常!");
                    break;
                case OPERATOR_DISSMISS:
                    //操作员离职
                    Toast.makeText(getApplicationContext(),"操作员离职",Toast.LENGTH_SHORT).show();
                    et_enter_operator.setText("");
                    et_enter_operator.requestFocus();
                    break;
                case OPERATOR_NULL:
                    //不存在该操作员
                    Toast.makeText(getApplicationContext(),"请重新扫描",Toast.LENGTH_LONG).show();
                    et_enter_operator.setText("");
                    et_enter_operator.requestFocus();
                    break;
                case TO_WARE_HOUSE:
                    //仓库
                    Toast.makeText(getApplicationContext(),"仓库",Toast.LENGTH_SHORT).show();
                    Intent warehouse=new Intent(getApplicationContext(),WareHouseActivity.class);
                    Bundle warehouseBundle=new Bundle();
                    warehouseBundle.putString("orderNum",curOrderNum);
                    warehouseBundle.putString("operatorNum",curOperatorNum);
                    warehouse.putExtras(warehouseBundle);
//                    startActivity(warehouse);
                    startActivityForResult(warehouse,Constants.ACTIVITY_RESULT);
                    break;
                case TO_FACTORY:
                    //厂线
                    Toast.makeText(getApplicationContext(),"厂线",Toast.LENGTH_SHORT).show();
                    Intent factory=new Intent(getApplicationContext(),FactoryLineActivity.class);
                    Bundle factoryBundle=new Bundle();
                    factoryBundle.putString("orderNum",curOrderNum);
                    factoryBundle.putString("operatorNum",curOperatorNum);
                    factory.putExtras(factoryBundle);
//                    startActivity(factory);
                    startActivityForResult(factory,Constants.ACTIVITY_RESULT);
                    break;
                case TO_QC:
                    //QC
                    Toast.makeText(getApplicationContext(),"QC",Toast.LENGTH_SHORT).show();
                    Intent qc=new Intent(getApplicationContext(),QCActivity.class);
                    Bundle qcBundle=new Bundle();
                    qcBundle.putString("orderNum",curOrderNum);
                    qcBundle.putString("operatorNum",curOperatorNum);
                    qc.putExtras(qcBundle);
//                    startActivity(qc);
                    startActivityForResult(qc,Constants.ACTIVITY_RESULT);
                    break;
                case ADMIN:
                    //管理员
                    Toast.makeText(getApplicationContext(),"您是管理员!",Toast.LENGTH_LONG).show();
                    et_enter_operator.setText("");
                    break;
                case UPDATE_APK:
                    et_enter_operator.setText("");
                    break;
            }
        }
    };
    private ProgressDialog updateProgress;
    private UpdateApkReceiver apkReceiver;
    private GlobalFunc globalFunc;
    private EditText et_enter_line;
    private ListView lv_orders;
    private EnterOrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        //获取全局变量
        globalData = (GlobalData) getApplication();
        globalFunc=new GlobalFunc(this);
        //创建apk下载路径
        createApkDownloadDir();
        initViews();
        //先判断是否有网络连接
        boolean netConnect = globalFunc.isNetWorkConnected();
        if (netConnect){
            //更新app
            boolean result = updateApp();
            Log.d(TAG,"updateApp-result::"+result);
        }else {
            showInfo("警告","请检查网络连接是否正常!");
        }
        //注册广播
        registeReceiver();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"onRestart");


        if (et_enter_operator != null){
            et_enter_operator.setText("");
        }

        if (et_enter_line != null){
            et_enter_line.setText("");
            et_enter_line.requestFocus();
        }

        if ((mProgramList != null) && (mProgramList.size() >0)){
            mProgramList.clear();
            ordersAdapter.notifyDataSetChanged();
        }

        curCheckIndex = -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apkReceiver != null){
            unregisterReceiver(apkReceiver);
        }

    }

    //初始化布局
    private void initViews(){
        et_enter_line = (EditText) findViewById(R.id.et_enter_line);
        et_enter_operator = (EditText) findViewById(R.id.et_enter_operator);
        lv_orders = (ListView) findViewById(R.id.lv_orders);
        RelativeLayout rl_enter = (RelativeLayout) findViewById(R.id.rl_enter);
        rl_enter.setOnClickListener(this);
        et_enter_line.setOnEditorActionListener(this);
        et_enter_operator.setOnEditorActionListener(this);
        et_enter_operator.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (TextUtils.isEmpty(et_enter_line.getText().toString())){
                        et_enter_line.setText("");
                        et_enter_line.requestFocus();
                    }
                }
            }
        });
        lv_orders.setOnItemClickListener(this);

    }

    //初始化数据
    private void initData(List<Program> programs){
        if ((programs != null) && (programs.size() > 0)){
            //显示工单
            ordersAdapter = new EnterOrdersAdapter(getApplicationContext(),programs);
            lv_orders.setAdapter(ordersAdapter);
        }else {
            Toast.makeText(this,"请重新扫描线号",Toast.LENGTH_LONG).show();
            et_enter_line.setText("");
            et_enter_line.requestFocus();
        }
    }

    //操作员输入框监听事件
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //回车键
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == event.KEYCODE_ENTER)){
            Log.d(TAG,"event.getAction()::"+event.getAction());
            Log.d(TAG,"event.getKeyCode()::"+event.getKeyCode());

            switch (event.getAction()){
                case KeyEvent.ACTION_UP:
                    //判断网络是否连接
                    if (globalFunc.isNetWorkConnected()){
                        if (!TextUtils.isEmpty(v.getText().toString().trim())){
                            //扫描内容
                            String scanValue = String.valueOf(((EditText) v).getText());
                            scanValue = scanValue.replaceAll("\r", "");
                            Log.i(TAG, "scan Value:" + scanValue);

                            switch (v.getId()){
                                case R.id.et_enter_line:
                                    //目前站位格式
                                    // 100805100 -> 308线 05-10 站位
                                    // 100805084 -> 308线 05-08 站位
                                    boolean lineExit = false;
                                    //扫描线号
                                    String scanLine = scanValue;
                                    if (scanValue.length() >= 8){
                                        scanLine="30"+scanValue.substring(3,4);
                                    }
                                    scanValue = scanLine;
                                    for (int i = 0;i < Constants.lines.length;i++){
                                        if (scanLine.equals(Constants.lines[i])){
                                            lineExit = true;
                                        }
                                    }
                                    if (lineExit){
                                        //根据线号获取工单
                                        getPrograms(scanLine);
                                        et_enter_operator.requestFocus();
                                    }else {
                                        Toast.makeText(getApplicationContext(),"该线号不存在，重新扫描线号!",Toast.LENGTH_LONG).show();
                                        et_enter_line.setText("");
                                        et_enter_line.requestFocus();
                                    }
                                    break;
                                case R.id.et_enter_operator:
                                    final String scanOperatorStr = scanValue;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //查找数据库判断操作员类型
                                            Operator operator=new DBService().getCurOperator(scanOperatorStr);
                                            Message message=Message.obtain();
                                            if (operator != null){
                                                if (operator.isEnabled() == 0){
                                                    //操作员处于离职
                                                    message.what=OPERATOR_DISSMISS;
                                                    //发送消息
                                                    enterHandler.sendMessage(message);
                                                }else if (operator.isEnabled() == 1){
                                                    //操作员在职
                                                    scanOperator = operator;
                                                }
                                            }else {
                                                //不存在该操作员,重新扫描
                                                message.what=OPERATOR_NULL;
                                                //发送消息
                                                enterHandler.sendMessage(message);
                                            }
                                        }
                                    }).start();

                                    break;
                            }
                        }else {
                            Toast.makeText(this,"请重新扫描!",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        showInfo("警告","请检查网络连接是否正常!");
                    }

                    return true;
                default:
                    return true;
            }

        }
        return false;
    }

    //弹出提示消息窗口
    private boolean showInfo(String title,String message){
        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};
        //标题和内容
        String titleMsg[]=new String[]{title,message};
        //内容的样式
        int msgStype[]=new int[]{22, Color.RED};
        InfoDialog infoDialog=new InfoDialog(this,
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStype);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        dialog.dismiss();
                        et_enter_line.setText("");
                        et_enter_operator.setText("");
                        et_enter_line.requestFocus();
                        break;
                }
            }
        });
        infoDialog.show();

        return true;
    }

    //获取工单号
    private void getPrograms(final String line){
        progressDialog = ProgressDialog.show(this,"请稍候!","正在加载工单...",true);
        progressDialog.setCanceledOnTouchOutside(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Program> programList=new DBService().getProgramOrder(line);
//                List<Program> programList=new DBService().getProgramOrder("308");
                Message message=Message.obtain();
                message.what=SET_ORDER;
                mProgramList=programList;
                //发送消息
                enterHandler.sendMessage(message);
            }
        }).start();
    }

    private boolean updateAppResult;
    //更新app
    private boolean updateApp(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo("com.jimi.smt.eps_appclient",0);
                    int mVersionCode=packageInfo.versionCode;
                    String mVersionName=packageInfo.versionName;
                    EpsAppVersion epsAppVersion=new DBService().getAppVersion();

                    if (epsAppVersion == null){
                        Log.d(TAG,"epsAppVersion == null");
                        //弹出网络不好
                        Message message = Message.obtain();
                        message.what = NET_MATERIAL_FALL;
                        enterHandler.sendMessage(message);
                        updateAppResult = false;
                    }else {
                        Log.d(TAG,"mVersionCode-"+mVersionCode+"\n"+"serviceCode-"+epsAppVersion.getVersionCode());
                        Log.d(TAG,"mVersionName-"+mVersionName+"\n"+"serviceName-"+epsAppVersion.getVersionName());
                        if (epsAppVersion.getVersionCode() <= mVersionCode){
                            updateAppResult = true;
                        }else {
                            //服务器上的版本号大于该版本,则更新
                            Intent intent=new Intent(getApplicationContext(), UpdateAppService.class);
                            intent.putExtra("apkUrl",Constants.DOWNLOAD_URL+"/"+epsAppVersion.getVersionName()+".apk");
                            intent.putExtra("apkName",epsAppVersion.getVersionName()+".apk");
                            startService(intent);

                            updateAppResult = false;
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    updateAppResult = false;
                }
            }
        }).start();

        return updateAppResult;
    }

    //创建apk下载路径
    private void createApkDownloadDir(){
        File downloadDir;
        boolean sd = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sd){
            File sdPath = Environment.getExternalStorageDirectory();
            String path=sdPath.getPath();
            downloadDir = new File(path);
            if (!downloadDir.exists()){
                downloadDir.mkdir();
            }
            globalData.setApkDownloadDir(downloadDir.getPath());
            //删除之前下载的apk
            deleteDownLoadApk();
        }
    }

    //更新安装apk之后，重新启动app，将下载的apk删除
    private void deleteDownLoadApk(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.jimi.smt.eps_appclient",0);
            int mVersionCode=packageInfo.versionCode;
            String mVersionName=packageInfo.versionName;
            File file=new File(globalData.getApkDownloadDir()+"/"+mVersionName+".apk");
            Log.d(TAG,"deleteDownLoadApk-"+file.getAbsolutePath());
            if (file.exists()){
                file.delete();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.ACTIVITY_RESULT:
                //先判断网络是否连接
                boolean net=globalFunc.isNetWorkConnected();
                if (net){
                    //更新app
                    boolean result = updateApp();
                    Log.d(TAG,"updateApp-result::"+result);
/*

                    //刷新工单号列表 // TODO: 2017/11/8
                    getPrograms("308");
                    //获取上一个活动返回的工单号
                    Bundle bundle=data.getExtras();
                    String curOrderNum=bundle.getString("orderNum");
                    Log.d(TAG,"orderNum----"+curOrderNum);
*/

                }else {
                    showInfo("警告","请检查网络连接是否正常!");
                }

                break;
            default:
                break;
        }
    }

    //工单点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        curCheckIndex = position;
        Program curProgram = mProgramList.get(curCheckIndex);
        curProgram.setChecked(true);
        if (oldCheckIndex >= 0){
            if (curCheckIndex != oldCheckIndex){
                //设置之前的选中为不选中状态，当前的选中为选中状态
                Program oldProgram = mProgramList.get(oldCheckIndex);
                oldProgram.setChecked(false);
                //将当前赋值给之前
                oldCheckIndex = curCheckIndex;
            }
        }else {
            //将当前赋值给之前
            oldCheckIndex = curCheckIndex;
        }
        ordersAdapter.notifyDataSetChanged();
        lv_orders.setSelection(curCheckIndex);
        Log.d(TAG,"curProgram-order::"+curProgram.getWork_order());
        Log.d(TAG,"curProgram-ProgramID::"+curProgram.getProgramID());
    }

    //进入按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_enter:
                if (globalFunc.isNetWorkConnected()){
                    if ((!TextUtils.isEmpty(et_enter_line.getText().toString().trim()))
                            && (!TextUtils.isEmpty(et_enter_operator.getText().toString().trim()))){
                        if (curCheckIndex >= 0){
                            lv_orders.setSelection(curCheckIndex);
                            //显示加载框
                            progressDialog = ProgressDialog.show(this,"请稍等!", "正在加载数据...",true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Message message=Message.obtain();
                                    //获取工单号对应的料号表
                                    List<MaterialItem> materialItems=new DBService()
                                            .getMaterial(mProgramList.get(curCheckIndex).getProgramID());
                                    Log.d(TAG,"curCheckIndex-ProgramID-"+mProgramList.get(curCheckIndex).getProgramID());
                                    //未获取到工单号对应的料号表
                                    if (materialItems.size() <= 0){
                                        message.what=NET_MATERIAL_FALL;
                                    }else {
                                        globalData.setMaterialItems(materialItems);
                                        globalData.setLine(mProgramList.get(curCheckIndex).getLine());
                                        globalData.setWork_order(mProgramList.get(curCheckIndex).getWork_order());
                                        globalData.setBoard_type(mProgramList.get(curCheckIndex).getBoard_type());
                                        //成功获取料号表,操作员在职
                                        message.what = scanOperator.getType();
                                        curOrderNum = mProgramList.get(curCheckIndex).getWork_order();
                                        curOperatorNum = scanOperator.getId();
                                    }
                                    //发送消息
                                    enterHandler.sendMessage(message);
                                }
                            }).start();
                            //判断操作员类型,进入页面
                        }else {
                            Toast.makeText(getApplicationContext(),"请选择工单号",Toast.LENGTH_LONG).show();
                        }
                    }else if (TextUtils.isEmpty(et_enter_line.getText().toString().trim())){
                        Toast.makeText(getApplicationContext(),"请扫描线号",Toast.LENGTH_LONG).show();
                        et_enter_line.setText("");
                        et_enter_line.requestFocus();
                    }else if (TextUtils.isEmpty(et_enter_operator.getText().toString().trim())){
                        Toast.makeText(getApplicationContext(),"请扫描线工号",Toast.LENGTH_LONG).show();
                        et_enter_operator.setText("");
                        et_enter_operator.requestFocus();
                    }
                }else {
                    globalFunc.showInfo("警告","请检查网络连接是否正常!","请连接网络!");
                }

                break;
        }
    }

    /**
     * 升级apk进度广播
     */
    private class UpdateApkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度更新ui
            int progress=intent.getIntExtra("progress",0);
            showUpdateProgress(progress);
            if (progress >= 100){
                Log.d(TAG,"progress-"+progress);
                updateProgress.dismiss();
                updateProgress.cancel();
            }

        }
    }

    //注册广播
    private void registeReceiver(){
        updateProgress = new ProgressDialog(this);
        apkReceiver = new UpdateApkReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.jimi.smt.eps_appclient.UPDATE");
        registerReceiver(apkReceiver,intentFilter);
        Log.d(TAG,"===registeReceiver===");
    }

    //显示下载进度
    private void showUpdateProgress(int progress){
//        updateProgress.show();
        updateProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        updateProgress.setCancelable(false);
        updateProgress.setCanceledOnTouchOutside(false);
        updateProgress.setIcon(R.mipmap.icon_download);
        updateProgress.setMax(100);
        updateProgress.setProgress(progress);
        updateProgress.setTitle("下载更新App");
        if (progress > 0 && progress < 100){
            updateProgress.setMessage("正在下载...");
        }else if (progress >= 100){
            updateProgress.setTitle("下载完成");
//            updateProgress.dismiss();
        }
        updateProgress.show();
    }
}
