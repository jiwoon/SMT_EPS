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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;

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
    private TextView edt_Operation;
    private EditText edt_LineSeat;
    private EditText edt_Material;

    //上料列表
    private ListView lv_FeedMaterial;
    private MaterialAdapter materialAdapter;

    //当前上料时用到的排位料号表
    private List<MaterialItem> lFeedMaterialItem = new ArrayList<MaterialItem>();

    //当前上料项
    private int curFeedMaterialId = 0;
    //成功上料数
    private int sucFeedCount = 0;
    //料号表的总数
    private int allCount = 0;
    //匹配的站位表项
    private int matchFeedMaterialId = -1;
    //上料结果
    private boolean feedResult = true;

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
        //这几个命令要设置
//        vFeedMaterialFragment.setFocusable(true);
//        vFeedMaterialFragment.setFocusableInTouchMode(true);
//        vFeedMaterialFragment.setOnKeyListener(menuListener);
        //获取工单号和操作员
        Intent intent=getActivity().getIntent();
        savedInstanceState=intent.getExtras();
        Log.i(TAG,"curOderNum::"+savedInstanceState.getString("orderNum")+" -- curOperatorNUm::"+savedInstanceState.getString("operatorNum"));

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperator(savedInstanceState.getString("operatorNum"));
        globalData.setOperType(Constants.FEEDMATERIAL);
        initViews(savedInstanceState);
        initEvents();
        initData();//初始化数据

        edt_LineSeat.requestFocus();
        return vFeedMaterialFragment;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_feed_order= (TextView) vFeedMaterialFragment.findViewById(R.id.tv_feed_order);
        edt_Operation = (TextView) vFeedMaterialFragment.findViewById(R.id.tv_feed_Operation);
        edt_LineSeat = (EditText) vFeedMaterialFragment.findViewById(R.id.edt_lineseat);
        edt_Material = (EditText) vFeedMaterialFragment.findViewById(R.id.edt_material);

        lv_FeedMaterial = (ListView) vFeedMaterialFragment.findViewById(R.id.list_view);
        tv_feed_order.setText(bundle.getString("orderNum"));
        edt_Operation.setText(bundle.getString("operatorNum"));
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
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG, "initData");
        curFeedMaterialId=0;
        sucFeedCount = 0;
        //填充数据
        lFeedMaterialItem.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();
        for (MaterialItem materialItem : materialItems) {
            MaterialItem feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
            lFeedMaterialItem.add(feedMaterialItem);
        }
        /*
        //测试用
        for (int i=0;i < 10;i++){
            MaterialItem feedMaterialItem = new MaterialItem(materialItems.get(i).getFileId(),
            materialItems.get(i).getOrgLineSeat(), materialItems.get(i).getOrgMaterial(), "", "", "", "");
            lFeedMaterialItem.add(feedMaterialItem);
        }
        */
        allCount = lFeedMaterialItem.size();
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
        //回车键
        if (i == EditorInfo.IME_ACTION_SEND ||
                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
            Log.i(TAG, "keyEvent:" + keyEvent.getAction());
            switch (keyEvent.getAction()) {
                //抬上
                case KeyEvent.ACTION_UP:
                    //扫描内容
                    String scanValue = String.valueOf(((EditText) textView).getText());
                    scanValue = scanValue.replaceAll("\r", "");
                    Log.i(TAG, "scan Value:" + scanValue);
                    textView.setText(scanValue);

                    //将扫描的内容更新至列表中
                    //MaterialItem feedMaterialItem = lFeedMaterialItem.get(curFeedMaterialId);
                    switch (textView.getId()) {
                        case R.id.edt_lineseat:
                            //站位
                            Log.i(TAG, "lineseat:" + scanValue);
                            //转化成站位表站位格式
                            String scanLineSeat=scanValue;
                            if (scanValue.length()>=8){
                                scanLineSeat=scanValue.substring(4,6)+"-"+scanValue.substring(6,8);
                            }
                            scanValue=scanLineSeat;

                            for (int j = 0; j < lFeedMaterialItem.size(); j++) {
                                MaterialItem materialItem = lFeedMaterialItem.get(j);
                                //扫到的站位在表中存在
                                if (materialItem.getOrgLineSeat().equalsIgnoreCase(scanValue)) {
                                    matchFeedMaterialId = j;
                                    materialItem.setScanLineSeat(scanValue);
//                                    break;
                                }
                            }
                            //无匹配的站位
                            if (matchFeedMaterialId < 0){
                                feedNextMaterial();
                                return true;
                            } else{
                                //更新显示
                                materialAdapter.notifyDataSetChanged();
                                edt_Material.requestFocus();
                            }
                            break;
                        case R.id.edt_material:
                            //料号,若为二维码则提取@@前的料号
                            //checkMaterial
                            //提取有效料号
                            if (scanValue.indexOf("@") != -1) {
                                scanValue = scanValue.substring(0, scanValue.indexOf("@"));
//                                textView.setText(scanValue);
                            }
                            textView.setText(scanValue);
                            //站位不存在
                            if (matchFeedMaterialId < 0){
                                feedNextMaterial();
                                return true;
                            }
                            //先获取扫描到的站位
                            int lineSeatIndex=matchFeedMaterialId;
                            String scanSeatNo = lFeedMaterialItem.get(matchFeedMaterialId).getScanLineSeat();
                            Log.d(TAG,"scanSeatNo-"+scanSeatNo);
                            //比对料号，包含替代料
                            matchFeedMaterialId = -1;
                            //站位在料号表中的索引
                            ArrayList<Integer> lineSeatIndexs=new ArrayList<Integer>();
                            for (int x = 0;x < lFeedMaterialItem.size();x++){
                                if (lFeedMaterialItem.get(x).getOrgLineSeat().equalsIgnoreCase(scanSeatNo)){
                                    lineSeatIndexs.add(x);
                                }
                            }
                            if (lineSeatIndexs.size() == 1){
                                MaterialItem singleMaterialItem=lFeedMaterialItem.get(lineSeatIndexs.get(0));
                                singleMaterialItem.setScanMaterial(scanValue);
                                if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(scanValue)){
                                    singleMaterialItem.setResultRemark("PASS","上料成功");
                                    //成功次数加1
                                    sucFeedCount++;
                                }else {
                                    singleMaterialItem.setResultRemark("FAIL","料号与站位不相符");
                                }
                                matchFeedMaterialId = lineSeatIndexs.get(0);
                            }else {
                                checkMultiItem(lineSeatIndexs,scanValue);
                            }
                            /*
                            ArrayList<Integer> lineSeats=new ArrayList<Integer>();
                            //遍历料号表
                            for (int j = 0; j < lFeedMaterialItem.size(); j++) {
                                MaterialItem feedMaterialItem = lFeedMaterialItem.get(j);
                                //料号存在
                                if (feedMaterialItem.getOrgMaterial().equalsIgnoreCase(scanValue)) {
                                    //保存站位
                                    lineSeats.add(j);
                                    if (!feedMaterialItem.getOrgLineSeat().equalsIgnoreCase(scanSeatNo)) {
                                        //料号与站位不相符,(失败也要设置站位)
                                        *//*
                                        feedMaterialItem.setScanLineSeat(scanSeatNo);
                                        feedMaterialItem.setResultRemark("FAIL", "料号与站位不相符");
                                        feedMaterialItem.setScanMaterial(scanValue);
                                        *//*
                                        matchFeedMaterialId = lineSeatIndex;
                                        MaterialItem failItem=lFeedMaterialItem.get(lineSeatIndex);
                                        failItem.setScanLineSeat(scanSeatNo);
                                        failItem.setScanMaterial(scanValue);
                                        failItem.setResultRemark("FAIL", "料号与站位不相符");

                                    }
                                    Log.d(TAG,"feedMaterialItem.getScanLineSeat-"+feedMaterialItem.getScanLineSeat());
                                }
                            }
                            //遍历所有相同料号的站位,判断与扫描到的站位是否一样
                            for (int k=0;k < lineSeats.size();k++){
                                if (lFeedMaterialItem.get(lineSeats.get(k)).getOrgLineSeat().equals(scanSeatNo)){
                                    //相同
                                    matchFeedMaterialId = lineSeats.get(k);
                                    lFeedMaterialItem.get(lineSeats.get(k)).setResultRemark("PASS", "上料成功");
                                    lFeedMaterialItem.get(lineSeats.get(k)).setScanMaterial(scanValue);
                                }
                            }

                            */

                            if (matchFeedMaterialId < 0){
                                feedNextMaterial();
                                return true;
                            }
                            //更新数据显示
                            materialAdapter.notifyDataSetChanged();
                            //增加数据库日志
                            globalData.setOperType(Constants.FEEDMATERIAL);
                            new GlobalFunc().AddDBLog(globalData, lFeedMaterialItem.get(matchFeedMaterialId));
                            //清空站位数组
                            lineSeatIndexs.clear();
                            //上下一个料
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

    private void checkMultiItem(ArrayList<Integer> integers,String mScanValue){
        //多个相同的站位,即有替换料
        for (int z = 0;z < integers.size();z++){
            MaterialItem multiMaterialItem = lFeedMaterialItem.get(integers.get(z));
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())){
                for (int x = 0;x < integers.size();x++){
                    MaterialItem innerMaterialItem = lFeedMaterialItem.get(integers.get(x));
                    innerMaterialItem.setScanMaterial(mScanValue);
                    if (x == z){
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                        matchFeedMaterialId = integers.get(x);
                    }else {
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }
                    //成功次数加1
                    sucFeedCount++;
                }
                //跳出循环
                return;
            }else {
                multiMaterialItem.setResult("FAIL");
                multiMaterialItem.setRemark("料号与站位不相符");
                //都不相符,默认操作的是第一个
                matchFeedMaterialId = integers.get(0);
            }
        }
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 上下一个料
     */
    private void feedNextMaterial() {
        Log.i(TAG, "feedNextMaterial:" + matchFeedMaterialId);
        //显示最新的上料结果
        if (matchFeedMaterialId >= 0) {
            lv_FeedMaterial.setSelection(matchFeedMaterialId);
            matchFeedMaterialId = -1;
        } else{
            //排位表不存在此站位或料号
            curFeedMaterialId--;
            //弹出提示框
            String titleMsg[]=new String[]{"提示","排位表不存在此站位或料号!!!"};
            int msgStyle[]=new int[]{22,Color.RED};
            showInfo(titleMsg,msgStyle,false,0);
        }

        /*
        //循环上料
        if (curFeedMaterialId < lFeedMaterialItem.size() - 1) {
            curFeedMaterialId++;
            clearLineSeatMaterialScan();
        }else {
            //上料结束,显示结果
            showFeedMaterialResult();
        }
        */
        Log.d(TAG,"sucFeedCount-"+sucFeedCount
                +"\nlFeedMaterialItem-"+lFeedMaterialItem.size()+"\nallCount-"+allCount);
        if (sucFeedCount < lFeedMaterialItem.size() || sucFeedCount < allCount){
            clearLineSeatMaterialScan();
        }else {
            //上料结束,显示结果
            showFeedMaterialResult();
        }
    }

    //显示上料结果
    private void showFeedMaterialResult() {
        //默认上料结果是PASS
        feedResult = true;
        for (MaterialItem feedMaterialItem : lFeedMaterialItem) {
            if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                feedResult = false;
                break;
            }
        }
        //弹出上料结果
        String titleMsg[];
        int msgStyle[];
        if (feedResult){
            titleMsg=new String[]{"上料结果","PASS"};
            msgStyle=new int[]{66,Color.argb(255,102,153,0)};
        }else {
            titleMsg=new String[]{"上料失败，请检查!","FAIL"};
            msgStyle=new int[]{66,Color.RED};
        }
        showInfo(titleMsg,msgStyle,feedResult,1);
    }

    //弹出消息窗口
    private boolean showInfo(String[] titleMsg, int[] msgStyle, final boolean result, final int resultType){
        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};

        InfoDialog infoDialog=new InfoDialog(getActivity(),
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStyle);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        if (result){
                            dialog.dismiss();
                            clearLineSeatMaterialScan();
                            initData();
                        }else{
                            dialog.dismiss();
                            clearLineSeatMaterialScan();
                            if (resultType == 1){
                                //将未成功数加到
                                for (MaterialItem feedMaterialItem : lFeedMaterialItem) {
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
        infoDialog.show();

        return true;
    }

    //清除扫描数据
    private void clearLineSeatMaterialScan() {
        edt_LineSeat.setText("");
        edt_Material.setText("");
        edt_LineSeat.requestFocus();
    }

    //监听菜单按钮事件
    private View.OnKeyListener menuListener =new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP){
                if (keyCode == KeyEvent.KEYCODE_MENU){//菜单按键
                    Log.d(TAG,"按下按钮了！！！哈哈哈哈哈哈哈哈......");
                    showFeedMaterialResult();
                }
            }
            return true;
        }
    };


}
