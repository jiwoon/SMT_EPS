package com.jimi.smt.eps_appclient;

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
 */
public class ChangeMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    GlobalData globalData;

    //换料视图
    View vChangeMaterialFragment;
    //操作员　站位　料号
    private EditText edt_Operation, edt_LineSeat, edt_OrgMaterial, edt_ChgMaterial;
    private TextView tv_Result,tv_Remark,tv_lastInfo;

    String curLineSeat,curOrgMaterial,curChgMaterial;

    //当前上料时用到的排位料号表x
    private List<MaterialItem> lChangeMaterialItem = new ArrayList<MaterialItem>();

    //当前换料项
    int curChangeMaterialId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vChangeMaterialFragment = inflater.inflate(R.layout.changematerial_layout, container, false);

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperType(Constants.CHANGEMATERIAL);

        initViews();
        initEvents();
        initData();

        return vChangeMaterialFragment;
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews() {
        Log.i(TAG, "initViews");
        edt_Operation = (EditText) vChangeMaterialFragment.findViewById(R.id.edt_Operation);
        edt_LineSeat = (EditText) vChangeMaterialFragment.findViewById(R.id.edt_lineseat);
        edt_OrgMaterial = (EditText) vChangeMaterialFragment.findViewById(R.id.edt_OrgMaterial);
        edt_ChgMaterial = (EditText) vChangeMaterialFragment.findViewById(R.id.edt_ChgMaterial);
        tv_Result = (TextView) vChangeMaterialFragment.findViewById(R.id.tv_Result);
        tv_lastInfo= (TextView) vChangeMaterialFragment.findViewById(R.id.tv_LastInfo);
        tv_Remark= (TextView) vChangeMaterialFragment.findViewById(R.id.tv_Remark);
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG, "initEvents");
        edt_Operation.setOnEditorActionListener(this);
        edt_LineSeat.setOnEditorActionListener(this);
        edt_OrgMaterial.setOnEditorActionListener(this);
        edt_ChgMaterial.setOnEditorActionListener(this);

        //点击结果后换下一个料
        tv_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNextMaterial();
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
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
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
                case KeyEvent.ACTION_DOWN:
                    //扫描内容
                    String strValue = String.valueOf(((EditText) textView).getText());
                    strValue = strValue.replaceAll("\r", "");
                    Log.i(TAG, "strValue:" + strValue);
                    textView.setText(strValue);

                    //将扫描的内容更新至列表中
                    switch (textView.getId()) {
                        case R.id.edt_Operation:
                            globalData.setOperator(strValue);
                            break;
                        case R.id.edt_lineseat:
                            changeNextMaterial();
                            //站位
                            //String scanLineSeat=strValue.substring(4,6)+"-"+strValue.substring(6,8);
                            for (int j = 0; j < lChangeMaterialItem.size(); j++) {
                                MaterialItem materialItem = lChangeMaterialItem.get(j);
                                if (materialItem.getOrgLineSeat().equalsIgnoreCase(strValue)) {
                                    curChangeMaterialId = j;
                                }
                            }
                            if (curChangeMaterialId < 0) {
                                displayResult(1,"排位表不存在此站位！");
                            }
                            curLineSeat=strValue;

                            break;
                        case R.id.edt_OrgMaterial:
                            if (strValue.indexOf("@") != -1) {
                                strValue = strValue.substring(0, strValue.indexOf("@"));
                                textView.setText(strValue);
                            }
                            curOrgMaterial=strValue;
                            //比对线上料号
                            if (curChangeMaterialId >= 0) {
                                MaterialItem materialItem = lChangeMaterialItem.get(curChangeMaterialId);
                                //线上的料号与排位表的值不对时显示结果
                                if (!materialItem.getOrgMaterial().equalsIgnoreCase(strValue)) {
                                    displayResult(1,"原始料号与排位表不相符！");
                                }
                            }

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
                                    displayResult(0,"");
                                } else {
                                    displayResult(1,"更换的料号与线上料号不相符！");
                                }
                            }

                            break;
                    }
                    return false;
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * @param i
     */
    private void displayResult(int i,String remark) {
        Log.i(TAG, "displayResult");
        String Result = "";
        switch (i) {
            case 0:
                tv_Result.setBackgroundColor(Color.GREEN);
                Result = "PASS";
                break;
            case 1:
                tv_Result.setBackgroundColor(Color.RED);
                Result = "FAIL";

                break;
        }
        tv_Result.setText(Result);
        tv_Remark.setText(remark);
        tv_lastInfo.setText("扫描结果: 站位:"+curLineSeat+"\r\n原始料号:"+curOrgMaterial+"\r\n替换料号:"+curChgMaterial);

        edt_LineSeat.setText("");
        edt_OrgMaterial.setText("");
        edt_ChgMaterial.setText("");
        edt_Operation.requestFocus();

        new GlobalFunc().AddDBLog(globalData,
                new MaterialItem("",
                        String.valueOf(edt_OrgMaterial.getText()),
                        String.valueOf(edt_LineSeat.getText()),
                        String.valueOf(edt_ChgMaterial.getText()),
                        Result,
                        ""));

    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 测试下一项
     */
    private void changeNextMaterial() {
        Log.i(TAG, "changeNextMaterial");
        tv_Result.setBackgroundColor(Color.TRANSPARENT);
        tv_Result.setText("");
        tv_Remark.setText("");
        tv_lastInfo.setText("");
        curLineSeat="";
        curOrgMaterial="";
        curChgMaterial="";
        curChangeMaterialId=-1;

    }


}
