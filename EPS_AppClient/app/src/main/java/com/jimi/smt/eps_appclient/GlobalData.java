package com.jimi.smt.eps_appclient;

import android.app.Application;
import android.content.Context;

import com.jimi.smt.eps_appclient.Unit.MaterialItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名:GlobalData
 * 创建人:Connie
 * 创建时间:2017-9-25
 * 描述:全局变量
 * 版本号:V1.0
 * 修改记录:
 */
public class GlobalData extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalData.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GlobalData.context;
    }

    //料号表
    private List<MaterialItem> materialItems = new ArrayList<MaterialItem>();

    public List<MaterialItem> getMaterialItems() {
        return materialItems;
    }

    public void setMaterialItems(List<MaterialItem> materialItems) {
        this.materialItems = materialItems;
    }

    //操作员
    private String Operator = "";

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    //操作员类型(0:仓库操作员;1:厂线操作员;2:IPQC;3:管理员)
    private int UserType;

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    //操作类型
    private int OperType;

    public int getOperType() {
        return OperType;
    }

    public void setOperType(int operType) {
        OperType = operType;
    }

    //更新显示日志类型
    private int updateType;

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    //操作线号
    private String line;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    //工单号
    private String work_order;

    public String getWork_order() {
        return work_order;
    }

    public void setWork_order(String work_order) {
        this.work_order = work_order;
    }

    //板面类型
    private int board_type;

    public int getBoard_type() {
        return board_type;
    }

    public void setBoard_type(int board_type) {
        this.board_type = board_type;
    }


    //apk下载路径
    private String apkDownloadDir;

    public String getApkDownloadDir() {
        return apkDownloadDir;
    }

    public void setApkDownloadDir(String apkDownloadDir) {
        this.apkDownloadDir = apkDownloadDir;
    }
}
