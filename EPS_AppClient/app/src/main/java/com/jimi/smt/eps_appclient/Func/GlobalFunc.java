package com.jimi.smt.eps_appclient.Func;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.jimi.smt.eps_appclient.GlobalData;
import com.jimi.smt.eps_appclient.R;
import com.jimi.smt.eps_appclient.Unit.Constants;
import com.jimi.smt.eps_appclient.Unit.MaterialItem;
import com.jimi.smt.eps_appclient.Unit.OperLogItem;
import com.jimi.smt.eps_appclient.Unit.ProgramItemVisit;
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

    /**
     * 添加日志
     * @param globalData
     * @param materialItem
     */
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

    /**
     * 更新日志
     * @param globalData
     * @param materialItem
     */
    public void updateVisitLog(final GlobalData globalData, final MaterialItem materialItem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProgramItemVisit programItemVisit = new ProgramItemVisit();
//                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                programItemVisit.setLast_operation_type(globalData.getUpdateType());
//                programItemVisit.setLast_operation_time(timestamp);
                programItemVisit.setProgram_id(materialItem.getFileId());
                programItemVisit.setLineseat(materialItem.getOrgLineSeat());
                programItemVisit.setMaterial_no(materialItem.getOrgMaterial());
                programItemVisit.setScan_lineseat(materialItem.getScanLineSeat());
                programItemVisit.setScan_material_no(materialItem.getScanMaterial());
                byte result = 1;
                if (materialItem.getResult().equalsIgnoreCase("PASS")){
                    result = 1;
                }else if (materialItem.getResult().equalsIgnoreCase("FAIL")){
                    result = 0;
                }
                switch (globalData.getUpdateType()){
                    case 0://上料
                        programItemVisit.setFeed_result(result);
//                        programItemVisit.setFeed_time(timestamp);
                        break;
                    case 1://换料
                        programItemVisit.setChange_result(result);
                        //核料置为false
                        programItemVisit.setCheck_result((byte) 0);
//                        programItemVisit.setChange_time(timestamp);
                        break;
                    case 2://核料
                        programItemVisit.setCheck_result(result);
//                        programItemVisit.setCheck_time(timestamp);
                        break;
                    case 3://全检
                        programItemVisit.setCheck_all_result(result);
//                        programItemVisit.setCheck_all_time(timestamp);
                        break;
                    case 4://发料
                        programItemVisit.setStore_issue_result(result);
//                        programItemVisit.setStore_issue_time(timestamp);
                        break;
                    case 5://首检
                        programItemVisit.setFirst_check_all_result(result);
//                        programItemVisit.setFirst_check_all_time(timestamp);
                        break;
                }
                new DBService().updateItemVisitLog(programItemVisit);
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

    //获取扫描的线号
    public String getLineNum(String scanValue){
        String lineNum = scanValue.replaceAll(" ","").trim();;
        if (scanValue.length() >= 8){
            lineNum = "30"+scanValue.substring(3,4);
        }
        return lineNum;
    }

    //检测线号是否存在
    public boolean checkLineNum(String lineNo){
        boolean lineExist = false;
        for (int i = 0; i < Constants.lines.length; i++){
            if (lineNo.equals(Constants.lines[i])){
                lineExist = true;
            }
        }
        return lineExist;
    }

    //根据扫的站位条码获取站位值 (两者兼容100805118,3040101)
    public String getLineSeat(String scanValue){
        scanValue = scanValue.replaceAll(" ","").trim();
        String linSeat = scanValue;
        if (scanValue.length() >= 8){
            linSeat = scanValue.substring(4,6) + "-" + scanValue.substring(6,8);
        }else if (scanValue.length() == 7){
            linSeat = scanValue.substring(3,5) + "-" + scanValue.substring(5,7);
        }
        return linSeat;
    }

    //获取料号(新料号格式 03.01.0080@1000@1512736259917 ; 旧料号格式 )
    public String getMaterial(String scanValue){
        scanValue = scanValue.replaceAll(" ","").trim();
        String material = scanValue;
        if (scanValue.indexOf("@") != -1) {
            material = scanValue.substring(0, scanValue.indexOf("@"));
        }
        return material;
    }

    //获取料号的流水号(即是时间戳,如料号:03.01.0080@1000@1512736259917)
    public String getSerialNum(String scanValue){
        scanValue = scanValue.replaceAll(" ","").trim();
        String serialNum = scanValue;
        if (scanValue.contains("@")){
            serialNum = scanValue.substring(scanValue.lastIndexOf("@"),scanValue.length());
        }
        return serialNum;
    }

}
