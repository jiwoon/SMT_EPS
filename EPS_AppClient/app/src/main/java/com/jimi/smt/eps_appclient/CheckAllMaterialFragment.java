package com.jimi.smt.eps_appclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Views.InputDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caobotao on 16/1/4.
 */
public class CheckAllMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    //全局变量
    private GlobalData globalData;

    //上料视图
    private View vCheckAllMaterialFragment;
    //操作员　站位　料号
    private TextView edt_Operation; /*edt_LineSeat,*/
    private EditText edt_ScanMaterial;

    //上料列表
    private ListView lv_CheckAllMaterial;
    private MaterialAdapter materialAdapter;

    //当前检料时用到的排位料号表
    private List<MaterialItem> lCheckAllMaterialItem = new ArrayList<MaterialItem>();

    //当前检料项
    private int curCheckId = 0;
    private android.app.AlertDialog dialog = null;
    //长按时选择的行
    private int selectRow=-1;
    private GlobalFunc globalFunc;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vCheckAllMaterialFragment = inflater.inflate(R.layout.checkallmaterial_layout, container, false);

        context=EPS_MainActivity.context;
        //获取工单号和操作员
        Intent intent=getActivity().getIntent();
        savedInstanceState=intent.getExtras();
        Log.i(TAG,"curOderNum::"+savedInstanceState.getString("orderNum")+" -- curOperatorNUm::"+savedInstanceState.getString("operatorNum"));

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperType(Constants.CHECKALLMATERIAL);
        globalData.setOperator(savedInstanceState.getString("operatorNum"));
        globalFunc = new GlobalFunc(getActivity());

        initViews(savedInstanceState);
        initEvents();
        initData();//初始化数据
        return vCheckAllMaterialFragment;
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews(Bundle bundle) {
        Log.i(TAG, "initViews");
        TextView tv_checkAll_order= (TextView) vCheckAllMaterialFragment.findViewById(R.id.tv_checkAll_order);
        edt_Operation = (TextView) vCheckAllMaterialFragment.findViewById(R.id.tv_checkAll_Operation);
        edt_ScanMaterial = (EditText) vCheckAllMaterialFragment.findViewById(R.id.edt_material);
        lv_CheckAllMaterial = (ListView) vCheckAllMaterialFragment.findViewById(R.id.list_view);
        tv_checkAll_order.setText(bundle.getString("orderNum"));
        edt_Operation.setText(bundle.getString("operatorNum"));
        edt_ScanMaterial.requestFocus();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG, "initEvents");
        edt_ScanMaterial.setOnEditorActionListener(this);

        lv_CheckAllMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int row, long l) {
                //弹出对话框
                selectRow=row;
                showLongClickDialog("请重新扫描新的料号");
                return true;
            }
            });
    }

    //弹出长按对话框
    private void showLongClickDialog(String title){
        InputDialog inputDialog=new InputDialog(getActivity(),
                R.layout.input_dialog_layout,new int[]{R.id.input_dialog_title,R.id.et_input},title);
        inputDialog.show();
        inputDialog.setOnDialogEditorActionListener(new InputDialog.OnDialogEditorActionListener() {
            @Override
            public boolean OnDialogEditorAction(InputDialog inputDialog,final TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == event.KEYCODE_ENTER)) {

                    switch (event.getAction()) {
                        //按下
                        case KeyEvent.ACTION_UP:
                            //先判断是否联网
                            if (globalFunc.isNetWorkConnected()){
                                switch (v.getId()){
                                    case R.id.et_input:
                                        //扫描内容
                                        String scanValue = String.valueOf(((EditText) v).getText());
                                        if (scanValue.indexOf("@") != -1) {
                                            scanValue = scanValue.substring(0, scanValue.indexOf("@"));
                                        }
                                        MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(selectRow);
                                        checkAllMaterialItem.setScanMaterial(scanValue);
                                        //调用全捡方法
                                        checkAllItems(checkAllMaterialItem,selectRow,scanValue);
                                    /*
                                    //比对料号是否相等
                                    if (checkAllMaterialItem.getOrgMaterial().equalsIgnoreCase(checkAllMaterialItem.getScanMaterial())) {
                                        checkAllMaterialItem.setResultRemark("PASS", "站位和料号正确!");
                                    } else {
                                        if (!checkAllMaterialItem.getOrgMaterial().equalsIgnoreCase(checkAllMaterialItem.getScanMaterial())) {
                                            checkAllMaterialItem.setResultRemark("FAIL","料号与排位表不相符");
                                        }
                                    }
*/
                                        materialAdapter.notifyDataSetChanged();

                                        //增加数据库日志
                                        new GlobalFunc().AddDBLog(globalData, lCheckAllMaterialItem.get(selectRow));
//                                dialog.dismiss();
                                        inputDialog.dismiss();
                                        edt_ScanMaterial.requestFocus();

                                        break;
                                }
                            }else {
                                globalFunc.showInfo("警告","请检查网络连接是否正常!","请连接网络!");
                                v.setText("");
                            }
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
//        inputDialog.show();
    }


    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG, "initData");
        //填充数据
        curCheckId = 0;
        lCheckAllMaterialItem.clear();
        List<MaterialItem> materialItems = globalData.getMaterialItems();
        for (MaterialItem materialItem : materialItems) {
            MaterialItem feedMaterialItem;
            feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
            lCheckAllMaterialItem.add(feedMaterialItem);
        }
        /*
        //测试用
        for (int k=0;k < 10;k++){
            MaterialItem feedMaterialItem;
            feedMaterialItem = new MaterialItem(materialItems.get(k).getFileId(),materialItems.get(k).getOrgLineSeat(),
                    materialItems.get(k).getOrgMaterial(), "", "", "", "");
            lCheckAllMaterialItem.add(feedMaterialItem);
        }*/

        //设置Adapter
        materialAdapter = new MaterialAdapter(getActivity(), lCheckAllMaterialItem);
        lv_CheckAllMaterial.setAdapter(materialAdapter);
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
        Log.i("ACTION_DOWN::onEditorAction-",textView.getText());
        Log.i("ACTION_DOWN::onEditorAction-",i);
        //回车键
        if (i == EditorInfo.IME_ACTION_SEND ||
                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
            switch (keyEvent.getAction()) {
                //按下
                case KeyEvent.ACTION_UP:
                    //先判断是否联网
                    if (globalFunc.isNetWorkConnected()){
                        //扫描内容
                        String scanValue = String.valueOf(((EditText) textView).getText());
                        scanValue = scanValue.replaceAll("\r", "");
                        Log.i(TAG, "scan Value:" + scanValue);
                        textView.setText(scanValue);

                        //将扫描的内容更新至列表中
                        MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(curCheckId);

                        switch (textView.getId()) {
                            case R.id.edt_material:
                                //料号,若为二维码则提取@@前的料号
                                if (scanValue.indexOf("@") != -1) {
                                    scanValue = scanValue.substring(0, scanValue.indexOf("@"));
                                    textView.setText(scanValue);
                                }
                                //当前操作的站位
                                String curCheckLineSeat = checkAllMaterialItem.getOrgLineSeat();
                                //相同站位的索引数组
                                ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();
                                //当前操作的位置
                                int curOperateIndex = curCheckId;
                                curCheckId=curCheckId-1;
                                //向上遍历所有相同站位的位置
                                for (int j = curOperateIndex-1; j >= 0; j--){
                                    if (lCheckAllMaterialItem.get(j).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                        sameLineSeatIndexs.add(j);
                                    }else {
                                        break;
                                    }
                                }
                                //向下遍历所有相同的站位位置
                                for (int k = curOperateIndex; k < lCheckAllMaterialItem.size();k++){
                                    if (lCheckAllMaterialItem.get(k).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                                        sameLineSeatIndexs.add(k);
                                        //将检查的位置往后移
                                        curCheckId ++;
                                    }
                                }
                                //根据站位索引数组检查料号与扫到的料号比对
                                if (sameLineSeatIndexs.size() == 1){
                                    //只有一个站位
                                    MaterialItem singleMaterialItem = lCheckAllMaterialItem.get(sameLineSeatIndexs.get(0));
                                    singleMaterialItem.setScanMaterial(scanValue);
                                    if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())){
                                        singleMaterialItem.setResult("PASS");
                                        singleMaterialItem.setRemark("站位和料号正确");
                                    }else {
                                        singleMaterialItem.setResult("FAIL");
                                        singleMaterialItem.setRemark("料号与站位不对应");
                                    }
                                }else if (sameLineSeatIndexs.size() > 1){
                                    //多个相同的站位,即有替换料
                                    checkMultiItem(sameLineSeatIndexs,scanValue);
                                }

                                materialAdapter.notifyDataSetChanged();

                                //增加数据库日志
                                globalData.setOperType(Constants.CHECKALLMATERIAL);
                                new GlobalFunc().AddDBLog(globalData, lCheckAllMaterialItem.get(curOperateIndex));
                                //清空站位
                                sameLineSeatIndexs.clear();
                                //检查下一个料
                                checkNextMaterial();
                                break;
                        }
                    }else {
                        globalFunc.showInfo("警告","请检查网络连接是否正常!","请连接网络!");
                        clearLineSeatMaterialScan();
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
            MaterialItem multiMaterialItem = lCheckAllMaterialItem.get(integers.get(z));
            multiMaterialItem.setScanMaterial(mScanValue);
            if (multiMaterialItem.getOrgMaterial().equalsIgnoreCase(multiMaterialItem.getScanMaterial())){
                for (int x = 0;x < integers.size();x++){
                    MaterialItem innerMaterialItem = lCheckAllMaterialItem.get(integers.get(x));
                    innerMaterialItem.setScanMaterial(mScanValue);
                    if (x == z){
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }else {
                        innerMaterialItem.setResult("PASS");
                        innerMaterialItem.setRemark("主替有一项成功");
                    }
                }
                //跳出循环
                return;
            }else {
                multiMaterialItem.setResult("FAIL");
                multiMaterialItem.setRemark("料号与站位不对应");
            }
        }
    }

    private void checkAllItems(MaterialItem materialItem,int curCheckIndex,String mScanValue){

        //当前操作的站位
        String curCheckLineSeat = materialItem.getOrgLineSeat();
        //相同站位的索引数组
        ArrayList<Integer> sameLineSeatIndexs = new ArrayList<Integer>();
        //当前操作的位置
        int curOperateIndex = curCheckIndex;
//        curCheckIndex=curCheckIndex-1;
        //向上遍历所有相同站位的位置
        for (int j = curOperateIndex-1; j >= 0; j--){
            if (lCheckAllMaterialItem.get(j).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                sameLineSeatIndexs.add(j);
            }else {
                break;
            }
        }
        //向下遍历所有相同的站位位置
        for (int k = curOperateIndex; k < lCheckAllMaterialItem.size();k++){
            if (lCheckAllMaterialItem.get(k).getOrgLineSeat().equalsIgnoreCase(curCheckLineSeat)){
                sameLineSeatIndexs.add(k);
                //将检查的位置往后移
//                curCheckIndex ++;
            }
        }
        //根据站位索引数组检查料号与扫到的料号比对
        if (sameLineSeatIndexs.size() == 1){
            //只有一个站位
            MaterialItem singleMaterialItem = lCheckAllMaterialItem.get(sameLineSeatIndexs.get(0));
            singleMaterialItem.setScanMaterial(mScanValue);
            if (singleMaterialItem.getOrgMaterial().equalsIgnoreCase(singleMaterialItem.getScanMaterial())){
                singleMaterialItem.setResult("PASS");
                singleMaterialItem.setRemark("站位和料号正确");
            }else {
                singleMaterialItem.setResult("FAIL");
                singleMaterialItem.setRemark("料号与站位不对应");
            }
        }else if (sameLineSeatIndexs.size() > 1){
            //多个相同的站位,即有替换料
            checkMultiItem(sameLineSeatIndexs,mScanValue);
        }

    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 上下一个料
     */
    private void checkNextMaterial() {
        Log.i(TAG, "checkNextMaterial:" + curCheckId);
        lv_CheckAllMaterial.setSelection(curCheckId);
        if (curCheckId < lCheckAllMaterialItem.size() - 1) {
            curCheckId++;
            clearLineSeatMaterialScan();
        } else {
            showCheckAllMaterialResult();
        }
    }

    private void showCheckAllMaterialResult() {
        boolean feedResult = true;
        for (MaterialItem feedMaterialItem : lCheckAllMaterialItem) {
            if (!feedMaterialItem.getResult().equalsIgnoreCase("PASS")) {
                feedResult = false;
            }
        }
        //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        //设置Title的图标
        builder.setIcon(android.R.drawable.ic_dialog_info);
        //设置Title的内容
        builder.setTitle("全检料结果");
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
                clearMaterialInfo();
            }
        });
        //显示出该对话框
        builder.show();
    }

    /**
     * @method resetTestPar
     * @author connie
     * @time 2017-9-26
     * @describe 清空之前的全检信息进入下一轮全检
     */
    private void clearMaterialInfo() {
        clearLineSeatMaterialScan();
//        edt_LineSeat.requestFocus();
        edt_ScanMaterial.requestFocus();
        curCheckId = 0;
        initData();
    }

    private void clearLineSeatMaterialScan() {
//        edt_LineSeat.setText("");
        edt_ScanMaterial.setText("");
        edt_ScanMaterial.requestFocus();
    }

}
