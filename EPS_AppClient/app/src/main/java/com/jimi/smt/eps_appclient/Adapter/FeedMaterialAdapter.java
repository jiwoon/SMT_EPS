package com.jimi.smt.eps_appclient.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Func.DBService;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.FeedMaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by think on 2017/9/20.
 */
public class FeedMaterialAdapter extends BaseAdapter {
    private static final String TAG = "FeedMaterialAdapter";
    private ViewHolder mViewHolder;
    private LayoutInflater mLayoutInflater;
    private List<FeedMaterialItem> feedMaterialItems;

    public FeedMaterialAdapter(Context context, List<FeedMaterialItem> list) {
        mLayoutInflater = LayoutInflater.from(context);
        feedMaterialItems = list;
    }

    @Override
    public int getCount() {
        return feedMaterialItems.size();
    }

    @Override
    public Object getItem(int position) {
        return feedMaterialItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.feedmaterial_item, null);
            mViewHolder.tv_orgLineSeat = (TextView) convertView.findViewById(R.id.tv_orgLineSeat);
            mViewHolder.tv_orgMaterial = (TextView) convertView.findViewById(R.id.tv_orgMaterial);
            mViewHolder.tv_scanLineSeat = (TextView) convertView.findViewById(R.id.tv_scanLineSeat);
            mViewHolder.tv_scanMaterial = (TextView) convertView.findViewById(R.id.tv_scanMaterial);
            mViewHolder.tv_Result = (TextView) convertView.findViewById(R.id.tv_Result);
            mViewHolder.tv_Remark = (TextView) convertView.findViewById(R.id.tv_Remark);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        //显示相关测试结果
        FeedMaterialItem feedMaterialItem = feedMaterialItems.get(position);
        mViewHolder.tv_orgLineSeat.setText(feedMaterialItem.getOrgLineSeat());
        mViewHolder.tv_orgMaterial.setText(feedMaterialItem.getOrgMaterial());
        mViewHolder.tv_scanLineSeat.setText(feedMaterialItem.getScanLineSeat());
        mViewHolder.tv_scanMaterial.setText(feedMaterialItem.getScanMaterial());
        mViewHolder.tv_Result.setText(feedMaterialItem.getResult());
        mViewHolder.tv_Remark.setText(feedMaterialItem.getRemark());

        if (mViewHolder.tv_Result.getText().equals("PASS")) {
            mViewHolder.tv_Result.setBackgroundColor(Color.GREEN);
        } else if (mViewHolder.tv_Result.getText().equals("ERROR")) {
            mViewHolder.tv_Result.setBackgroundColor(Color.YELLOW);
        } else if (mViewHolder.tv_Result.getText().equals("FAIL")) {
            mViewHolder.tv_Result.setBackgroundColor(Color.RED);
        } else {
            mViewHolder.tv_Result.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;

    }

    static final class ViewHolder {
        TextView tv_orgLineSeat, tv_orgMaterial, tv_scanLineSeat, tv_scanMaterial, tv_Result, tv_Remark;
    }

}
