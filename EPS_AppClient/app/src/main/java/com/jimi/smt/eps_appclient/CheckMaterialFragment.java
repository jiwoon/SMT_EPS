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
import android.widget.TextView.OnEditorActionListener;

import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.ArrayList;
import java.util.List;


public class CheckMaterialFragment extends Fragment implements OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    GlobalData globalData;

    //检料视图
    private View vCheckMaterialFragment;

    //操作员　站位　料号
    private EditText edt_Operation, edt_LineSeat, edt_Material;
    private TextView tv_Result,tv_Remark,tv_LastInfo;

    String curLineSeat,curMaterial;

    //当前检料时用到的排位料号表
    List<MaterialItem> lCheckMaterialItems = new ArrayList<MaterialItem>();

    //当前检料项
    int curCheckMaterialId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vCheckMaterialFragment = inflater.inflate(R.layout.checkmaterial_layout, container, false);

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperType(Constants.CHECKMATERIAL);

        initViews();
        initData();//初始化数据
        initEvents();

        return vCheckMaterialFragment;
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews() {
        Log.i(TAG, "initViews");
        edt_Operation = (EditText) vCheckMaterialFragment.findViewById(R.id.edt_Operation);
        edt_LineSeat = (EditText) vCheckMaterialFragment.findViewById(R.id.edt_lineseat);
        edt_Material = (EditText) vCheckMaterialFragment.findViewById(R.id.edt_material);
        tv_Result= (TextView) vCheckMaterialFragment.findViewById(R.id.tv_Result);
        tv_Remark= (TextView) vCheckMaterialFragment.findViewById(R.id.tv_Remark);
        tv_LastInfo= (TextView) vCheckMaterialFragment.findViewById(R.id.tv_LastInfo);
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
        edt_Material.setOnEditorActionListener(this);

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
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
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
                case KeyEvent.ACTION_DOWN:
                    //扫描内容
                    String scanValue = String.valueOf(((EditText) textView).getText());
                    scanValue = scanValue.replaceAll("\r", "");
                    Log.i(TAG, "scan Value:" + scanValue);
                    textView.setText(scanValue);

                    //将扫描的内容更新至列表中
                    switch (textView.getId()) {
                        case R.id.edt_Operation:
                            //操作员格式不对
                            if (scanValue.length() != 10) {
                            }
                            globalData.setOperator(scanValue);
                            //edt_Operation.setEnabled(false);
                            break;
                        case R.id.edt_lineseat:
                            //站位
                            //String scanLineSeat=scanValue.substring(4,6)+"-"+scanValue.substring(6,8);
                            //feedMaterialItem.setScanLineSeat(scanValue);
//                            if (edt_LineSeat.length() != 7) {
//                                feedMaterialItem.setRemark("站位长度不正确");
//                            } else {
//                                feedMaterialItem.setRemark("");
//                            }
                            checkAgain();
                            for (int j = 0; j < lCheckMaterialItems.size(); j++) {
                                MaterialItem materialItem = lCheckMaterialItems.get(j);
                                if (materialItem.getOrgLineSeat().equalsIgnoreCase(scanValue)) {
                                    curCheckMaterialId = j;
                                }
                            }
                            curLineSeat=scanValue;
                            break;
                        case R.id.edt_material:
                            //料号
                            if (scanValue.indexOf("@") != -1) {
                                scanValue = scanValue.substring(0, scanValue.indexOf("@"));
                                textView.setText(scanValue);
                            }

                            curMaterial=scanValue;
                            String Remark="";
                            if (curCheckMaterialId != -1) {

                                MaterialItem checkMaterialItem = lCheckMaterialItems.get(curCheckMaterialId);
                                checkMaterialItem.setScanLineSeat(String.valueOf(edt_LineSeat.getText()));
                                checkMaterialItem.setScanMaterial(scanValue);
                                //比对站位和料号是否相等
                                if (checkMaterialItem.getOrgMaterial().equalsIgnoreCase(checkMaterialItem.getScanMaterial())) {
                                    checkMaterialItem.setResult("PASS");
                                    showCheckMaterialResult(0,"");
                                } else {
                                    checkMaterialItem.setResult("FAIL");
                                    showCheckMaterialResult(1, "");
                                    if (!checkMaterialItem.getOrgMaterial().equalsIgnoreCase(checkMaterialItem.getScanMaterial())) {
                                        Remark="料号与排位表不相符";
                                        checkMaterialItem.setRemark(Remark);
                                        showCheckMaterialResult(1, Remark);
                                    }
                                }
                            }
                            else{
                                Remark="排位表不存在此站位！";
                                showCheckMaterialResult(1,Remark);
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

    private void showCheckMaterialResult(int checkResult,String strRemark) {
        String Result="";
        switch (checkResult) {
            case 0:
                tv_Result.setBackgroundColor(Color.GREEN);
                Result="PASS";
                break;
            case 1:
                tv_Result.setBackgroundColor(Color.RED);
                Result="FAIL";
                break;
        }
        tv_Remark.setText(strRemark);
        tv_Result.setText(Result);

        //自动清空站位和料号以便下一项检测
        tv_LastInfo.setText("扫描结果\r\n站位:"+curLineSeat+"\r\n"+
            "料号:"+curMaterial);
        Log.i(TAG,"扫描结果\r\n站位:"+curLineSeat+"\r\n"+
                "料号:"+curMaterial);
        edt_LineSeat.setText("");
        edt_Material.setText("");
        edt_Operation.requestFocus();

        //保存至数据库日志
        MaterialItem materialItem;
        if (curCheckMaterialId >-1) {
            materialItem = lCheckMaterialItems.get(curCheckMaterialId);
        }
        else{
            materialItem = new MaterialItem("",
                    String.valueOf(""),
                    String.valueOf(edt_LineSeat.getText()),
                    String.valueOf(String.valueOf(edt_Material.getText())),
                    Result,
                    strRemark);
        }
        new GlobalFunc().AddDBLog(globalData, materialItem);
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 重新检下一个料
     */
    private void checkAgain() {
        Log.i(TAG, "testAgain");
        initData();
        tv_Remark.setText("");
        tv_Result.setText("");
        tv_Result.setBackgroundColor(Color.TRANSPARENT);
        curLineSeat="";
        curMaterial="";
        //edt_LineSeat.setText("");
        edt_Material.setText("");
        //edt_LineSeat.requestFocus();
    }
}
