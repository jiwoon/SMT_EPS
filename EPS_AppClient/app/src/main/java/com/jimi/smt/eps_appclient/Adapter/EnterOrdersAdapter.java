package com.jimi.smt.eps_appclient.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Program;

import java.util.List;

/**
 * 类名:EnterOrdersAdapter
 * 创建人:Liang GuoChang
 * 创建时间:2017/11/8 10:47
 * 描述:
 * 版本号:
 * 修改记录:
 */

public class EnterOrdersAdapter extends BaseAdapter{
    private final String TAG="EnterOrdersAdapter";
    private List<Program> programList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;


    public EnterOrdersAdapter(Context context,List<Program> programs){
        this.mContext = context;
        this.programList = programs;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (programList.size() > 0) ? (programList.size()) : 0;
    }

    @Override
    public Object getItem(int position) {
        return programList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.enter_orders_item,null);
            viewHolder.tv_order = (TextView) convertView.findViewById(R.id.tv_order_no);
            viewHolder.tv_board_type = (TextView) convertView.findViewById(R.id.tv_board_type);
            viewHolder.order_cb = (CheckBox) convertView.findViewById(R.id.order_cb);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //显示工单
        Program program = programList.get(position);
        String board_type = "";
        viewHolder.tv_order.setText(program.getWork_order());
        //0：默认 1：AB面 2：A面 3:B面
        switch (program.getBoard_type()){
            case 0:
                board_type = "默认";
                break;
            case 1:
                board_type = "AB面";
                break;
            case 2:
                board_type = "A面";
                break;
            case 3:
                board_type = "B面";
                break;
        }
        viewHolder.tv_board_type.setText(board_type);
        viewHolder.order_cb.setChecked(program.isChecked());
        if (program.isChecked()){
            viewHolder.tv_order.setTextColor(Color.argb(255,34,164,65));
            viewHolder.tv_board_type.setTextColor(Color.argb(255,34,164,65));
        }else {
            viewHolder.tv_order.setTextColor(Color.BLACK);
            viewHolder.tv_board_type.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    public final class ViewHolder{
        public TextView tv_order;
        public TextView tv_board_type;
        public CheckBox order_cb;
    }
}
