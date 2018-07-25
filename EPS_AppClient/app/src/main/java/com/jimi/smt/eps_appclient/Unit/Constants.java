package com.jimi.smt.eps_appclient.Unit;

/**
 * 类名:Constants
 * 创建人:Liang GuoChang
 * 创建时间:2017/9/27 19:50
 * 描述:
 * 版本号:
 * 修改记录:
 */
public class Constants {

    //    最后一次操作类型：0:上料 1:换料 2:检查 3:全料检查 4:仓库发料
    //操作类型
    public static final int FEEDMATERIAL = 0;            //上料
    public static final int CHANGEMATERIAL = 1;          //换料
    public static final int CHECKMATERIAL = 2;           //检料
    public static final int CHECKALLMATERIAL = 3;        //全检
    public static final int STORE_ISSUE = 4;            //发料
    public static final int FIRST_CHECK_ALL = 5;        //首次全检
    //用户类型
    public static final int WARE_HOUSE = 0;//仓库
    public static final int FACTORY = 1;//厂线
    public static final int QC = 2;//QC
    public static final int ADMIN = 3;//管理员
    public static final int ADMIN_WARE_HOUSE = 30;//管理员操作仓库内容
    public static final int ADMIN_FACTORY = 31;//管理员操作厂线内容
    public static final int ADMIN_QC = 32;//管理员操作IPQC内容

    //返回首页时返回码
    public static final int ACTIVITY_RESULT = 6;
    public static final int ADMIN_ACTIVITY_RESULT = 7;
    //apk地址
    public static final String DOWNLOAD_URL = "http://39.108.231.15/SMT_EPS_APK";
    //线号301-308
    public static final String[] lines = {"301", "302", "303", "304", "305", "306", "307", "308"};
    //开启或关闭工位检测
    public static final boolean isCheckWorkType = true;
    //缓存功能
    public static final boolean isCache = true;
    //上料解锁密码
    public static final String feedPwd = "12348765";
    //实时更新功能
    public static final boolean liveUpdate = true;
    //ip地址（测试）
    public static final String urlBase = "http://10.10.11.153:8080/eps_server";
    //ip地址（生产）
//    public static final String urlBase = "http://192.168.2.9:8080/eps_server";
    //数据库
//    public static String dataBaseUrl = "jdbc:mysql://10.10.11.120:3306/smt_eps";//本地测试
        public static final String dataBaseUrl = "jdbc:mysql://10.10.11.153:3306/smt_eps";//测试
    //    public static final String dataBaseUrl = "jdbc:mysql://192.168.2.9:3306/smt_eps";//工厂用内网
    public static final String user = "root";//用户名
        public static final String password = "123456";//测试密码
//    public static final String password = "newttl!@#$1234";//密码
}
