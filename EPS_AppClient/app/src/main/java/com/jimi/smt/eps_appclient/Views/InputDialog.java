package com.jimi.smt.eps_appclient.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.R;

/**
 * 类名:InputDialog
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/17 9:29
 * 描述: 弹出输入框
 * 版本号:V-1.0
 * 修改记录:
 */

public class InputDialog extends Dialog implements TextView.OnEditorActionListener {

    private Context context;
    //布局id
    private int layoutId;
    //所用控件id
    private int[] itemResIds;
    //标题
    private String title;
    private OnDialogEditorActionListener listener;

    /**
     * 构造方法
     *
     * @param context     上下文
     * @param layoutResId 布局id
     * @param itemResIds  所有控件id
     * @param title       标题
     */
    public InputDialog(Context context, int layoutResId, int[] itemResIds, String title) {
        super(context, R.style.dialog_custom);
        this.context = context;
        this.layoutId = layoutResId;
        this.itemResIds = itemResIds;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置对话框弹出样式
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);//居中
        //设置布局
        setContentView(layoutId);

        // 宽度全屏
        WindowManager windowManager = ((Activity) context).getWindowManager();

        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 5 / 6; // 设置dialog宽度为屏幕的5/6
        getWindow().setAttributes(lp);

        //设置点击外部消失
        setCanceledOnTouchOutside(true);
        if (itemResIds != null) {
            //设置标题
            ((TextView) findViewById(itemResIds[0])).setText(title);
            //设置输入框监听事件
            ((EditText) findViewById(itemResIds[1])).setOnEditorActionListener(this);
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return listener.OnDialogEditorAction(this, v, actionId, event);
    }

    public interface OnDialogEditorActionListener {
        boolean OnDialogEditorAction(InputDialog inputDialog, TextView v, int actionId, KeyEvent event);
    }

    public void setOnDialogEditorActionListener(OnDialogEditorActionListener listener) {
        this.listener = listener;
    }
}
