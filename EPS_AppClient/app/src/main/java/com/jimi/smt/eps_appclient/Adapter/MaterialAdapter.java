package com.jimi.smt.eps_appclient.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.List;

/**
 * Created by think on 2017/9/20.
 */
public class MaterialAdapter extends BaseAdapter {
    private ViewHolder mViewHolder;
    private LayoutInflater mLayoutInflater;
    private List<MaterialItem> materialItems;

    public MaterialAdapter(Context context, List<MaterialItem> list) {
        mLayoutInflater = LayoutInflater.from(context);
        materialItems = list;
    }

    @Override
    public int getCount() {
        return materialItems.size();
    }

    @Override
    public Object getItem(int position) {
        return materialItems.get(position);
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
        MaterialItem materialItem = materialItems.get(position);
        mViewHolder.tv_orgLineSeat.setText(materialItem.getOrgLineSeat());
        mViewHolder.tv_orgMaterial.setText(materialItem.getOrgMaterial());
        mViewHolder.tv_scanLineSeat.setText(materialItem.getScanLineSeat());
        mViewHolder.tv_scanMaterial.setText(materialItem.getScanMaterial());
        mViewHolder.tv_Result.setText(materialItem.getResult());
        mViewHolder.tv_Remark.setText(materialItem.getRemark());

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
