package com.jimi.smt.eps_appclient.socket;

import android.os.AsyncTask;

import com.jimi.smt.eps_appclient.Func.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 类名:SocketClient
 * 创建人:Liang GuoChang
 * 创建时间:2018/3/13 19:40
 * 描述:
 * 版本号:
 * 修改记录:
 */

public class SocketClient extends AsyncTask<String, Void, Boolean> {

    private final static String TAG = "SocketClient";

    @Override
    protected Boolean doInBackground(String... strings) {
        String ip = strings[0];
        int port = Integer.valueOf(strings[1]);
        int msg = Integer.valueOf(strings[2]);
        return startClient(ip, port, msg);
    }

    private Boolean startClient(String ip, int port, int msg) {
        OutputStream outputStream = null;
        Socket socket = null;
        int result;
        try {
            socket = new Socket(ip, port);
            outputStream = socket.getOutputStream();
            outputStream.write(msg);
            Log.d(TAG, "msg--" + msg);
            result = socket.getInputStream().read();
            Log.d(TAG, "result--" + result);

        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (result == 0) {
            return false;
        } else if (result == 1) {
            return true;
        }
        return false;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            Log.d(TAG, "成功");
        } else {
            Log.d(TAG, "失败");
        }
    }

}
