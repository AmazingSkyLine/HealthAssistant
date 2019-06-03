package com.jktr.healthassistant.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.jktr.healthassistant.SettingActivity;

public class AfterNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "AfterNotifyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received");
        if(intent.getAction() != null && intent.getAction().equals("afterNotification")) {
            int tag = intent.getIntExtra("tag", -1);
            Log.d(TAG, "tag is " + tag);
            switch(tag) {
                case SettingActivity.DAY:
                    SharedPreferences pref = context.getSharedPreferences(SettingActivity.TAG, Context.MODE_PRIVATE);
                    if(pref.getBoolean("clock_switch", false)) {
                        Intent playIntent = new Intent(context, MusicService.class);
                        context.getApplicationContext().stopService(playIntent);
                    }
                    break;
                case SettingActivity.NIGHT:
                    break;
                case SettingActivity.NIGHT_EXPIRE:
                    break;
                default:
                    break;
            }
        }
    }
}
