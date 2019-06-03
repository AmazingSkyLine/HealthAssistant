package com.jktr.healthassistant.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.jktr.healthassistant.MyFrontGroundService;
import com.jktr.healthassistant.SettingActivity;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "BOOT_COMPLETED received");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MyFrontGroundService.class));
            } else {
                context.startService(new Intent(context, MyFrontGroundService.class));
            }

            Alarm.resetAlarm(context, SettingActivity.DAY);
            Alarm.resetAlarm(context, SettingActivity.NIGHT);
            Alarm.resetAlarm(context, SettingActivity.NIGHT_EXPIRE);
        }
    }
}
