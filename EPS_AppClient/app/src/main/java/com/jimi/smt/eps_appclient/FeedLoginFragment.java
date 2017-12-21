package com.jimi.smt.eps_appclient;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.Activity.FactoryLineActivity;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;


/**
 * 类名:FeedLoginFragment
 * 创建人:Liang GuoChang
 * 创建时间:2017-12-19
 * 描述:解锁二次上料Fragment
 * 版本号:V1.0
 * 修改记录:
 */
public class FeedLoginFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "FeedLoginFragment";
    private View mFeedLoginFragment;
    private EditText edt_pwd;
    private FactoryLineActivity mActivity;
    private GlobalData globalData;
    private LoadingDialog loadingDialog;
    private static final int UNLOCK = 120;

    private Handler feedLoginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UNLOCK:
                    //取消弹出窗
                    if (loadingDialog != null && loadingDialog.isShowing()){
                        loadingDialog.cancel();
                        loadingDialog.dismiss();
                    }
                    mActivity.setSelectTabTitle(1);
                    edt_pwd.setText("");
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (FactoryLineActivity) getActivity();
        globalData = (GlobalData) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFeedLoginFragment = inflater.inflate(R.layout.fragment_feed_login, container, false);

        initView();
        return mFeedLoginFragment;
    }

    private void initView() {
        edt_pwd = (EditText) mFeedLoginFragment.findViewById(R.id.edt_pwd);
        Button btn_unlock = (Button) mFeedLoginFragment.findViewById(R.id.btn_unlock);
        Button btn_cancel = (Button) mFeedLoginFragment.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_unlock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_unlock:
                unlock();
                break;

            case R.id.btn_cancel:
                if (!TextUtils.isEmpty(edt_pwd.getText().toString().trim())){
                    edt_pwd.setText("");
                }
                mActivity.setSelectTabTitle(2);
                break;

        }
    }

    //检测密码是否正确,正确则登陆
    private void unlock() {
        if (!TextUtils.isEmpty(edt_pwd.getText().toString().trim())){
            String pwd = edt_pwd.getText().toString().trim();
            if (pwd.equalsIgnoreCase(Constants.feedPwd)){
                loadingDialog = new LoadingDialog(getActivity(),"正在加载...");
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //初始化全部上料结果
                        int result = new DBService().initFeedResult(globalData.getProgram_id());
                        Log.d(TAG,"unlock-result-"+result);
                        Message message = Message.obtain();
                        message.what = UNLOCK;
                        feedLoginHandler.sendMessage(message);
                    }
                }).start();

            }else {
                Toast.makeText(mActivity.getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                edt_pwd.setText("");
                edt_pwd.requestFocus();
            }
        }else {
            Toast.makeText(mActivity.getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
            edt_pwd.requestFocus();
        }
    }
}
