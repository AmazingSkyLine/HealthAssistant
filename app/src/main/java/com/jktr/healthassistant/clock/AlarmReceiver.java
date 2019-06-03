package com.jktr.healthassistant.clock;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jktr.healthassistant.R;
import com.jktr.healthassistant.SettingActivity;
import com.jktr.healthassistant.utils.NotificationChannelUtil;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    NotificationManager manager;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.jktr.healthassistant.clock")) {
            Log.d(TAG, "Alarm Received");

            String appName = context.getString(R.string.app_name);
            String noticeStr;
            Intent playIntent;

            pref = context.getSharedPreferences(SettingActivity.TAG, Context.MODE_PRIVATE);

            int tag = intent.getIntExtra("tag", -1);
            Log.d(TAG, "tag is " + tag);
            switch(tag) {
                case SettingActivity.DAY:
                    noticeStr = context.getString(R.string.notify_str0);
                    if(pref.getBoolean("clock_switch", false)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.getApplicationContext().startForegroundService(new Intent(context, MusicService.class));
                        } else {
                            context.getApplicationContext().startService(new Intent(context, MusicService.class));
                        }
                    }
                    Alarm.resetAlarm(context, SettingActivity.DAY);
                    break;
                case SettingActivity.NIGHT:
                    setPref("is_night_expired", false);
                    Log.d(TAG, "Night card is enabled");
                    noticeStr = context.getString(R.string.notify_str1);
                    Alarm.resetAlarm(context, SettingActivity.NIGHT);
                    break;
                case SettingActivity.NIGHT_EXPIRE:
                    setPref("is_night_expired", true);
                    Log.d(TAG, "Night card is expired");
                    noticeStr = context.getString(R.string.notify_str2);
                    Alarm.resetAlarm(context, SettingActivity.NIGHT_EXPIRE);
                    break;
                default:
                    return;
            }

            manager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

            // playIntent = new Intent(context, AlarmActivity.class);
            playIntent = new Intent("afterNotification");
            playIntent.setComponent(new ComponentName("com.jktr.healthassistant",
                   "com.jktr.healthassistant.clock.AfterNotificationReceiver"));
            // playIntent = new Intent(context, NotificationHandlerService.class);
            playIntent.putExtra("tag", tag);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tag, playIntent, 0);
            // PendingIntent pendingIntent = PendingIntent.getService(context, tag, playIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannelUtil.DEFAULT_CHANNEL);
            builder.setContentTitle(appName).setContentText(noticeStr).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent).setAutoCancel(true);
            manager.notify(tag + 1, builder.build());
            Log.d(TAG, "收到推送: " + noticeStr);
        }
    }

    private void setPref(String key, boolean value) {
        editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
