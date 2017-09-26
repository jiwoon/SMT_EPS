package com.jimi.smt.eps_appclient;

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

import com.jimi.smt.eps_appclient.Adapter.FeedMaterialAdapter;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.FeedMaterialItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caobotao on 16/1/4.
 */
public class CheckAllMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    private static final int DATA_CAPACITY = 20;
    //上料视图
    private View vCheckAllMaterialFragment;
    //操作员　站位　料号
    private EditText edt_Operation, edt_LineSeat, edt_ScanMaterial;

    //上料列表
    private ListView lv_FeedMaterial;
    private List<FeedMaterialItem> mList = new ArrayList<FeedMaterialItem>(DATA_CAPACITY);
    private FeedMaterialAdapter feedMaterialAdapter;

    //当前测试项
    int TestId = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vCheckAllMaterialFragment = inflater.inflate(R.layout.checkallmaterial_layout, container, false);

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
        edt_LineSeat = (EditText) vCheckAllMaterialFragment.findViewById(R.id.edt_Lineseat);
        edt_ScanMaterial = (EditText) vCheckAllMaterialFragment.findViewById(R.id.edt_material);

        lv_FeedMaterial = (ListView) vCheckAllMaterialFragment.findViewById(R.id.list_view);
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化事件
     */
    private void initEvents() {
        Log.i(TAG,"initEvents");
        edt_Operation.setOnEditorActionListener(this);
        edt_LineSeat.setOnEditorActionListener(this);
        edt_ScanMaterial.setOnEditorActionListener(this);
    }

    /**
     * @author connie
     * @time 2017-9-22
     * @describe 初始化数据
     */
    private void initData() {
        Log.i(TAG,"initData");
        //填充数据
        for (int i = 0; i < DATA_CAPACITY; i++) {
            FeedMaterialItem feedMaterialItem;
            if (i < 10) {
                feedMaterialItem = new FeedMaterialItem("306010" + i, "6940105300121", "", "", "", "");
            } else {
                feedMaterialItem = new FeedMaterialItem("30601" + i, "6940105300121", "", "", "", "");
            }
            mList.add(feedMaterialItem);
        }
        //设置Adapter
        feedMaterialAdapter = new FeedMaterialAdapter(this.getActivity(), mList);
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
        Log.i(TAG,"onEditorAction");
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
                    FeedMaterialItem feedMaterialItem = mList.get(TestId);
                    switch (textView.getId()) {
                        case R.id.edt_Operation:
                            //操作员格式不对
                            if (strValue.length()!=10){

                            }
                            break;
                        case R.id.edt_lineseat:
                            //站位
                            if (edt_LineSeat.length()!=7){
                                feedMaterialItem.setRemark("站位长度不正确");
                            }
                            else{
                                feedMaterialItem.setRemark("");
                            }
                            feedMaterialItem.setScanLineSeat(strValue);
                            feedMaterialAdapter.notifyDataSetChanged();
                            break;
                        case R.id.edt_material:
                            //料号
                            feedMaterialItem.setScanMaterial(strValue);
                            feedMaterialAdapter.notifyDataSetChanged();

                            //1秒后进行下一轮测试
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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
        Log.i(TAG, "testNext:" + TestId);
        if (TestId < DATA_CAPACITY - 1) {
            TestId++;
            edt_LineSeat.setText("");
            edt_ScanMaterial.setText("");
            edt_Operation.requestFocus();
        }
    }
}
