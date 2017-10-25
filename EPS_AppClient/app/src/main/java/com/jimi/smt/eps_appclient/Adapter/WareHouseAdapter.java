package com.jimi.smt.eps_appclient.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.List;

/**
 * 类名:WareHouseAdapter
 * 创建人:Liang GuoChang
 * 创建时间:2017/10/20 15:32
 * 描述:
 * 版本号:
 * 修改记录:
 */

public class WareHouseAdapter extends BaseAdapter{
    private final String TAG="WareHouseAdapter";
    private Context context;
    private List<MaterialItem> materialItems;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;

    public WareHouseAdapter(Context context, List<MaterialItem> materialItems){
        this.context=context;
        this.materialItems=materialItems;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (materialItems.size() > 0) ? (materialItems.size()) : 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.warehouse_item,null);
            viewHolder.tv_lineSeat= (TextView) convertView.findViewById(R.id.tv_wareLineSeat);
            viewHolder.tv_material= (TextView) convertView.findViewById(R.id.tv_wareMaterial);
            viewHolder.view_blank=convertView.findViewById(R.id.view_wareBlank);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //显示结果
        MaterialItem materialItem=materialItems.get(position);
        viewHolder.tv_lineSeat.setText(materialItem.getOrgLineSeat());
        viewHolder.tv_material.setText(materialItem.getOrgMaterial());
        viewHolder.view_blank.setBackgroundColor(Color.BLACK);
        //被匹配的项
        Log.d(TAG,"position="+position);
        Log.d(TAG,"OrgLineSeat"+materialItem.getOrgLineSeat());
        Log.d(TAG,"result="+materialItem.getResult());
        if (materialItem.getResult()!=null && materialItem.getResult().equalsIgnoreCase("PASS")){
            viewHolder.tv_material.setBackgroundColor(Color.GREEN);
            viewHolder.tv_lineSeat.setBackgroundColor(Color.GREEN);
        }else {
            viewHolder.tv_material.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.tv_lineSeat.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;
    }

    static final class ViewHolder{
        TextView tv_lineSeat;
        TextView tv_material;
        View view_blank;
    }
}
