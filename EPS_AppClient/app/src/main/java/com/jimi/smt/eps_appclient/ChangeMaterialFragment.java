package com.jimi.smt.eps_appclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caobotao on 16/1/4.
 * @ 描述:换料
 */
public class ChangeMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    private GlobalData globalData;

    //换料视图
    private View vChangeMaterialFragment;
    //操作员　站位　料号
    private TextView edt_Operation;
    private EditText edt_LineSeat;
    private EditText edt_OrgMaterial;
    private EditText edt_ChgMaterial;
    private TextView tv_Result,tv_Remark,tv_lastInfo;

    //当前的站位，线上料号，更换料号
    private String curLineSeat,curOrgMaterial,curChgMaterial;

    //当前上料时用到的排位料号表x
    private List<MaterialItem> lChangeMaterialItem = new ArrayList<MaterialItem>();

    //当前换料项
    private int curChangeMaterialId = -1;
    private String FileId;
    //成功换料的站位
    private String sucSeatNo;
    //成功换料时的时间
    private long sucTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vChangeMaterialFragment = inflater.inflate(R.layout.changematerial_layout, container, false);
        //获取工单号和操作员
        Intent intent=getActivity().getIntent();
        savedInstanceState=intent.getExtras();
        Log.i(TAG,"curOderNum::"+savedInstanceState.getString("orderNum")+" -- curOperatorNUm::"+savedInstanceState.getString("operatorNum"));

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperator(savedInstanceState.getString("operatorNum"));
        globalData.setOperType(Constants.CHANGEMATERIAL);

        initViews(savedInstanceState);
        initEvents();
        initData();

        return vChangeMaterialFragment;
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_change_order= (TextView) vChangeMaterialFragment.findViewById(R.id.tv_change_order);
        edt_Operation = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_change_Operation);
        edt_LineSeat = (EditText) vChangeMaterialFragment.findViewById(R.id.edt_lineseat);
        edt_OrgMaterial = (EditText) vChangeMaterialFragment.findViewById(R.id.edt_OrgMaterial);
        edt_ChgMaterial = (EditText) vChangeMaterialFragment.findViewById(R.id.edt_ChgMaterial);
        tv_Result = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_Result);
        tv_lastInfo= (TextView) vChangeMaterialFragment.findViewById(R.id.tv_LastInfo);
        tv_Remark= (TextView) vChangeMaterialFragment.findViewById(R.id.tv_Remark);

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

        //点击结果后换下一个料
        tv_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNextMaterial();
                clearResultRemark();
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
        curChangeMaterialId = -1;
        //填充数据
        lChangeMaterialItem.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();

        for (MaterialItem materialItem : materialItems) {
            FileId=materialItem.getFileId();
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
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
                    //扫描内容
                    String strValue = String.valueOf(((EditText) textView).getText());
                    strValue = strValue.replaceAll("\r", "");
                    Log.i(TAG, "strValue:" + strValue);
                    textView.setText(strValue);

                    //将扫描的内容更新至列表中
                    switch (textView.getId()) {
                        case R.id.edt_lineseat:
                            changeNextMaterial();
                            String scanLineSeat=strValue;
                            if (strValue.length()>=8){
                                scanLineSeat=strValue.substring(4,6)+"-"+strValue.substring(6,8);
                            }
                            strValue=scanLineSeat;
                            edt_LineSeat.setText(strValue);
                            //判断在该5分钟内,该站位是否成功换过料
                            if (scanLineSeat.equalsIgnoreCase(sucSeatNo)){
                                if (System.currentTimeMillis() - sucTime < 5000){

                                    return true;
                                }
                            }
                            //站位
                            //String scanLineSeat=strValue.substring(4,6)+"-"+strValue.substring(6,8);
                            for (int j = 0; j < lChangeMaterialItem.size(); j++) {
                                MaterialItem materialItem = lChangeMaterialItem.get(j);
                                if (materialItem.getOrgLineSeat().equalsIgnoreCase(strValue)) {
                                    curChangeMaterialId = j;
                                    materialItem.setScanLineSeat(strValue);
                                }
                            }
                            if (curChangeMaterialId < 0) {
                                curLineSeat=strValue;
                                displayResult(1,"","排位表不存在此站位！");
                                return true;
                            }
                            curLineSeat=strValue;
                            edt_OrgMaterial.requestFocus();
                            break;
                        case R.id.edt_OrgMaterial:
                            if (strValue.indexOf("@") != -1) {
                                strValue = strValue.substring(0, strValue.indexOf("@"));
                                textView.setText(strValue);
                            }
                            curOrgMaterial=strValue;
                            //比对线上料号,包括替换料
                            String orgLineSeat="";//料号表中的站位
                            //先获取扫描到的站位
//                            String scanSeatNo=lChangeMaterialItem.get(curChangeMaterialId).getScanLineSeat();
                            String scanSeatNo = curLineSeat;
                            Log.d(TAG,"scanSeatNo-"+scanSeatNo);
                            ArrayList<Integer> lineSeats=new ArrayList<Integer>();
                            curChangeMaterialId=-1;
                            for (int j = 0;j < lChangeMaterialItem.size();j++){
                                MaterialItem materialItem = lChangeMaterialItem.get(j);
                                //料号存在
                                if (materialItem.getOrgMaterial().equalsIgnoreCase(curOrgMaterial)){
                                    lineSeats.add(j);
                                    //原始站位与扫描到的站位一样
                                    if (!materialItem.getOrgLineSeat().equalsIgnoreCase(scanSeatNo)){
                                        //原始站位与扫描到的站位不一样
                                        curChangeMaterialId=-2;
                                    }
                                    orgLineSeat=materialItem.getOrgLineSeat();
                                }
                            }
                            //遍历所有相同料号的站位,判断与扫描到的站位是否一样
                            for (int k=0;k < lineSeats.size();k++){
                                if (lChangeMaterialItem.get(lineSeats.get(k)).getOrgLineSeat().equals(scanSeatNo)){
                                    //相同
                                    curChangeMaterialId = lineSeats.get(k);
                                }
                            }

                            //扫描到的料号不存在表中
                            if (curChangeMaterialId < 0){
                                if (curChangeMaterialId == -2){
                                    displayResult(1,orgLineSeat,"料号与站位不对应！");
                                }else {
                                    displayResult(1,orgLineSeat,"原始料号与排位表不相符！");
                                }
                                return true;
                            }

                            //清空站位数组
                            lineSeats.clear();
                            edt_ChgMaterial.requestFocus();

                            break;
                        case R.id.edt_ChgMaterial:
                            if (strValue.indexOf("@") != -1) {
                                strValue = strValue.substring(0, strValue.indexOf("@"));
                                textView.setText(strValue);
                            }
                            curChgMaterial=strValue;
                            //比对更换料号
                            if (curChangeMaterialId >= 0) {
                                if (edt_ChgMaterial.getText().toString().equals(edt_OrgMaterial.getText().toString())) {
                                    displayResult(0,lChangeMaterialItem.get(curChangeMaterialId).getOrgLineSeat(),
                                            "换料成功!");
                                } else {
                                    displayResult(1,lChangeMaterialItem.get(curChangeMaterialId).getOrgLineSeat(),
                                            "更换的料号与线上料号不同！");
                                }
                            }
                            edt_LineSeat.requestFocus();

                            break;
                    }
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    /**
     * @param i
     */
    private void displayResult(int i,String orgLineSeat,String remark) {
        Log.i(TAG, "displayResult");
        String Result = "";
        switch (i) {
            case 0:
                tv_Result.setBackgroundColor(Color.GREEN);
                tv_Remark.setTextColor(Color.argb(255,102,153,0));
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
        new GlobalFunc().AddDBLog(globalData,
                new MaterialItem(
                        FileId,
                        orgLineSeat,
                        String.valueOf(edt_OrgMaterial.getText()),
                        String.valueOf(edt_LineSeat.getText()),
                        String.valueOf(edt_ChgMaterial.getText()),
                        Result,
                        remark));

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

    private void clearAndSetFocus(){
        edt_LineSeat.setText("");
        edt_OrgMaterial.setText("");
        edt_ChgMaterial.setText("");
        edt_LineSeat.requestFocus();

        curLineSeat="";
        curOrgMaterial="";
        curChgMaterial="";
        curChangeMaterialId=-1;
    }

    private void clearResultRemark(){
        tv_Result.setText("");
        tv_Remark.setText("");
    }


}
