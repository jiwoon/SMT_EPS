package com.jimi.smt.eps_appclient.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.jimi.smt.eps_appclient.Func.Log;
import com.jimi.smt.eps_appclient.Unit.GlobalData;
import com.jimi.smt.eps_appclient.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

public class UpdateAppService extends Service {
    private final String TAG = "UpdateAppService";

    //下载链接
    private String mDownloadUrl;
    //apk名称
    private String newApkName;
    private NotificationManager notificationManager;
    private Notification notification;
    //全局变量
    private GlobalData globalData;
    //广播intent
    private Intent broadcastIntent = new Intent("com.jimi.smt.eps_appclient.UPDATE");

    @Override
    public void onCreate() {
        super.onCreate();
        globalData = (GlobalData) getApplication();
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            notifyMsg("温馨提示", "文件下载失败", 0);
            stopSelf();
        } else {
            mDownloadUrl = intent.getStringExtra("apkUrl");//获取下载的链接
            newApkName = intent.getStringExtra("apkName");//apk名字
            if ((mDownloadUrl != null) && (newApkName != null)) {
                downloadFile(mDownloadUrl);
                Log.d(TAG, "mDownloadUrl-" + mDownloadUrl + "\n" + "newApkName-" + newApkName);
            } else {
                stopSelf();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void notifyMsg(String title, String msg, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.icon_download);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_download));
        builder.setContentTitle(title);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(msg);
        //下载进行中
        if ((progress > 0) && (progress < 100)) {
            builder.setProgress(100, progress, false);
        }/*else{
            builder.setProgress(0,0,false);
        }*/
        //下载完成
        if (progress >= 100) {
//            builder.setContentIntent(getInstallIntent());
            installApk();
        }
        notification = builder.build();
        notificationManager.notify(0, notification);
    }

    //安装apk
    private PendingIntent getInstallIntent() {
        File file = new File(globalData.getApkDownloadDir() + "/" + newApkName);
        Log.d(TAG, "file-" + file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
//        intent.setDataAndType(Uri.parse("file://"+file.getAbsolutePath()),"application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    //安装apk
    private void installApk() {
        File file = new File(globalData.getApkDownloadDir() + "/" + newApkName);
        Log.d(TAG, "file-" + file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    //下载文件
    private void downloadFile(String url) {
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(globalData.getApkDownloadDir(), newApkName) {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "Exception::" + e.toString());
                notifyMsg("温馨提示", "下载失败,请退出App在进来", 0);
                stopSelf();
            }

            @Override
            public void onResponse(File response, int id) {
                //下载完成
                notifyMsg("温馨提示", "下载完成", 100);
                stopSelf();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                //progress * 100 为当前下载进度,total为文件大小
                if ((int) (progress * 100) % 10 == 0) {
                    broadcastIntent.putExtra("progress", (int) (progress * 100));
                    sendBroadcast(broadcastIntent);
                    //避免频繁刷新View，这里设置每下载10%提醒更新一次进度
                    notifyMsg("温馨提醒", "文件正在下载..", (int) (progress * 100));
                }
            }
        });
    }

}
