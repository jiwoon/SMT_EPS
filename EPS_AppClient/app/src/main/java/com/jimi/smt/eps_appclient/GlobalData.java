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
    String Operator = "";

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }
}
