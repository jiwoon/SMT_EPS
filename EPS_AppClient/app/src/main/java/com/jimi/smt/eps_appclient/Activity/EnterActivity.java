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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.Func.DBService;
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
public class EnterActivity extends Activity implements TextView.OnEditorActionListener {

    private final String TAG="EnterActivity";
    private EditText et_enter_operator;
    private Spinner sp_oderNo;
    private ArrayList<String> spinnerList;
    private ArrayAdapter<String> spinnerAdapter;
    private ProgressDialog progressDialog;
    private GlobalData globalData;
    private String curOrderNum;//工单号
    private String curOperatorNum;//操作员

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

    private Handler enterHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //取消弹出窗
            progressDialog.dismiss();
            switch (msg.what){
                case SET_ORDER:
                    initData(mProgramList);
                    break;
                case NET_MATERIAL_FALL:
                    //获取料号表失败
                    Toast.makeText(getApplicationContext(),"获取料号表失败",Toast.LENGTH_SHORT).show();
                    showInfo("提示", "请确认当前网络是否正常\n或工单号是否正确!!!");
                    break;
                case OPERATOR_DISSMISS:
                    //操作员离职
                    Toast.makeText(getApplicationContext(),"操作员离职",Toast.LENGTH_SHORT).show();
                    et_enter_operator.setText("");
                    break;
                case OPERATOR_NULL:
                    //操作员离职
                    Toast.makeText(getApplicationContext(),"请重新扫描",Toast.LENGTH_LONG).show();
                    et_enter_operator.setText("");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        //获取全局变量
        globalData = (GlobalData) getApplication();
        //创建apk下载路径
        createApkDownloadDir();
        initViews();
//        initData();
        getPrograms();
        //注册广播
        registeReceiver();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"onRestart");
        if (sp_oderNo != null){
            sp_oderNo.setSelection(0);
        }
        if (et_enter_operator != null){
            et_enter_operator.setText("");
        }
    }

    //初始化布局
    private void initViews(){
        et_enter_operator = (EditText) findViewById(R.id.et_enter_operator);
        sp_oderNo = (Spinner) findViewById(R.id.sp_oderNo);
        et_enter_operator.setOnEditorActionListener(this);
    }

    //初始化数据
    private void initData(List<Program> programs){
        //TODO 获取数据库中工单列表
        spinnerList = new ArrayList<String>();
        spinnerList.add("请选择工单号");
        if (programs != null && (programs.size() > 0)){
            for (int i=0;i < programs.size();i++){
                spinnerList.add(programs.get(i).getWork_order());
            }
        }
        //适配器
        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,spinnerList);
        //加载适配器
        sp_oderNo.setAdapter(spinnerAdapter);
        sp_oderNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"sp_oderNo--"+sp_oderNo.getSelectedItem());
                et_enter_operator.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //操作员输入框监听事件
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //回车键
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == event.KEYCODE_ENTER)){

            Log.d(TAG,"event.getAction()::"+event.getAction());
            Log.d(TAG,"event.getKeyCode()::"+event.getKeyCode());

            if (!TextUtils.isEmpty(v.getText().toString().trim())){
                //扫描内容
                String scanValue = String.valueOf(((EditText) v).getText());
                scanValue = scanValue.replaceAll("\r", "");
                Log.i(TAG, "scan Value:" + scanValue);

                if (sp_oderNo.getSelectedItemPosition() <= 0){
                    Toast.makeText(this,"请先选择工单号!!!",Toast.LENGTH_LONG).show();
                    //清空操作员，等待再扫描
//                  et_enter_operator.setText("");
                    v.setText("");
                }else {
                    //显示加载框
                    progressDialog = ProgressDialog.show(this,"请稍等!",
                            "正在加载数据...",true);
                    //获取工单号和操作员
                    final int orderIndex = sp_oderNo.getSelectedItemPosition();
                    final String orderNum = sp_oderNo.getSelectedItem().toString().trim();
//                                final String operatorNum=et_enter_operator.getText().toString().trim();
                    final String operatorNum = scanValue;
                    Log.d(TAG,"onEditorAction::["+orderNum+"]--["+operatorNum+"]"+"--"+orderIndex);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //更新app版本
                            boolean result=updateApp();
                            Message message=Message.obtain();
                            if (result){
                                //获取工单号对应的料号表
                                List<MaterialItem> materialItems=new DBService().getMaterial(mProgramList.get(orderIndex-1).getProgramID());
                                globalData.setMaterialItems(materialItems);
                                //查找数据库判断操作员类型,并跳转到对应的操作页面
                                Operator operator=new DBService().getCurOperator(operatorNum);
                                //未获取到工单号对应的料号表
                                if (materialItems.size() <= 0){
                                    message.what=NET_MATERIAL_FALL;
                                }else {
                                    if (null != operator){
                                        Log.d(TAG,operator.getOperator());
                                        if (operator.isEnabled() == 0){
                                            //成功获取料号表,但是操作员处于离职
                                            message.what=OPERATOR_DISSMISS;
                                        }else if (operator.isEnabled() == 1){
                                            //成功获取料号表,操作员在职
                                            message.what=operator.getType();
                                            curOrderNum=orderNum;
                                            curOperatorNum=operatorNum;
                                        }
                                    }else {
                                        //不存在该操作员,重新扫描
                                        message.what=OPERATOR_NULL;
                                    }
                                }
                            }else {
                                message.what=UPDATE_APK;
                            }
                            //发送消息
                            enterHandler.sendMessage(message);
                        }
                    }).start();

                }
            }else {
                Toast.makeText(this,"请重新扫描操作员工号!!!",Toast.LENGTH_LONG).show();
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
                        et_enter_operator.requestFocus();
                        et_enter_operator.setText("");
                        break;
                }
            }
        });
        infoDialog.show();

        return true;
    }

    //获取工单号
    private void getPrograms(){
        progressDialog = ProgressDialog.show(this,"请稍候!","正在加载工单...",true);
        progressDialog.setCanceledOnTouchOutside(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Program> programList=new DBService().getProgramOrder();
                Message message=Message.obtain();
                message.what=SET_ORDER;
                mProgramList=programList;
                //发送消息
                enterHandler.sendMessage(message);
            }
        }).start();
    }

    //更新app
    private boolean updateApp(){
        boolean result;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.jimi.smt.eps_appclient",0);
            int mVersionCode=packageInfo.versionCode;
            String mVersionName=packageInfo.versionName;
            EpsAppVersion epsAppVersion=DBService.getDbService().getAppVersion();
            if (epsAppVersion == null){
                //弹出网络不好
                showInfo("提示", "请确认当前网络是否正常\n或工单号是否正确!!!");
                result = false;
            }else {
                Log.d(TAG,"mVersionCode-"+mVersionCode+"\n"+"serviceCode-"+epsAppVersion.getVersionCode());
                Log.d(TAG,"mVersionName-"+mVersionName+"\n"+"serviceName-"+epsAppVersion.getVersionName());
                if (epsAppVersion.getVersionCode() <= mVersionCode){
                    result = true;
                }else {
                    //服务器上的版本号大于该版本,则更新
                    Intent intent=new Intent(getApplicationContext(), UpdateAppService.class);
                    intent.putExtra("apkUrl",Constants.DOWNLOAD_URL+"/"+epsAppVersion.getVersionName()+".apk");
                    intent.putExtra("apkName",epsAppVersion.getVersionName()+".apk");
                    startService(intent);

                    result = false;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
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
                //刷新工单号列表
                getPrograms();
                //获取上一个活动返回的工单号
                Bundle bundle=data.getExtras();
                String curOrderNum=bundle.getString("orderNum");
                Log.d(TAG,"orderNum----"+curOrderNum);
                break;
            default:
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
        UpdateApkReceiver apkReceiver=new UpdateApkReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.jimi.smt.eps_appclient.UPDATE");
        registerReceiver(apkReceiver,intentFilter);
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
