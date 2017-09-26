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

import com.jimi.smt.eps_appclient.Adapter.FeedMaterialAdapter;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.FeedMaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;


import java.sql.Timestamp;
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

    private static final int DATA_CAPACITY = 2;
    //上料视图
    private View vFeedMaterialFragment;
    //操作员　站位　料号
    private EditText edt_Operation, edt_LineSeat, edt_Material;

    //上料列表
    private ListView lv_FeedMaterial;
    private List<FeedMaterialItem> lFeedMaterialItem = new ArrayList<FeedMaterialItem>();
    private FeedMaterialAdapter feedMaterialAdapter;

    //当前测试项
    int curTestId = 0;

    GlobalData globalData;

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
        initViews();
        initEvents();
        initData();//初始化数据
        return vFeedMaterialFragment;
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
        //填充数据
        lFeedMaterialItem.clear();
        for (int i = 0; i < DATA_CAPACITY; i++) {
            FeedMaterialItem feedMaterialItem;
//            if (i < 10) {
//                feedMaterialItem = new FeedMaterialItem("306010" + i, "6940105300121", "", "", "", "");
//            } else {
//                feedMaterialItem = new FeedMaterialItem("30601" + i, "6940105300121", "", "", "", "");
//            }
            feedMaterialItem = new FeedMaterialItem("6940105300121", "6940105300121", "", "", "", "");
            lFeedMaterialItem.add(feedMaterialItem);
        }
        //设置Adapter
        feedMaterialAdapter = new FeedMaterialAdapter(this.getActivity(), lFeedMaterialItem);
        lv_FeedMaterial.setAdapter(feedMaterialAdapter);
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
                    FeedMaterialItem feedMaterialItem = lFeedMaterialItem.get(curTestId);
                    switch (textView.getId()) {
                        case R.id.edt_Operation:
                            //操作员格式不对
                            if (scanValue.length() != 10) {
                            }
                            globalData.setOperator(scanValue);
                            break;
                        case R.id.edt_lineseat:
                            //站位
                            feedMaterialItem.setScanLineSeat(scanValue);
//                            if (edt_LineSeat.length() != 7) {
//                                feedMaterialItem.setRemark("站位长度不正确");
//                            } else {
//                                feedMaterialItem.setRemark("");
//                            }
                            feedMaterialAdapter.notifyDataSetChanged();
                            break;
                        case R.id.edt_material:
                            //料号
                            scanValue=scanValue.substring(0,scanValue.indexOf("@"));
                            feedMaterialItem.setScanMaterial(scanValue);
                            textView.setText(scanValue);

                            //比对站位和料号是否相等
                            if ((feedMaterialItem.getOrgLineSeat().equalsIgnoreCase(feedMaterialItem.getScanLineSeat())) &&
                                    feedMaterialItem.getOrgMaterial().equalsIgnoreCase(feedMaterialItem.getScanMaterial())) {
                                feedMaterialItem.setResult("PASS");
                            } else {
                                feedMaterialItem.setResult("FAIL");
                                if (!feedMaterialItem.getOrgLineSeat().equalsIgnoreCase(feedMaterialItem.getScanLineSeat())) {
                                    feedMaterialItem.setRemark("站位不相符");
                                } else if (!feedMaterialItem.getOrgMaterial().equalsIgnoreCase(feedMaterialItem.getScanMaterial())) {
                                    feedMaterialItem.setRemark("料号与排位表不相符");
                                }
                            }

                            feedMaterialAdapter.notifyDataSetChanged();
                            AddLog();

                            testNext();
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
     * @author connie
     * @time 2017-9-22
     * @describe 测试下一项
     */
    private void testNext() {
        Log.i(TAG, "testNext:" + curTestId);
        lv_FeedMaterial.setSelection(curTestId);
        if (curTestId < lFeedMaterialItem.size() - 1) {
            curTestId++;
            clearLineSeatMaterialScan();
            edt_Operation.requestFocus();
        } else {
            boolean feedResult=true;
            for (FeedMaterialItem feedMaterialItem:lFeedMaterialItem){
                if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")){
                    feedResult=false;
                }
            }
//    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //    设置Title的图标
            builder.setIcon(android.R.drawable.ic_dialog_info);
            View view = View.inflate(getActivity(), R.layout.materialresult_dialog, null);
            builder.setView(view);
            final EditText etResult = (EditText) view.findViewById(R.id.et_result);


            //    设置Title的内容
            builder.setTitle("上料结果");
            //    设置Content来显示一个信息
            if(feedResult){
                etResult.setText("PASS");
                etResult.setBackgroundColor(Color.GREEN);

            }
            else{
                etResult.setText("FAIL");
                etResult.setBackgroundColor(Color.RED);
            }

            //    设置一个PositiveButton
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    resetTestPar();
                }
            });
            //    设置一个NegativeButton
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    resetTestPar();
                }
            });
            //    显示出该对话框
            builder.show();
        }
    }

    private void clearLineSeatMaterialScan() {
        edt_LineSeat.setText("");
        edt_Material.setText("");
    }

    private void resetTestPar() {
        clearLineSeatMaterialScan();
        edt_LineSeat.requestFocus();
        curTestId = 0;
        initData();
    }

    //    private void AddAllLog() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<OperLogItem> operLogItems = new ArrayList<OperLogItem>();
//                for (FeedMaterialItem feedMaterialItem:mList)
//                {
//                    OperLogItem operLogItem = new OperLogItem();
//                    operLogItem.setOperator("124325");
//                    operLogItem.setTime(new Timestamp(System.currentTimeMillis()));
//                    operLogItem.setType(0);
//                    operLogItem.setResult("PASS");
//                    operLogItem.setLineseat(feedMaterialItem.getScanLineSeat());
//                    operLogItem.setMaterial_no(feedMaterialItem.getScanMaterial());
//                    operLogItem.setOld_material_no(feedMaterialItem.getOrgMaterial());
//                    operLogItems.add(operLogItem);
//                }
//                new DBService().inserOpertLog(operLogItems);
//            }
//        }).start();
//    }
    private void AddLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<OperLogItem> operLogItems = new ArrayList<OperLogItem>();
                FeedMaterialItem feedMaterialItem = lFeedMaterialItem.get(curTestId);
//                for (FeedMaterialItem feedMaterialItem:mList)
                {
                    OperLogItem operLogItem = new OperLogItem();
                    operLogItem.setOperator(globalData.getOperator());
                    operLogItem.setTime(new Timestamp(System.currentTimeMillis()));
                    operLogItem.setType(0);
                    operLogItem.setResult(feedMaterialItem.getResult());
                    operLogItem.setLineseat(feedMaterialItem.getScanLineSeat());
                    operLogItem.setMaterial_no(feedMaterialItem.getScanMaterial());
                    operLogItem.setOld_material_no(feedMaterialItem.getOrgMaterial());
                    operLogItem.setScanLineseat(feedMaterialItem.getScanLineSeat());
                    operLogItem.setRemark(feedMaterialItem.getRemark());
                    operLogItems.add(operLogItem);
                }
                new DBService().inserOpertLog(operLogItems);
            }
        }).start();
    }
}
