package com.jimi.smt.eps_appclient.Unit;

/**
 * Created by think on 2017/9/27.
 */
public class Constants {

    //操作类型
    public static final int FEEDMATERIAL =0;            //上料
    public static final int CHANGEMATERIAL =1;          //换料
    public static final int CHECKMATERIAL =2;           //检料
    public static final int CHECKALLMATERIAL =3;        //全检
    public static final int STORE_ISSUE = 4;            //发料
    public static final int FIRST_CHECK_ALL = 5;        //首次全检
    //返回首页时返回码
    public static final int ACTIVITY_RESULT = 6;
    //apk地址
    public static final String DOWNLOAD_URL="http://39.108.231.15/SMT_EPS_APK";
    //线号301-308
    public static final String[] lines={"301","302","303","304","305","306","307","308"};
    //报警器域名
    public static final String alarmIp = "http://10.10.11.11";
}
