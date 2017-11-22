package com.jimi.smt.eps_appclient.Func;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 类名:AlarmTask
 * 创建人:Liang GuoChang
 * 创建时间:2017/11/20 15:32
 * 描述:报警器功能
 * 版本号:
 * 修改记录:
 */

public class AlarmTask extends AsyncTask<String,Integer,String>{
    private Context context;

    public AlarmTask(Context context){
        this.context = context;
    }

    /**
     * 打开报警器
     */
    public void alarmRing(){

    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        return null;
    }



}
