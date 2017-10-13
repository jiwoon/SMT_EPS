package com.jimi.smt.eps_appclient.Func;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jimi.smt.eps_appclient.GlobalData;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2017/9/27.
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
