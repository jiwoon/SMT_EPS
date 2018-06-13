package com.jimi.smt.eps_appclient.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.Views.InfoDialog;

public class AdminActivity extends Activity implements View.OnClickListener {

    private final String TAG = "AdminActivity";
    private static final int EXIT = 210;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private String curOrderNum = "";
    private String curOperatorNum = "";
    private GlobalFunc globalFunc;
    private GlobalData globalData;

    @SuppressLint("HandlerLeak")
    private Handler adminHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXIT:
                    isExit = false;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin);
        //获取工单号和操作员
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        if (savedInstanceState != null) {
            curOrderNum = savedInstanceState.getString("orderNum");
            curOperatorNum = savedInstanceState.getString("operatorNum");
        }
        globalFunc = new GlobalFunc(getApplicationContext());
        globalData = (GlobalData) getApplication();

        initView();
    }

    private void initView() {
        findViewById(R.id.iv_admin_back).setOnClickListener(this);
        findViewById(R.id.rl_factory).setOnClickListener(this);
        findViewById(R.id.rl_ipqc).setOnClickListener(this);
        findViewById(R.id.rl_ware).setOnClickListener(this);
    }

    //返回主页
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            Message message = Message.obtain();
            message.what = EXIT;
            adminHandler.sendMessageDelayed(message, 2000);
        } else {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_admin_back:
                exit();
                break;
            case R.id.rl_ware:
                globalData.setAdminOperType(Constants.ADMIN_WARE_HOUSE);
                Intent warehouse = new Intent(getApplicationContext(), WareHouseActivity.class);
                Bundle warehouseBundle = new Bundle();
                warehouseBundle.putString("orderNum", curOrderNum);
                warehouseBundle.putString("operatorNum", curOperatorNum);
                warehouse.putExtras(warehouseBundle);
                startActivityForResult(warehouse, Constants.ADMIN_ACTIVITY_RESULT);
                break;
            case R.id.rl_factory:
                globalData.setAdminOperType(Constants.ADMIN_FACTORY);
                Intent factory = new Intent(getApplicationContext(), FactoryLineActivity.class);
                Bundle factoryBundle = new Bundle();
                factoryBundle.putString("orderNum", curOrderNum);
                factoryBundle.putString("operatorNum", curOperatorNum);
                factory.putExtras(factoryBundle);
                startActivityForResult(factory, Constants.ADMIN_ACTIVITY_RESULT);
                break;
            case R.id.rl_ipqc:
                globalData.setAdminOperType(Constants.ADMIN_QC);
                Intent qc = new Intent(getApplicationContext(), QCActivity.class);
                Bundle qcBundle = new Bundle();
                qcBundle.putString("orderNum", curOrderNum);
                qcBundle.putString("operatorNum", curOperatorNum);
                qc.putExtras(qcBundle);
                startActivityForResult(qc, Constants.ADMIN_ACTIVITY_RESULT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ADMIN_ACTIVITY_RESULT:
                //先判断网络是否连接
                boolean net = globalFunc.isNetWorkConnected();
                if (!net) {
                    showInfo("警告", "请检查网络连接是否正常!");
                }
                break;
            default:
                break;
        }
    }

    //弹出提示消息窗口
    private boolean showInfo(String title, String message) {
        //对话框所有控件id
        int itemResIds[] = new int[]{R.id.dialog_title_view,
                R.id.dialog_title, R.id.tv_alert_info, R.id.info_trust};
        //标题和内容
        String titleMsg[] = new String[]{title, message};
        //内容的样式
        int msgStype[] = new int[]{22, Color.RED};
        InfoDialog infoDialog = new InfoDialog(this,
                R.layout.info_dialog_layout, itemResIds, titleMsg, msgStype);

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
        infoDialog.show();

        return true;
    }


}
