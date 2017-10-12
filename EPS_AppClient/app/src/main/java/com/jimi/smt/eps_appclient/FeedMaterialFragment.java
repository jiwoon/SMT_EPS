package com.jimi.smt.eps_appclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.ArrayList;
import java.util.List;
/**
 * 类名:FeedMaterialFragment
 * 创建人:Connie
 * 创建时间:2017-9-21
 * 描述:上料Fragment
 * 版本号:V1.0
 * 修改记录:
 */
public class FeedMaterialFragment extends Fragment implements OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    GlobalData globalData;

    //上料视图
    private View vFeedMaterialFragment;
    //操作员　站位　料号
    private EditText edt_Operation, edt_LineSeat, edt_Material;

    //上料列表
    private ListView lv_FeedMaterial;
    private MaterialAdapter materialAdapter;

    //当前上料时用到的排位料号表
    private List<MaterialItem> lFeedMaterialItem = new ArrayList<MaterialItem>();

    //当前上料项
    int curFeedMaterialId = 0;
    int matchFeedMaterialId = -1;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @author connie
     * @time 2017-9-22
     * @describe 创建视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vFeedMaterialFragment = inflater.inflate(R.layout.feedmaterial_layout, container, false);

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperType(Constants.FEEDMATERIAL);
        initViews();
        initEvents();
        initData();//初始化数据

        edt_LineSeat.requestFocus();
        return vFeedMaterialFragment;


    }

    @Override
    public void onResume() {
        super.onResume();
//        edt_Operation.requestFocus();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews() {
        Log.i(TAG, "initViews");
        edt_Operation = (EditText) vFeedMaterialFragment.findViewById(R.id.edt_Operation);
        edt_LineSeat = (EditText) vFeedMaterialFragment.findViewById(R.id.edt_lineseat);
        edt_Material = (EditText) vFeedMaterialFragment.findViewById(R.id.edt_material);

        lv_FeedMaterial = (ListView) vFeedMaterialFragment.findViewById(R.id.list_view);


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
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG, "initData");
        curFeedMaterialId=0;
        //填充数据
        lFeedMaterialItem.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();
        for (MaterialItem materialItem : materialItems) {
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
            lFeedMaterialItem.add(feedMaterialItem);
        }
        //设置Adapter
        materialAdapter = new MaterialAdapter(this.getActivity(), lFeedMaterialItem);
        lv_FeedMaterial.setAdapter(materialAdapter);

        matchFeedMaterialId=-1;
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
        Log.i(TAG,"keyEvent:"+keyEvent.getAction());
        //回车键
