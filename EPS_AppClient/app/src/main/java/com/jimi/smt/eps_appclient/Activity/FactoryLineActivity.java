package com.jimi.smt.eps_appclient.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.ChangeMaterialFragment;
import com.jimi.smt.eps_appclient.CheckAllMaterialFragment;
import com.jimi.smt.eps_appclient.FeedMaterialFragment;
import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.GlobalData;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:FactoryLineActivity
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/19 20:03
 * 描述: 厂线 Activity
 */
public class FactoryLineActivity extends FragmentActivity implements View.OnClickListener {

    private final String TAG="FactoryLineActivity";
    private TextView tv_factory_feed;
    private TextView tv_factory_change;
    private String curOrderNum;
    private String curOperatorNUm;
    private TextView tv_factory_checkAll;
    private GlobalData globalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_factory_line);
//        Intent intent=getIntent();
//        savedInstanceState=intent.getExtras();
//        curOrderNum =savedInstanceState.getString("orderNum");
//        curOperatorNUm =savedInstanceState.getString("operatorNum");
        globalData = (GlobalData) getApplication();
        initView();
        //设置选中标题
        setSelectTabTitle(0);
    }

    //初始化布局
    private void initView(){
        ImageView iv_factory_back= (ImageView) findViewById(R.id.iv_factory_back);
        tv_factory_feed = (TextView) findViewById(R.id.tv_factory_feed);
        tv_factory_change = (TextView) findViewById(R.id.tv_factory_change);
        tv_factory_checkAll = (TextView) findViewById(R.id.tv_factory_checkAll);
        ViewPager viewpager_factory= (ViewPager) findViewById(R.id.viewpager_factory);
        iv_factory_back.setOnClickListener(this);
        tv_factory_feed.setOnClickListener(this);
        tv_factory_change.setOnClickListener(this);
        tv_factory_checkAll.setOnClickListener(this);
        //fragment集合
        final List<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new FeedMaterialFragment());
        fragmentList.add(new ChangeMaterialFragment());
        fragmentList.add(new CheckAllMaterialFragment());
        //fragment适配器
        FragmentPagerAdapter fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        //设置适配器
        viewpager_factory.setAdapter(fragmentPagerAdapter);
        //设置viewpager切换事件监听
        viewpager_factory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滑动事件
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置选中标题
                setSelectTabTitle(position);
            }

            //页面滚动状态改变事件
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_factory_back:
                Intent intent=getIntent();
                Bundle bundle=intent.getExtras();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                this.finish();
                break;
        }
    }

    //设置选中页面时标题
    private void setSelectTabTitle(int tab){
        resetTitle();
        switch (tab){
            case 0:
                globalData.setOperType(Constants.FEEDMATERIAL);
                tv_factory_feed.setBackgroundResource(R.drawable.factory_feed_click_shape);
                break;
            case 1:
                globalData.setOperType(Constants.CHANGEMATERIAL);
                tv_factory_change.setBackgroundResource(R.drawable.factory_change_click_shape);
                break;
            case 2:
                globalData.setOperType(Constants.CHECKALLMATERIAL);
                tv_factory_checkAll.setBackgroundResource(R.drawable.factory_checkall_click_shape);
                break;
        }
        Log.d(TAG,"setSelectTabTitle-globalData-OperType:"+globalData.getOperType());
    }

    //设置标题为原状态
    private void resetTitle(){
        tv_factory_feed.setBackgroundResource(R.drawable.factory_feed_unclick_shape);
        tv_factory_change.setBackgroundResource(R.drawable.factory_change_unclick_shape);
        tv_factory_checkAll.setBackgroundResource(R.drawable.factory_checkall_unclick_shape);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    //物理返回键
    @Override
    public void onBackPressed() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        this.finish();
    }
}
