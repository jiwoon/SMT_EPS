package com.jimi.smt.eps_appclient.Func;

import com.jimi.smt.eps_appclient.GlobalData;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 类名:AlarmUtil
 * 创建人:Liang GuoChang
 * 创建时间:2017/11/21 9:04
 * 描述:
 * 版本号:
 * 修改记录:
 */

public class AlarmUtil {

    private static final String TAG = "AlarmUtil";
    private GlobalData globalData;
    private static final boolean isAlarm = false;//是否报警开关

    public AlarmUtil(GlobalData globalData){
        this.globalData = globalData;
    }
    /**
     * 打开报警,断电响铃;或者关闭报警
     * @param urlStr
     * @param alarmState
     */
    private void onOROffAlarm(final String urlStr, final int alarmState){

        if (isAlarm){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"onOROffAlarm-alarmState-"+alarmState);
                    //设置报警状态
                    switch (alarmState){
                        case 0://报警
                            //当前处于不报警状态
                            Log.d(TAG,"globalData.getAlarmState-"+globalData.getAlarmState());
                            if (globalData.getAlarmState() == 1){
                                globalData.setAlarmState(0);
                                connectAlarmIp(urlStr);
//                            globalData.setAlarmState(0);
                            }
                            break;
                        case 1://不报警
                            //当前处于报警状态
                            if (globalData.getAlarmState() == 0){
                                globalData.setAlarmState(1);
                                connectAlarmIp(urlStr);
//                            globalData.setAlarmState(1);
                            }
                            break;
                    }
                }
            }).start();
        }




        /*
        OkHttpUtils
                .get()
                .url(url)
//                .addParams(param,value)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG,"onError-"+call.toString()+"\nException-"+e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG,"response-"+response);
                    }
                });
        */
    }

    private void connectAlarmIp(String urlStr){
        try {
            Log.d(TAG,"connectAlarmIp-urlStr-"+urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(5000);
//                    connection.connect();
            Log.d(TAG,"connectAlarmIp-"+connection.getResponseCode());
            if (connection.getResponseCode() == 200){
                InputStream inputStream = connection.getInputStream();
                /*
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String str;
                while ((str = reader.readLine()) != null){
                    sb.append(str);
                }
                Log.d(TAG,"onOROffAlarm-"+sb);
                reader.close();
                */

                inputStream.close();
                connection.disconnect();
            }

        }catch (Exception e) {
            Log.d(TAG,"Exception-"+e.toString());
            e.printStackTrace();
        }
    }



    //报警(断电) http://10.10.11.11/15=0
    public void turnOnAlarm(String url,int alarmState){
        Log.d(TAG,"==turnOnAlarm=="+url);
        onOROffAlarm(url+"=0",alarmState);
    }

    //关掉报警(通电) http://10.10.11.11/15=1
    public void turnOffAlarm(String url,int alarmState){
        Log.d(TAG,"==turnOffAlarm=="+url);
        onOROffAlarm(url+"=1",alarmState);
    }
}
