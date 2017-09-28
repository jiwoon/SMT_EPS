package com.jimi.smt.eps_appclient.Func;

import android.content.Context;

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
