package com.jimi.smt.eps_appclient.Views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.R;

/**
 * 类名:LoadingDialog
 * 创建人:Liang GuoChang
 * 创建时间:2017/11/28 11:49
 * 描述:正在加载旋转进度框
 * 版本号:
 * 修改记录:
 */

public class LoadingDialog extends Dialog {

    private String mTitle = "";
    private TextView tv_title;

    public LoadingDialog(Context context, String title) {
        super(context, R.style.loadingDialogStyle);
        this.mTitle = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tv_title = (TextView) findViewById(R.id.tv);
        tv_title.setText(mTitle);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(230);
    }
}
