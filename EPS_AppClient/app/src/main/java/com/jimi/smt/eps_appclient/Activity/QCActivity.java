package com.jimi.smt.eps_appclient.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.ChangeMaterialFragment;
import com.jimi.smt.eps_appclient.CheckAllMaterialFragment;
import com.jimi.smt.eps_appclient.CheckMaterialFragment;
import com.jimi.smt.eps_appclient.FeedMaterialFragment;
import com.jimi.smt.eps_appclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:QCActivity
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/19 19:29
 * 描述: 质检 Activity
 */
public class QCActivity extends FragmentActivity implements View.OnClickListener {

    private TextView tv_check_some;
    private TextView tv_check_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qc);
        initView();
        //设置选中标题
        setSelectTabTitle(0);
    }

    //初始化布局
    private void initView(){
        ImageView iv_QC_back= (ImageView) findViewById(R.id.iv_QC_back);
        tv_check_some = (TextView) findViewById(R.id.tv_check_some);
        tv_check_all = (TextView) findViewById(R.id.tv_check_all);
        ViewPager viewpager_QC= (ViewPager) findViewById(R.id.viewpager_QC);
        iv_QC_back.setOnClickListener(this);
        tv_check_some.setOnClickListener(this);
        tv_check_all.setOnClickListener(this);
        //设置viewpager切换事件监听
        viewpager_QC.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        //fragment集合
        final List<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new CheckMaterialFragment());
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
        viewpager_QC.setAdapter(fragmentPagerAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_QC_back:
                Intent intent=getIntent();
                Bundle bundle=intent.getExtras();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                this.finish();
                break;
        }
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


    //设置选中页面时标题
    private void setSelectTabTitle(int tab){
        resetTitle();
        switch (tab){
            case 0:
                tv_check_some.setBackgroundResource(R.drawable.factory_feed_click_shape);
                break;
            case 1:
                tv_check_all.setBackgroundResource(R.drawable.factory_change_click_shape);
                break;
        }
    }

    //设置标题为原状态
    private void resetTitle(){
        tv_check_some.setBackgroundResource(R.drawable.factory_feed_unclick_shape);
        tv_check_all.setBackgroundResource(R.drawable.factory_change_unclick_shape);
    }
}
