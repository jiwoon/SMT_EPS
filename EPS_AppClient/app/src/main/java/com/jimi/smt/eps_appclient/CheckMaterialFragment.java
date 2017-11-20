package com.jimi.smt.eps_appclient;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;

import java.util.ArrayList;
import java.util.List;


public class CheckMaterialFragment extends Fragment implements OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    private GlobalData globalData;

    //检料视图
    private View vCheckMaterialFragment;

    //操作员　站位　料号
    private TextView edt_Operation;
    private EditText edt_LineSeat;
    private EditText edt_Material;
    private TextView tv_Result,tv_Remark,tv_LastInfo;
    //当前扫描的站位,料号
    private String curLineSeat,curMaterial;
    //错误结果的对应料号
    private MaterialItem curOrgMaterialTtem;
    //当前料号表中的站位,料号
    private String curOrgSeatNo;
    private String curOrgMaterialNo;

    //当前检料时用到的排位料号表
    private List<MaterialItem> lCheckMaterialItems = new ArrayList<MaterialItem>();

    //当前检料项
    private int curCheckMaterialId = -1;
    private String FileId;
    private GlobalFunc globalFunc;

    private boolean first_checkAll_result;
    private static final int FIRST_CHECKALL_TRUE = 103;
    private static final int FIRST_CHECKALL_FALSE = 104;
    private int checkType;
    private Handler checkHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FIRST_CHECKALL_TRUE:
                    setFirst_checkAll_result(true);
                    break;
                case FIRST_CHECKALL_FALSE:
                    setFirst_checkAll_result(false);
                    edt_LineSeat.setText("");
                    if (checkType == 0){
                        showInfo("提示","IPQC未做首次全检","请联系管理员！");
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vCheckMaterialFragment = inflater.inflate(R.layout.checkmaterial_layout, container, false);
        //获取工单号和操作员
        Intent intent=getActivity().getIntent();
        savedInstanceState=intent.getExtras();
        Log.i(TAG,"curOderNum::"+savedInstanceState.getString("orderNum")+" -- curOperatorNUm::"+savedInstanceState.getString("operatorNum"));

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperator(savedInstanceState.getString("operatorNum"));
//        globalData.setOperType(Constants.CHECKMATERIAL);
        globalFunc = new GlobalFunc(getActivity());

        initViews(savedInstanceState);
        initData();//初始化数据
        initEvents();

        return vCheckMaterialFragment;
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_check_order= (TextView) vCheckMaterialFragment.findViewById(R.id.tv_check_order);
        edt_Operation = (TextView) vCheckMaterialFragment.findViewById(R.id.tv_check_Operation);
        edt_LineSeat = (EditText) vCheckMaterialFragment.findViewById(R.id.edt_lineseat);
        edt_Material = (EditText) vCheckMaterialFragment.findViewById(R.id.edt_material);
        tv_Result= (TextView) vCheckMaterialFragment.findViewById(R.id.tv_Result);
        tv_Remark= (TextView) vCheckMaterialFragment.findViewById(R.id.tv_Remark);
        tv_LastInfo= (TextView) vCheckMaterialFragment.findViewById(R.id.tv_LastInfo);

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
                if (hasFocus){
                    checkType = 0;
                    //判断是否首次全检
                    getFirstCheckAllResult(lCheckMaterialItems.get(0).getFileId());
                    Log.d(TAG,"globalData-OperType:"+globalData.getOperType());
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
            FileId=materialItem.getFileId();
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
            lCheckMaterialItems.add(feedMaterialItem);
        }
        curCheckMaterialId=-1;
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
                    if (globalFunc.isNetWorkConnected()){
                        if (isFirst_checkAll_result()){
                            //扫描内容
                            String scanValue = String.valueOf(((EditText) textView).getText());
                            scanValue = scanValue.replaceAll("\r", "");
                            Log.i(TAG, "scan Value:" + scanValue);
                            textView.setText(scanValue);

                            //将扫描的内容更新至列表中
                            switch (textView.getId()) {

                                case R.id.edt_lineseat:
                                    //站位
                                    String scanLineSeat=scanValue;
                                    if (scanValue.length()>=8){
                                        scanLineSeat=scanValue.substring(4,6)+"-"+scanValue.substring(6,8);
                                    }
                                    scanValue=scanLineSeat;
                                    checkAgain();
                                    for (int j = 0; j < lCheckMaterialItems.size(); j++) {
                                        MaterialItem materialItem = lCheckMaterialItems.get(j);
                                        if (materialItem.getOrgLineSeat().equalsIgnoreCase(scanValue)) {
                                            curCheckMaterialId = j;
                                            curOrgMaterialNo = materialItem.getOrgMaterial();
                                            materialItem.setScanLineSeat(scanValue);
                                        }
                                    }
                                    if (curCheckMaterialId < 0) {
                                        curLineSeat=scanValue;
                                        curOrgSeatNo="";
                                        curOrgMaterialNo="";
                                        showCheckMaterialResult(1,"排位表不存在此站位！",0);
                                        return true;
                                    }
                                    curLineSeat=scanValue;
                                    edt_Material.requestFocus();
                                    break;
                                case R.id.edt_material:
                                    //料号
                                    if (scanValue.indexOf("@") != -1) {
                                        scanValue = scanValue.substring(0, scanValue.indexOf("@"));
                                        textView.setText(scanValue);
                                    }
                                    //扫描到的站位
                                    String scanSeatNo=curLineSeat;
                                    //扫到的料号
                                    curMaterial=scanValue;
                                    curCheckMaterialId = -1;
                                    ArrayList<Integer> lineSeats=new ArrayList<Integer>();
                                    for (int k = 0;k < lCheckMaterialItems.size();k++){
                                        MaterialItem materialItem=lCheckMaterialItems.get(k);
                                        if (materialItem.getOrgMaterial().equalsIgnoreCase(scanValue)){
                                            //料号存在
                                            lineSeats.add(k);
                                            if (!materialItem.getOrgLineSeat().equalsIgnoreCase(scanSeatNo)){
                                                //料号对应的站位与扫描的站位不同
                                                curCheckMaterialId = -2;
                                                curOrgSeatNo = materialItem.getOrgLineSeat();
                                            }
                                        }
                                    }
                                    //遍历所有相同料号的站位,判断与扫描到的站位是否一样
                                    for (int j=0;j < lineSeats.size();j++){
                                        if (lCheckMaterialItems.get(lineSeats.get(j)).getOrgLineSeat().equals(scanSeatNo)){
                                            curCheckMaterialId = lineSeats.get(j);
                                        }
                                    }
                                    //显示结果
                                    String Remark="";
                                    if (curCheckMaterialId < 0){
                                        curOrgSeatNo = scanSeatNo;
                                        if (curCheckMaterialId == -2){
                                            Remark="站位与料号不对应！";
                                            showCheckMaterialResult(1,Remark,1);
                                        }else {
                                            Remark="排位表不存在此料号！";
                                            showCheckMaterialResult(1,Remark,1);
                                        }
                                    }else {
                                        MaterialItem materialItem=lCheckMaterialItems.get(curCheckMaterialId);
                                        materialItem.setScanMaterial(scanValue);
                                        materialItem.setResult("PASS");
                                        materialItem.setRemark("站位和料号正确!");
                                        showCheckMaterialResult(0,"站位和料号正确!",1);
                                    }
                                    break;
                            }
                        }else {
                            showInfo("提示","IPQC未做首次全检","请联系管理员！");
                        }
                    }else {
                        globalFunc.showInfo("警告","请检查网络连接是否正常!","请连接网络!");
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
    private void showCheckMaterialResult(int checkResult,String strRemark,int logType) {
        String result="";
        switch (checkResult) {
            case 0:
                tv_Result.setBackgroundColor(Color.GREEN);
                result="PASS";
                tv_Remark.setTextColor(Color.argb(255,102,153,0));
                break;
            case 1:
                tv_Result.setBackgroundColor(Color.RED);
                result="FAIL";
                tv_Remark.setTextColor(Color.RED);
                break;
        }
        tv_Remark.setText(strRemark);
        tv_Result.setText(result);
        tv_LastInfo.setText("扫描结果\r\n站位:"+curLineSeat+"\r\n"+ "料号:"+curMaterial);

//        Log.d(TAG,"扫描结果\r\n站位:"+curLineSeat+"\r\n"+"料号:"+curMaterial);

        //保存至数据库日志
        MaterialItem materialItem;
        if (curCheckMaterialId >= 0) {
            materialItem = lCheckMaterialItems.get(curCheckMaterialId);
        } else{
            materialItem = new MaterialItem(
                    FileId,
                    curOrgSeatNo,
                    curOrgMaterialNo,
                    String.valueOf(edt_LineSeat.getText()),
                    String.valueOf(String.valueOf(edt_Material.getText())),
                    result,
                    strRemark);
        }
        Log.d(TAG,"curCheckMaterialId:"+curCheckMaterialId);
        Log.d(TAG,"materialItem-"+materialItem.getOrgLineSeat()+"-"+materialItem.getOrgMaterial());
        //添加操作日志
        globalData.setOperType(Constants.CHECKMATERIAL);
        globalFunc.AddDBLog(globalData, materialItem);
        //添加显示日志
        if (logType == 1){
            globalData.setUpdateType(Constants.CHECKMATERIAL);
            globalFunc.updateVisitLog(globalData,materialItem);
        }
        //自动清空站位和料号以便下一项检测
        clearAndSetFocus();
    }

    //判断是否全部进行了首次全检
    private void getFirstCheckAllResult(final String programId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"getFirstCheckAllResult-programId-"+programId);
                boolean result = new DBService().isOrderFirstCheckAll(programId);
                Message message = Message.obtain();
                if (result){
                    message.what = FIRST_CHECKALL_TRUE;
                }else {
                    message.what = FIRST_CHECKALL_FALSE;
                }
                checkHandler.sendMessage(message);
            }
        }).start();
    }

    //IPQC未做首次全检
    public void showInfo(String title, String message, final String netFailToastStr){
        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};
        //标题和内容
        String titleMsg[]=new String[]{title,message};
        //内容的样式
        int msgStype[]=new int[]{22, Color.RED};
        InfoDialog infoDialog=new InfoDialog(getActivity(),
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStype);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        dialog.dismiss();
//                        Toast.makeText(getActivity(),netFailToastStr,Toast.LENGTH_LONG).show();
                        clearAndSetFocus();
                        checkType = 1;
                        getFirstCheckAllResult(lCheckMaterialItems.get(0).getFileId());
                        break;
                }
            }
        });
        infoDialog.show();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 重新检下一个料
     */
    private void checkAgain(){
        Log.i(TAG, "testAgain");
        initData();
        tv_Remark.setText("");
        tv_Result.setText("");
        tv_Result.setBackgroundColor(Color.TRANSPARENT);
        curLineSeat="";
        curMaterial="";
        edt_Material.setText("");

        curOrgSeatNo="";
        curOrgMaterialNo="";
    }

    private void clearAndSetFocus(){
        edt_LineSeat.setText("");
        edt_Material.setText("");
        edt_LineSeat.requestFocus();
    }

    public boolean isFirst_checkAll_result() {
        return first_checkAll_result;
    }

    public void setFirst_checkAll_result(boolean first_checkAll_result) {
        this.first_checkAll_result = first_checkAll_result;
    }
}
