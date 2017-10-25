package com.jimi.smt.eps_appclient.Activity;

import android.app.Activity;
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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Adapter.WareHouseAdapter;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.GlobalData;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:WareHouseActivity
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/19 19:29
 * 描述: 仓库 Activity
 */
public class WareHouseActivity extends Activity implements View.OnClickListener, TextView.OnEditorActionListener {

    private final String TAG="WareHouseActivity";

    private String curOrderNum;//当前工单号
    private String curOperatorNUm;//当前操作员
    private GlobalData globalData;
//    private List<MaterialItem> materialItems=new ArrayList<MaterialItem>();//料号表
    private List<MaterialItem> wareHouseMaterialItems=new ArrayList<MaterialItem>();//料号表
    private ListView lv_ware_materials;//所有料号列表
    private EditText et_ware_scan_material;//扫描的料号
    private WareHouseAdapter wareHouseAdapter;
    private InfoDialog infoDialog;//弹出料号对应站位
    private int sucIssueCount = 0;//成功发料个数
    private int allCount = 0;//总数

    private DissMissThread mDissMissThread;//
    private final int DISSMIASS_DIALOG = 120;//取消站位弹出窗
    private Handler dissmissDialogHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DISSMIASS_DIALOG:
                    //取消窗口
                    infoDialog.dismiss();
                    /*et_ware_scan_material.requestFocus();
                    et_ware_scan_material.setText("");*/
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ware_house);
        initViews(savedInstanceState);
        initData();
    }

    //初始化数据
    private void initData(){
        //全局变量
        globalData = (GlobalData) getApplication();
        globalData.setOperator(curOperatorNUm);
        globalData.setOperType(Constants.STORE_ISSUE);
        List<MaterialItem> materialItems=globalData.getMaterialItems();
        //填充数据
        wareHouseMaterialItems.clear();
        for (MaterialItem materialItem: materialItems) {
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(),
                    materialItem.getOrgMaterial(), "", "", "", "");
            wareHouseMaterialItems.add(feedMaterialItem);
        }
        /*
        //测试用
        for (int i=0;i < 10;i++){
            MaterialItem feedMaterialItem = new MaterialItem(materialItems.get(i).getFileId(),
                    materialItems.get(i).getOrgLineSeat(), materialItems.get(i).getOrgMaterial(), "", "", "", "");
            wareHouseMaterialItems.add(feedMaterialItem);
        }
        */
        sucIssueCount = 0;
        allCount = wareHouseMaterialItems.size();
        //设置Adapter
        wareHouseAdapter = new WareHouseAdapter(getApplicationContext(),wareHouseMaterialItems);
        lv_ware_materials.setAdapter(wareHouseAdapter);
    }
    //初始化页面
    private void initViews(Bundle bundle){
        Intent intent=getIntent();
        bundle=intent.getExtras();
        curOrderNum=bundle.getString("orderNum");
        curOperatorNUm=bundle.getString("operatorNum");
        Log.i(TAG,"curOderNum::"+curOrderNum+" -- curOperatorNUm::"+curOperatorNUm);
        ImageView iv_ware_back= (ImageView) findViewById(R.id.iv_ware_back);
        TextView tv_ware_order= (TextView) findViewById(R.id.tv_ware_order);
        TextView tv_ware_operator= (TextView) findViewById(R.id.tv_ware_operator);
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
        switch (v.getId()){
            case R.id.iv_ware_back:
                this.finish();
                break;
        }
    }

    //扫料号的输入事件
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == event.KEYCODE_ENTER)){
            Log.d(TAG,"onEditorAction::"+v.getText());
            Log.d(TAG,"event.getAction()::"+event.getAction());

            if (!TextUtils.isEmpty(v.getText().toString().trim())){
                //扫描的内容
                String scanMaterial= String.valueOf(((EditText) v).getText());
                scanMaterial = scanMaterial.replaceAll("\r", "");
                Log.i(TAG,"sacnMaterial="+scanMaterial);
                //料号,若为二维码则提取@@前的料号
                //提取有效料号
                if (scanMaterial.indexOf("@") != -1) {
                    scanMaterial = scanMaterial.substring(0, scanMaterial.indexOf("@"));
                    Log.d(TAG,"scan料号="+scanMaterial);
                }
                ArrayList<Integer> lineSeatIndexs=new ArrayList<Integer>();
                ArrayList<String> lineSeatList=new ArrayList<String>();
                for (int i = 0;i < wareHouseMaterialItems.size();i++) {
                    MaterialItem materialItem=wareHouseMaterialItems.get(i);
                    if (materialItem.getOrgMaterial().equalsIgnoreCase(scanMaterial)){
                        materialItem.setScanMaterial(scanMaterial);
                        materialItem.setResult("PASS");
                        Log.d(TAG,"scan站位="+materialItem.getOrgLineSeat());
                        //获取料号对应的所有站位
                        lineSeatList.add(materialItem.getOrgLineSeat());
                        //获取对应站位的索引
                        lineSeatIndexs.add(i);
                        sucIssueCount++;
                        //TODO: 2017/10/21 将其置顶？
                        lv_ware_materials.setSelection(i);
                    }
                }
                if (lineSeatIndexs.size() > 0){
                    //写日志
                    setOperateLog(lineSeatIndexs,"");
                }
                //弹出站位
                showInfo(scanMaterial,lineSeatList);
                //启动子线程
                mDissMissThread=new DissMissThread();
                mDissMissThread.start();
                //刷新数据
                wareHouseAdapter.notifyDataSetChanged();
            }

        }
        return false;
    }

    //弹出提示站位窗口
    private boolean showInfo(String title,ArrayList<String> lineSeatList){
        //内容
        String message="";
        //内容的样式
        int msgStype[];
        if (lineSeatList.size() > 0){
            //存在站位,添加所有站位
            message="站位:";
            for (String lineSeat:lineSeatList) {
                message+="\n"+lineSeat;
            }
            msgStype=new int[]{23, Color.argb(255,102,153,0)};
        }else {
            //写日志
            setOperateLog(null,title);
            //站位不存在
            message="不存在该料号的站位!";
            //内容的样式
            msgStype=new int[]{22, Color.RED};
        }
        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};
        //标题和内容
        String titleMsg[]=new String[]{"料号:"+title,message};

        infoDialog = new InfoDialog(this,
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStype);
        //确定按钮点击事件
        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        dialog.dismiss();
                        /*et_ware_scan_material.requestFocus();
                        et_ware_scan_material.setText("");*/
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
                //停止线程
                if ((mDissMissThread != null) && (!mDissMissThread.isInterrupted())){
                    mDissMissThread.interrupt();
                    mDissMissThread=null;
                }
                //显示最终结果
                showIssueResult();
            }
        });
        infoDialog.show();

        return true;
    }

    //显示最终发料结果
    private void showIssueResult(){
        Log.d(TAG,"sucIssueCount-"+sucIssueCount
                +"\nwareHouseMaterialItems-"+wareHouseMaterialItems.size()+"\nallCount-"+allCount);
        if (sucIssueCount >= wareHouseMaterialItems.size() &&  sucIssueCount >= allCount){
            //显示最终结果
            boolean result = true;
            for (MaterialItem materialItem : wareHouseMaterialItems) {
                if (!materialItem.getResult().equalsIgnoreCase("PASS")){
                    result=false;
                    break;
                }
            }
            //弹出发料结果
            String titleMsg[];
            int msgStyle[];
            if (result){
                titleMsg=new String[]{"发料结果","PASS"};
                msgStyle=new int[]{66,Color.argb(255,102,153,0)};
            }else {
                titleMsg=new String[]{"发料失败，请检查!","FAIL"};
                msgStyle=new int[]{66,Color.RED};
            }
            showIssueInfo(titleMsg,msgStyle,result,1);
        }
    }
    //发料结果
    private void showIssueInfo(String[] titleMsg, int[] msgStyle, final boolean result, final int resultType){
        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};

        InfoDialog mInfoDialog=new InfoDialog(this,
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStyle);

        mInfoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        if (result){
                            dialog.dismiss();
                            et_ware_scan_material.requestFocus();
                            et_ware_scan_material.setText("");
                            initData();
                        }else{
                            dialog.dismiss();
                            et_ware_scan_material.requestFocus();
                            et_ware_scan_material.setText("");
                            if (resultType == 1){
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
        mInfoDialog.show();
    }

    //写日志
    private void setOperateLog(final ArrayList<Integer> integers, final String scanMaterial){
        GlobalFunc globalFunc = new GlobalFunc();
        if ((integers != null) && (integers.size() > 0)){
            for (int j = 0; j < integers.size(); j++){
                MaterialItem materialItem=wareHouseMaterialItems.get(integers.get(j));
                globalFunc.AddDBLog(globalData,materialItem);
            }
        }else {
            MaterialItem failMaterialItem=new MaterialItem(wareHouseMaterialItems.get(0).getFileId(),"","","",scanMaterial,"FAIL","不存在该料号的站位!");
            globalFunc.AddDBLog(globalData,failMaterialItem);
        }
    }

    //定时取消窗口子线程
    protected class DissMissThread extends Thread{
        @Override
        public void run() {
            while (!isInterrupted()){
                if ((infoDialog != null) && (infoDialog.isShowing())){
                    try {
                        Thread.sleep(3000);//休眠3s
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

}
