package com.jimi.smt.eps_appclient.Activity;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.Fragment.ChangeMaterialFragment;
import com.jimi.smt.eps_appclient.Fragment.CheckAllMaterialFragment;
import com.jimi.smt.eps_appclient.Fragment.FeedLoginFragment;
import com.jimi.smt.eps_appclient.Fragment.FeedMaterialFragment;
import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Service.RefreshCacheService;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.EvenBusTest;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.Unit.ProgramItemVisit;
import com.jimi.smt.eps_appclient.Views.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:FactoryLineActivity
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/19 20:03
 * 描述: 厂线 Activity
 */
public class FactoryLineActivity extends FragmentActivity implements View.OnClickListener {

    private final String TAG = "FactoryLineActivity";
    private FeedLoginFragment feedLoginFragment;
    private FeedMaterialFragment feedMaterialFragment;
    private ChangeMaterialFragment changeMaterialFragment;
    private CheckAllMaterialFragment checkAllMaterialFragment;
    private FragmentManager fragmentManager;
    private int oldFragmentIndex = 0;
    private TextView tv_factory_feed;
    private TextView tv_factory_change;
    private TextView tv_factory_checkAll;
    private GlobalData globalData;
    public LinearLayout factory_parent;
    private LoadingDialog loadingDialog;
    //program_item_visit表
    private List<ProgramItemVisit> programItemVisits = new ArrayList<ProgramItemVisit>();
    private static final int REFRESH_ITEMS = 138;
    private static final int EXIT = 139;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    public LoadingDialog updateDialog;

