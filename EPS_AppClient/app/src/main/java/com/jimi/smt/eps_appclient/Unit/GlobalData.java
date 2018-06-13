package com.jimi.smt.eps_appclient.Unit;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.jimi.smt.eps_appclient.Dao.GreenDaoManager;

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
    private static final String TAG = "GlobalData";
    @SuppressLint("StaticFieldLeak")
    protected static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //greenDao全局配置,只希望有一个数据库操作对象
        GreenDaoManager.getInstance();
    }

    public static Context getAppContext() {
        return context;
    }

//    private final String appCacheDir = getCacheDir().getAbsolutePath();

//    public String getAppCacheDir() {
//        return appCacheDir;
//    }

    //料号表
    private List<MaterialItem> materialItems = new ArrayList<MaterialItem>();

    public List<MaterialItem> getMaterialItems() {
        return materialItems;
    }

    public void setMaterialItems(List<MaterialItem> materialItems) {
        this.materialItems = materialItems;
    }

    //program_item_visit表
    private List<ProgramItemVisit> programItemVisits = new ArrayList<ProgramItemVisit>();

    public List<ProgramItemVisit> getProgramItemVisits() {
        return programItemVisits;
    }

    public void setProgramItemVisits(List<ProgramItemVisit> programItemVisits) {
        this.programItemVisits = programItemVisits;
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

    //管理员所做的操作类型
    private int adminOperType;

    public int getAdminOperType() {
        return adminOperType;
    }

    public void setAdminOperType(int adminOperType) {
        this.adminOperType = adminOperType;
    }

    //更新显示日志类型
    private int updateType;

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    //操作线号 301 - 308
    private String line;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    //线号的负值 -127 - -120
    private int minusLine;

    public int getMinusLine() {
        return minusLine;
    }

    public void setMinusLine(String minusLine) {
        this.minusLine = Integer.valueOf(minusLine.substring(minusLine.length() - 1)) - 128;
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

    //选中的program_id
    /*
    private String program_id;

    public String getProgram_id() {
        Log.d(TAG,"getProgram_id-"+program_id);
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
        Log.d(TAG,"setProgram_id-"+program_id);
    }
    */


    //报警状态 0 正在报警 , 1 未报警 初始值为 1
    private int alarmState;

    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    //更新站位表
    private boolean updateProgram = false;

    public boolean isUpdateProgram() {
        return updateProgram;
    }

    public void setUpdateProgram(boolean updateProgram) {
        this.updateProgram = updateProgram;
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
