package com.jimi.smt.eps_appclient.Func;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.GlobalData;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;
import com.jimi.smt.eps_appclient.Views.InfoDialog;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名:GlobalFunc
 * 修改人:Liang GuoChang
 * 修改时间:2017/11/07 9:29
 * 描述: 公共方法类
 */
public class GlobalFunc {
    private static final String TAG = "GlobalFunc";
    Context context;

    public GlobalFunc() {
    }

    public GlobalFunc(Context context) {
        //获得全局变量
        this.context=context;
    }

    //operator,time,type,result,lineseat,material_no,old_material_no,
    // scanlineseat,remark,fileid,line,work_order,board_type
    public void AddDBLog(final GlobalData globalData,final MaterialItem materialItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<OperLogItem> operLogItems = new ArrayList<OperLogItem>();
                OperLogItem operLogItem = new OperLogItem();
                operLogItem.setOperator(globalData.getOperator());
                operLogItem.setTime(new Timestamp(System.currentTimeMillis()));
                operLogItem.setType(globalData.getOperType());
                operLogItem.setResult(materialItem.getResult());
                operLogItem.setLineseat(materialItem.getOrgLineSeat());
                operLogItem.setMaterial_no(materialItem.getScanMaterial());
                operLogItem.setOld_material_no(materialItem.getOrgMaterial());
                operLogItem.setScanLineseat(materialItem.getScanLineSeat());
                operLogItem.setRemark(materialItem.getRemark());
                operLogItem.setFileId(materialItem.getFileId());
                operLogItem.setLine(globalData.getLine());
                operLogItem.setWork_order(globalData.getWork_order());
                operLogItem.setBoard_type(globalData.getBoard_type());
                operLogItems.add(operLogItem);
                new DBService().inserOpertLog(operLogItems);
            }
        }).start();
    }

    //检测线号
    public boolean checkLine(String lineNo){
        if (lineNo.length() > 3) {
            lineNo = lineNo.substring(0, 3);
        }
        //301~308
        return true;
    }

    //判断是否有网络链接
    public boolean isNetWorkConnected(){
        ConnectivityManager connectivityManager=
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null){
            Log.d(TAG,"networkInfo::"+networkInfo.isAvailable());
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 提示网络连接失败
     * @param title 提示
     * @param message 提示内容
     * @param netFailToastStr 未联网提示
     */
    public void showInfo(String title, String message, final String netFailToastStr){

        //对话框所有控件id
        int itemResIds[]=new int[]{R.id.dialog_title_view,
                R.id.dialog_title,R.id.tv_alert_info,R.id.info_trust};
        //标题和内容
        String titleMsg[]=new String[]{title,message};
        //内容的样式
        int msgStype[]=new int[]{22, Color.RED};
        InfoDialog infoDialog=new InfoDialog(context,
                R.layout.info_dialog_layout,itemResIds,titleMsg,msgStype);

        infoDialog.setOnDialogItemClickListener(new InfoDialog.OnDialogItemClickListener() {
            @Override
            public void OnDialogItemClick(InfoDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.info_trust:
                        /*
                        if (isNetWorkConnected()){
                            dialog.dismiss();
                            result = true;
                        }else {
                            Toast.makeText(context,netFailToastStr,Toast.LENGTH_LONG).show();
                            result = false;
                        }
                        */
                        dialog.dismiss();
                        Toast.makeText(context,netFailToastStr,Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        infoDialog.show();
    }

    //检测操作员
    public boolean checkOperator(String operator){
        return false;
    }

    //检测站位
    public boolean checkLineSeat(String lineSeat){
        //01-08  02-01
        return false;
    }

    //检测料号
    public boolean checkMaterial(){
        //包含@@
        return false;
    }



    /**
     *
     */
    //    private void AddAllLog() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<OperLogItem> operLogItems = new ArrayList<OperLogItem>();
//                for (FeedMaterialItem feedMaterialItem:mList)
//                {
//                    OperLogItem operLogItem = new OperLogItem();
//                    operLogItem.setOperator("124325");
//                    operLogItem.setTime(new Timestamp(System.currentTimeMillis()));
//                    operLogItem.setType(0);
//                    operLogItem.setResult("PASS");
//                    operLogItem.setLineseat(feedMaterialItem.getScanLineSeat());
//                    operLogItem.setMaterial_no(feedMaterialItem.getScanMaterial());
//                    operLogItem.setOld_material_no(feedMaterialItem.getOrgMaterial());
//                    operLogItems.add(operLogItem);
//                }
//                new DBService().inserOpertLog(operLogItems);
//            }
//        }).start();
//    }

}
