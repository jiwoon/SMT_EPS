package com.jimi.smt.eps_appclient.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.R;

/**
 * 类名:InfoDialog
 * 创建人:LiangGuoChang
 * 创建时间:2017/10/14 14:53
 * 描述:弹出消息对话框
 * 版本号:V-1.0
 * 修改记录:
 */
public class InfoDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private int layoutResId;
    //控件的id 第0个为标题控件
    private int[] itemResIds;
    //消息{标题,内容}
    private String[] titleInfo;
    //内容样式,{大小,颜色}
    private int[] msgStype;

    private OnDialogItemClickListener listener;

    /**
     * 提示框构造函数
     *
     * @param context     上下文
     * @param layoutResID 布局id
     * @param itemResIDs  所有控件id
     * @param titleMsg    标题与内容数组
     * @param style       消息内容的样式
     */
    public InfoDialog(Context context, int layoutResID, int[] itemResIDs, String[] titleMsg, int[] style) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.layoutResId = layoutResID;
        this.itemResIds = itemResIDs;
        this.titleInfo = titleMsg;
        this.msgStype = style;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置对话框弹出样式
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);//居中
        //设置布局
        setContentView(layoutResId);

        // 宽度全屏
        WindowManager windowManager = ((Activity) context).getWindowManager();

        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 5 / 6; // 设置dialog宽度为屏幕的5/6
        getWindow().setAttributes(lp);

        //设置点击外部消失
        setCanceledOnTouchOutside(false);
        //设置标题和内容
        TextView titleTv = (TextView) findViewById(itemResIds[1]);
        TextView msgTv = (TextView) findViewById(itemResIds[2]);
        if (titleInfo != null) {
            //设置title消息
            titleTv.setText(titleInfo[0]);
            //设置内容
            msgTv.setText(titleInfo[1]);
            if (msgStype != null) {
                msgTv.setTextSize(msgStype[0]);
                msgTv.setTextColor(msgStype[1]);
                //设置标题栏背景颜色
                findViewById(itemResIds[0]).setBackgroundColor(msgStype[1]);
            }

            //已发料的站位信息
            if (titleInfo.length >= 3) {
                TextView waredSeat = (TextView) findViewById(itemResIds[4]);
                waredSeat.setText(titleInfo[2]);
                waredSeat.setTextColor(msgStype[2]);
            }
        }

        //设置按钮框监听事件
        for (int i = 3; i < itemResIds.length; i++) {
            findViewById(itemResIds[i]).setOnClickListener(this);
        }
    }

    public interface OnDialogItemClickListener {

        void OnDialogItemClick(InfoDialog dialog, View view);

    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
//        dismiss();
        listener.OnDialogItemClick(this, v);
    }
}
