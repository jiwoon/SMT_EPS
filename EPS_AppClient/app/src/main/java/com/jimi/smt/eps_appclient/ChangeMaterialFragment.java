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
import android.widget.ListView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.FeedMaterialItem;

/**
 * Created by caobotao on 16/1/4.
 */
public class ChangeMaterialFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    View vChangeMaterialFragment;
    //操作员　站位　料号
    private EditText edt_Operation, edt_LineSeat, edt_OrgMaterial, edt_ChgMaterial;
    private TextView tv_Result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vChangeMaterialFragment = inflater.inflate(R.layout.changematerial_layout, container, false);

        initViews();
        initEvents();
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

        tv_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testAgain();
            }
        });
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
                            break;
                        case R.id.edt_lineseat:
                            //站位
                            break;
                        case R.id.edt_OrgMaterial:
                            //线上料号
                            break;
                        case R.id.edt_ChgMaterial:
                            //更换料号
                            if (edt_ChgMaterial.getText().toString().equals(edt_OrgMaterial.getText().toString())) {
                                tv_Result.setBackgroundColor(Color.GREEN);
                                tv_Result.setText("PASS");
                            } else {
                                tv_Result.setBackgroundColor(Color.RED);
                                tv_Result.setText("FAIL");
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
     * @author connie
     * @time 2017-9-22
     * @describe 测试下一项
     */
    private void testAgain() {
        Log.i(TAG, "testAgain");
        tv_Result.setText("");
        tv_Result.setBackgroundColor(Color.TRANSPARENT);
        edt_LineSeat.setText("");
        edt_OrgMaterial.setText("");
        edt_ChgMaterial.setText("");
        edt_LineSeat.requestFocus();
    }
}