    @SuppressLint("HandlerLeak")
    private Handler mFactoryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case REFRESH_ITEMS:
                    //取消弹出窗
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.cancel();
                        loadingDialog.dismiss();
                    }
                    if (getFeedResult(programItemVisits)) {
                        setTabSelection(0);
                    } else {
                        setTabSelection(1);
                    }
                    break;
                case EXIT:
                    isExit = false;
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_factory_line);
        //使屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        globalData = (GlobalData) getApplication();
        //开启服务
        startService(new Intent(this, RefreshCacheService.class));
        //注册订阅
        EventBus.getDefault().register(this);

        initView();
        fragmentManager = getSupportFragmentManager();

        //判断是否上料完成
        getProgramItemVisits(globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type());
    }

    //初始化布局
    private void initView() {
        ImageView iv_factory_back = (ImageView) findViewById(R.id.iv_factory_back);
        factory_parent = (LinearLayout) findViewById(R.id.factory_parent);
        tv_factory_feed = (TextView) findViewById(R.id.tv_factory_feed);
        tv_factory_change = (TextView) findViewById(R.id.tv_factory_change);
        tv_factory_checkAll = (TextView) findViewById(R.id.tv_factory_checkAll);
        iv_factory_back.setOnClickListener(this);
        tv_factory_feed.setOnClickListener(this);
        tv_factory_change.setOnClickListener(this);
        tv_factory_checkAll.setOnClickListener(this);
    }

    //监听订阅的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenBusTest event) {
        Log.d(TAG, "onEventMainThread - " + event.getUpdated());
        if (event.getUpdated() == 0) {
            showUpdateDialog();
        }
    }

    private void showUpdateDialog() {
        Log.d(TAG, "showUpdateDialog");
        updateDialog = new LoadingDialog(this, "站位表更新...");
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (updateDialog != null && updateDialog.isShowing()) {
                    updateDialog.cancel();
                    updateDialog.dismiss();
                }
                globalData.setUpdateProgram(false);

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_factory_back:
                exit();
                break;

            case R.id.tv_factory_feed://上料

                if (oldFragmentIndex != 0 && oldFragmentIndex != 1) {
                    //判断是否上料完成
                    getProgramItemVisits(globalData.getLine(), globalData.getWork_order(), globalData.getBoard_type());
                }

                break;

            case R.id.tv_factory_change://换料
                setTabSelection(2);
                break;

            case R.id.tv_factory_checkAll://全检
                setTabSelection(3);
                break;
        }
    }

    //获取ProgramItemVisits
    private void getProgramItemVisits(final String line, final String order, final int boardType) {
        loadingDialog = new LoadingDialog(this, "正在加载...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                programItemVisits = new DBService().getProgramItemVisits(line, order, boardType);
                Message message = Message.obtain();
                message.what = REFRESH_ITEMS;
                mFactoryHandler.sendMessage(message);
            }
        }).start();
    }

    //判断所有上料结果
    private boolean getFeedResult(List<ProgramItemVisit> programItemVisits) {
        boolean feedResult = true;
        if (programItemVisits != null && programItemVisits.size() > 0) {
            for (ProgramItemVisit itemVisit : programItemVisits) {
                if (itemVisit.getFeed_result() != 1) {
                    feedResult = false;
                    break;
                }
            }
        }
        return feedResult;
    }

    public void setTabSelection(int index) {
        resetTitle();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        oldFragmentIndex = index;
        switch (index) {
            case 0:
                globalData.setOperType(Constants.FEEDMATERIAL);
                tv_factory_feed.setBackgroundResource(R.drawable.factory_feed_click_shape);
                if (feedLoginFragment == null) {
                    feedLoginFragment = new FeedLoginFragment();
                    transaction.add(R.id.factory_layouts_content, feedLoginFragment);
                } else {
                    transaction.show(feedLoginFragment);
                }
                break;

            case 1:
                globalData.setOperType(Constants.FEEDMATERIAL);
                tv_factory_feed.setBackgroundResource(R.drawable.factory_feed_click_shape);
                if (feedMaterialFragment == null) {
                    feedMaterialFragment = new FeedMaterialFragment();
                    transaction.add(R.id.factory_layouts_content, feedMaterialFragment);
                } else {
                    transaction.show(feedMaterialFragment);
                }
                break;

            case 2:
                globalData.setOperType(Constants.CHANGEMATERIAL);
                tv_factory_change.setBackgroundResource(R.drawable.factory_change_click_shape);
                if (changeMaterialFragment == null) {
                    changeMaterialFragment = new ChangeMaterialFragment();
                    transaction.add(R.id.factory_layouts_content, changeMaterialFragment);
                } else {
                    transaction.show(changeMaterialFragment);
                }
                break;

            case 3:
                globalData.setOperType(Constants.CHECKALLMATERIAL);
                tv_factory_checkAll.setBackgroundResource(R.drawable.factory_checkall_click_shape);
                if (checkAllMaterialFragment == null) {
                    checkAllMaterialFragment = new CheckAllMaterialFragment();
                    transaction.add(R.id.factory_layouts_content, checkAllMaterialFragment);
                } else {
                    transaction.show(checkAllMaterialFragment);
                }
                break;

        }
        transaction.commit();
    }

    //设置标题为原状态
    private void resetTitle() {
        tv_factory_feed.setBackgroundResource(R.drawable.factory_feed_unclick_shape);
        tv_factory_change.setBackgroundResource(R.drawable.factory_change_unclick_shape);
        tv_factory_checkAll.setBackgroundResource(R.drawable.factory_checkall_unclick_shape);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (feedLoginFragment != null) {
            transaction.hide(feedLoginFragment);
        }
        if (feedMaterialFragment != null) {
            transaction.hide(feedMaterialFragment);
        }
        if (changeMaterialFragment != null) {
            transaction.hide(changeMaterialFragment);
        }
        if (checkAllMaterialFragment != null) {
            transaction.hide(checkAllMaterialFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    //物理返回键
    @Override
    public void onBackPressed() {
        exit();
    }

    //返回主页
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            Message message = Message.obtain();
            message.what = EXIT;
            mFactoryHandler.sendMessageDelayed(message, 2000);
        } else {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        //注销订阅
        EventBus.getDefault().unregister(this);
        //关闭服务
        stopService(new Intent(this, RefreshCacheService.class));
        super.onDestroy();
    }

}
