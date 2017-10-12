package com.jimi.smt.eps_appclient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.ArrayList;
import java.util.List;


/**
 * 类名:EPS_MainActivity
 * 创建人:Connie
 * 创建时间:2017-9-25
 * 描述:EPS主Activity
 * 版本号:V1.0
 * 修改记录:
 */
public class EPS_MainActivity extends FragmentActivity implements OnClickListener {
    private final String TAG = this.getClass().getSimpleName();

    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;

    //四个Tab对应的布局
    private LinearLayout mTabFeedMaterial;
    private LinearLayout mTabChangeMaterial;
    private LinearLayout mTabCheckMaterial;
    private LinearLayout mTabCheckAllMaterial;

    //四个Tab对应的ImageButton
    private ImageButton mImgFeedMaterial;
    private ImageButton mImgChangeMaterial;
    private ImageButton mImgCheckMaterial;
    private ImageButton mImgCheckAllMaterial;

    //获取SD卡运行时权限
//    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    //进度框
    private ProgressDialog progressDialog = null;
    android.app.AlertDialog dialog = null;

    private final int MSG_GETMATERIAL = 1;          //获得料号列表
    GlobalData globalData;

    private Handler mainActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GETMATERIAL:
                    Log.i(TAG, "MSG_GETMATERIAL");
                    progressDialog.dismiss();
                    dialog.dismiss();

                    if (globalData.getMaterialItems().size()>0){
                        initViews();//初始化控件
                        initEvents();//初始化事件
                        initDatas();//初始化数据
                    }
                    else{
                        //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        AlertDialog.Builder builder = new AlertDialog.Builder(EPS_MainActivity.this);
                        //设置Title的图标
                        builder.setIcon(android.R.drawable.ic_dialog_info);
                        //设置Title的内容
                        builder.setTitle("提示");
                        builder.setMessage("请确认当前网络是否正常或线号是否正确!!!");

                        //设置一个PositiveButton
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getMaterial();
                            }
                        });
                        //显示出该对话框
                        builder.show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eps__main);

        //获得全局变量
        globalData = (GlobalData) getApplication();
        getMaterial();
    }

    /**
     * @author connie
     * @time 2017-9-25
    * @describe  接收线号并根据该线号获得相应的料号表
     */
    private void getMaterial() {
        Log.i(TAG,"getMaterial");
        //弹出对话框
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.inputline_dialog, null);
        final EditText etLineNo = (EditText) view.findViewById(R.id.et_LineNo);
//        final EditText edtOperation= (EditText) view.findViewById(R.id.edt_Operation);
        builder.setView(view);
        builder.setIcon(android.R.drawable.ic_dialog_info);
//        edtOperation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                globalData.setOperator(String.valueOf(((EditText) textView).getText()));
//                return false;
//            }
//        });
        etLineNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView textView, int i, KeyEvent keyEvent) {
                progressDialog = ProgressDialog.show(EPS_MainActivity.this, "请稍等...", "获取数据中...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获得料号表
                        String LineNo="";
                        LineNo= String.valueOf(((EditText) textView).getText());
                        LineNo=LineNo.substring(0, 3);
//                        LineNo=((EditText) textView).getText()
//                        List<MaterialItem>materialItems = new DBService().getMaterial(LineNo);
                        List<MaterialItem> materialItems = new DBService().getMaterial("308");
                        globalData.setMaterialItems(materialItems);
                        Log.i(TAG, "getMaterial finish");
                        Message msg_netData = new Message();
                        msg_netData.what = MSG_GETMATERIAL;
                        mainActivityHandler.sendMessage(msg_netData);
                        if (materialItems.size() > 0) {

                        }

                    }
                }).start();
                return false;
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    //初始化控件
    private void initViews() {
        Log.i(TAG,"initViews");
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mViewPager.setOffscreenPageLimit(4);

        mTabFeedMaterial = (LinearLayout) findViewById(R.id.id_tab_FeedMaterial);
        mTabChangeMaterial = (LinearLayout) findViewById(R.id.id_tab_ChangeMaterial);
        mTabCheckMaterial = (LinearLayout) findViewById(R.id.id_tab_CheckMaterial);
        mTabCheckAllMaterial = (LinearLayout) findViewById(R.id.id_tab_CheckAllMaterial);

        mImgFeedMaterial = (ImageButton) findViewById(R.id.id_tab_FeedMaterial_img);
        mImgChangeMaterial = (ImageButton) findViewById(R.id.id_tab_ChangeMaterial_img);
        mImgCheckMaterial = (ImageButton) findViewById(R.id.id_tab_CheckMaterial_img);
        mImgCheckAllMaterial = (ImageButton) findViewById(R.id.id_tab_CheckAllMaterial_img);
    }

    private void initEvents() {
        Log.i(TAG,"initEvents");
        //设置四个Tab的点击事件
        mTabFeedMaterial.setOnClickListener(this);
        mTabChangeMaterial.setOnClickListener(this);
        mTabCheckMaterial.setOnClickListener(this);
        mTabCheckAllMaterial.setOnClickListener(this);
    }

    private void initDatas() {
        try {
            Log.i(TAG, "initDatas");
            mFragments = new ArrayList<>();
            //将四个Fragment加入集合中
            mFragments.add(new FeedMaterialFragment());
            mFragments.add(new ChangeMaterialFragment());
            mFragments.add(new CheckMaterialFragment());
            mFragments.add(new CheckAllMaterialFragment());

            //初始化适配器
            mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                    return mFragments.get(position);
                }

                @Override
                public int getCount() {//获取集合中Fragment的总数
                    return mFragments.size();
                }

            };
            //不要忘记设置ViewPager的适配器
            mViewPager.setAdapter(mAdapter);
            //设置ViewPager的切换监听
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                //页面滚动事件
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                //页面选中事件
                @Override
                public void onPageSelected(int position) {
                    //设置position对应的集合中的Fragment
                    mViewPager.setCurrentItem(position);
                    resetImgs();
                    selectTab(position);
                }

                @Override
                //页面滚动状态改变事件
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG,"onClick");
        //先将四个ImageButton置为灰色
        resetImgs();

        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.id_tab_FeedMaterial:
                selectTab(0);
                break;
            case R.id.id_tab_ChangeMaterial:
                selectTab(1);
                break;
            case R.id.id_tab_CheckMaterial:
                selectTab(2);
                break;
            case R.id.id_tab_CheckAllMaterial:
                selectTab(3);
                break;
        }
    }

    private void selectTab(int i) {
        Log.i(TAG,"selectTab:"+i);
        globalData.setOperType(i);
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgFeedMaterial.setImageResource(R.mipmap.btn_feedmaterial_pressed);
                break;
            case 1:
                mImgChangeMaterial.setImageResource(R.mipmap.btn_changematerial_pressed);
                break;
            case 2:
                mImgCheckMaterial.setImageResource(R.mipmap.btn_checkmaterial_pressed);
                break;
            case 3:
                mImgCheckAllMaterial.setImageResource(R.mipmap.btn_checkallmaterial_pressed);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将四个ImageButton设置为灰色
    private void resetImgs() {
        Log.i(TAG,"resetImgs");
        mImgFeedMaterial.setImageResource(R.mipmap.btn_feedmaterial_normal);
        mImgChangeMaterial.setImageResource(R.mipmap.btn_changematerial_normal);
        mImgCheckMaterial.setImageResource(R.mipmap.btn_checkmaterial_normal);
        mImgCheckAllMaterial.setImageResource(R.mipmap.btn_checkallmaterial_normal);
    }
}
