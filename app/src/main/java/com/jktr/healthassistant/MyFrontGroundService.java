package com.jktr.healthassistant;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jktr.healthassistant.utils.NotificationChannelUtil;

public class MyFrontGroundService extends Service {

    private static final String TAG = "MyFrontGroundService";
    private int notifyId = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationChannelUtil.getInstance().createChannel(this);

        if(Build.VERSION.SDK_INT >= 18) {
            String appName = getString(R.string.app_name);
            String frontTip = getString(R.string.front_tip);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannelUtil.DEFAULT_CHANNEL);
            builder.setContentTitle(appName).setContentText(frontTip).setSmallIcon(R.mipmap.ic_launcher);

            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_ONGOING_EVENT;

            startForeground(notifyId, notification); //这个id不要和应用内的其他id一样
        } else startForeground(notifyId, new Notification());
        Log.d(TAG, "MyFrontGroundService started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 服务被杀后，系统自动重新创建服务
        return START_STICKY;
    }

    

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MyFrontGroundService stopped");

        if(Build.VERSION.SDK_INT >= 18) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(notifyId);
        }
        Intent intent = new Intent(getApplicationContext(), MyFrontGroundService.class);
        startService(intent);
    }
}
