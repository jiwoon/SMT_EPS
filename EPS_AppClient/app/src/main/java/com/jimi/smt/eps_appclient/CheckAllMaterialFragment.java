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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Adapter.MaterialAdapter;
import com.jimi.smt.eps_appclient.Func.GlobalFunc;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caobotao on 16/1/4.
 */
public class CheckAllMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    //全局变量
    GlobalData globalData;

    //上料视图
    private View vCheckAllMaterialFragment;
    //操作员　站位　料号
    private EditText edt_Operation, /*edt_LineSeat,*/
            edt_ScanMaterial;

    //上料列表
    private ListView lv_CheckAllMaterial;
    private MaterialAdapter materialAdapter;

    //当前检料时用到的排位料号表
    private List<MaterialItem> lCheckAllMaterialItem = new ArrayList<MaterialItem>();

    //当前检料项
    int curCheckId = 0;
    android.app.AlertDialog dialog = null;
    //长按时选择的行
    int selectRow=-1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        vCheckAllMaterialFragment = inflater.inflate(R.layout.checkallmaterial_layout, container, false);

        globalData = (GlobalData) getActivity().getApplication();
        globalData.setOperType(Constants.CHECKALLMATERIAL);

        initViews();
        initEvents();
        initData();//初始化数据
        return vCheckAllMaterialFragment;
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化控件
     */
    private void initViews() {
        Log.i(TAG, "initViews");
        edt_Operation = (EditText) vCheckAllMaterialFragment.findViewById(R.id.edt_Operation);
        //edt_LineSeat = (EditText) vCheckAllMaterialFragment.findViewById(R.id.edt_Lineseat);
        edt_ScanMaterial = (EditText) vCheckAllMaterialFragment.findViewById(R.id.edt_material);

        lv_CheckAllMaterial = (ListView) vCheckAllMaterialFragment.findViewById(R.id.list_view);

        edt_Operation.requestFocus();
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG, "initEvents");
        edt_Operation.setOnEditorActionListener(this);
        //edt_LineSeat.setOnEditorActionListener(this);
        edt_ScanMaterial.setOnEditorActionListener(this);

        lv_CheckAllMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int row, long l) {
                //弹出对话框
                selectRow=row;
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                View view1 = View.inflate(getActivity(), R.layout.inputmaterial_dialog, null);
                final EditText etMaterial = (EditText) view1.findViewById(R.id.et_Material);
                builder.setView(view1);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                etMaterial.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(final TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_SEND ||
                                (keyEvent != null && keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER)) {
                            switch (keyEvent.getAction()) {
                                //按下
                                case KeyEvent.ACTION_DOWN:
                                    //扫描内容
                                    String scanValue = String.valueOf(((EditText) textView).getText());
                                    if (scanValue.indexOf("@") != -1) {
                                        scanValue = scanValue.substring(0, scanValue.indexOf("@"));
                                        MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(selectRow);
                                        checkAllMaterialItem.setScanMaterial(scanValue);

                                        //比对料号是否相等
                                        if (checkAllMaterialItem.getOrgMaterial().equalsIgnoreCase(checkAllMaterialItem.getScanMaterial())) {
                                            checkAllMaterialItem.setResultRemark("PASS", "");
                                        } else {
                                            if (!checkAllMaterialItem.getOrgMaterial().equalsIgnoreCase(checkAllMaterialItem.getScanMaterial())) {
                                                checkAllMaterialItem.setResultRemark("FAIL","料号与排位表不相符");
                                            }
                                        }
                                        materialAdapter.notifyDataSetChanged();

                                        //增加数据库日志
                                        new GlobalFunc().AddDBLog(globalData, lCheckAllMaterialItem.get(curCheckId));
                                        dialog.dismiss();
                                        edt_Operation.requestFocus();
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                        return false;
                    }

                });
                dialog = builder.create();
                dialog.show();
                return true;
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
                curCheckId = 0;
                lCheckAllMaterialItem.clear();
                List<MaterialItem> materialItems = globalData.getMaterialItems();
                for (MaterialItem materialItem : materialItems) {
                    MaterialItem feedMaterialItem;
                    feedMaterialItem = new MaterialItem(materialItem.getFileId(),materialItem.getOrgLineSeat(), materialItem.getOrgMaterial(), "", "", "", "");
                    lCheckAllMaterialItem.add(feedMaterialItem);
                }
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
                //回车键
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
                            MaterialItem checkAllMaterialItem = lCheckAllMaterialItem.get(curCheckId);
                            switch (textView.getId()) {
                                case R.id.edt_Operation:
                                    //操作员格式不对
                                    if (scanValue.length() != 10) {
                                    }
                                    globalData.setOperator(scanValue);
                                    edt_ScanMaterial.requestFocus();
                                    break;
//                        case R.id.edt_Lineseat:
//                            //站位
//                            //String scanLineSeat=scanValue.substring(4,6)+"-"+scanValue.substring(6,8);
//                            checkAllMaterialItem.setScanLineSeat(scanValue);
////                            if (edt_LineSeat.length() != 7) {
////                                feedMaterialItem.setRemark("站位长度不正确");
////                            } else {
////                                feedMaterialItem.setRemark("");
////                            }
//                            materialAdapter.notifyDataSetChanged();
//                            break;
                                case R.id.edt_material:
                                    //料号,若为二维码则提取@@前的料号
                                    if (scanValue.indexOf("@") != -1) {
                                        scanValue = scanValue.substring(0, scanValue.indexOf("@"));
                                        textView.setText(scanValue);
                                    }

                                    checkAllMaterialItem.setScanMaterial(scanValue);
                                    //比对站位和料号是否相等
                                    if (checkAllMaterialItem.getOrgMaterial().equalsIgnoreCase(checkAllMaterialItem.getScanMaterial())) {
                                        checkAllMaterialItem.setResult("PASS");
                                    } else {
                                        checkAllMaterialItem.setResult("FAIL");
                                        if (!checkAllMaterialItem.getOrgMaterial().equalsIgnoreCase(checkAllMaterialItem.getScanMaterial())) {
                                            checkAllMaterialItem.setRemark("料号与排位表不相符");
                                        }
                                    }
                                    materialAdapter.notifyDataSetChanged();

                                    //增加数据库日志
                                    new GlobalFunc().AddDBLog(globalData, lCheckAllMaterialItem.get(curCheckId));

                                    checkNextMaterial();
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