//        if (i == EditorInfo.IME_ACTION_UNSPECIFIED) {
//            // do something
////        }
////
        if (i == EditorInfo.IME_ACTION_SEND ||
                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
            switch (keyEvent.getAction()) {
                //按下
                case KeyEvent.ACTION_UP:
                    //扫描内容
                    String scanValue = String.valueOf(((EditText) textView).getText());
                    scanValue = scanValue.replaceAll("\r", "");
                    Log.i(TAG, "scan Value:" + scanValue);
                    textView.setText(scanValue);

                    //将扫描的内容更新至列表中
                    //MaterialItem feedMaterialItem = lFeedMaterialItem.get(curFeedMaterialId);
                    switch (textView.getId()) {
                        case R.id.edt_Operation:
                            //操作员格式不对
                            if (scanValue.length() != 10) {
                            }
                            globalData.setOperator(scanValue);
                            edt_LineSeat.requestFocus();
                            break;
                        case R.id.edt_lineseat:
                            //站位
                            Log.i(TAG, "lineseat:" + scanValue);
                            String scanLineSeat=scanValue;
                            if (scanValue.length()>=8){
                                scanLineSeat=scanValue.substring(4,6)+"-"+scanValue.substring(6,8);
                            }
                            scanValue=scanLineSeat;


                            for (int j = 0; j < lFeedMaterialItem.size(); j++) {
                                MaterialItem materialItem = lFeedMaterialItem.get(j);
                                if (materialItem.getOrgLineSeat().equalsIgnoreCase(scanValue)) {
                                    matchFeedMaterialId = j;

                                }
                            }
                            if (matchFeedMaterialId<0){
                                feedNextMaterial();
                                return true;
                            }
                            else{
                                MaterialItem feedMaterialItem = lFeedMaterialItem.get(matchFeedMaterialId);
                                feedMaterialItem.setScanLineSeat(scanLineSeat);
                                materialAdapter.notifyDataSetChanged();
                                edt_Material.requestFocus();
                            }



//                            if (edt_LineSeat.length() != 7) {
//                                feedMaterialItem.setRemark("站位长度不正确");
//                            } else {
//                                feedMaterialItem.setRemark("");
//                            }


                            break;
                        case R.id.edt_material:
                            //料号,若为二维码则提取@@前的料号
                            if (scanValue.indexOf("@") != -1) {
                                scanValue = scanValue.substring(0, scanValue.indexOf("@"));
                                textView.setText(scanValue);
                            }
                            if (matchFeedMaterialId<0){
                                feedNextMaterial();
                                return true;
                            }
                            MaterialItem feedMaterialItem = lFeedMaterialItem.get(matchFeedMaterialId);
                            feedMaterialItem.setScanMaterial(scanValue);
                            //比对站位和料号是否相等
                            if ((feedMaterialItem.getOrgLineSeat().equalsIgnoreCase(feedMaterialItem.getScanLineSeat())) &&
                                    feedMaterialItem.getOrgMaterial().equalsIgnoreCase(feedMaterialItem.getScanMaterial())) {
                                feedMaterialItem.setResult("PASS");
                                feedMaterialItem.setRemark("");
                            } else {
                                feedMaterialItem.setResult("FAIL");
                                if (!feedMaterialItem.getOrgLineSeat().equalsIgnoreCase(feedMaterialItem.getScanLineSeat())) {
                                    feedMaterialItem.setRemark("站位不相符");
                                } else if (!feedMaterialItem.getOrgMaterial().equalsIgnoreCase(feedMaterialItem.getScanMaterial())) {
                                    feedMaterialItem.setRemark("料号与排位表不相符");
                                }
                            }
                            materialAdapter.notifyDataSetChanged();

                            //增加数据库日志
                            new GlobalFunc().AddDBLog(globalData, lFeedMaterialItem.get(matchFeedMaterialId));

                            feedNextMaterial();
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
     * @author connie
     * @time 2017-9-22
     * @describe 上下一个料
     */
    private void feedNextMaterial() {
        Log.i(TAG, "feedNextMaterial:" + matchFeedMaterialId);
        //显示最新的上料结果
        if (matchFeedMaterialId>=0) {
            lv_FeedMaterial.setSelection(matchFeedMaterialId);
            matchFeedMaterialId = -1;
        }
        else{
            curFeedMaterialId--;
            //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //设置Title的图标
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            //设置Title的内容
            builder.setTitle("提示");
            builder.setMessage("排位表不存在此站位!!!");

            //设置一个PositiveButton
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //getMaterial();
                }
            });
            //显示出该对话框
            builder.show();
        }
        //循环上料
        if (curFeedMaterialId < lFeedMaterialItem.size() - 1) {
//        if (curFeedMaterialId < 2) {
            curFeedMaterialId++;
            clearLineSeatMaterialScan();
            edt_LineSeat.requestFocus();
        }
        //上料结束,显示结果
        else {
            showFeedMaterialResult();
        }
    }

    private void showFeedMaterialResult() {
        //默认上料结果是PASS
        boolean feedResult = true;
        for (MaterialItem feedMaterialItem : lFeedMaterialItem) {
            if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                feedResult = false;
            }
        }


        //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //设置Title的图标
        builder.setIcon(android.R.drawable.ic_dialog_info);
        //设置Title的内容
        builder.setTitle("上料结果");
        View view = View.inflate(getActivity(), R.layout.materialresult_dialog, null);
        builder.setView(view);
        final EditText etResult = (EditText) view.findViewById(R.id.et_result);

        //设置Content来显示一个信息
        if (feedResult) {
            etResult.setText("PASS");
            etResult.setBackgroundColor(Color.GREEN);

        } else {
            etResult.setText("FAIL");
            etResult.setBackgroundColor(Color.RED);
        }

        //设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                clearLineSeatMaterialScan();
                initData();
                edt_LineSeat.requestFocus();
                edt_LineSeat.requestFocus();
            }
        });
        //显示出该对话框
        builder.show();
    }

    private void clearLineSeatMaterialScan() {
        edt_LineSeat.setText("");
        edt_Material.setText("");
    }
}
