package com.jktr.healthassistant.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class Alarm {

    private static final String TAG = "Alarm";
    private static final String TIME_STORAGE = "time";

    public static void setAlarm(Context context, int hour, int minute, int tag) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        setAlarm(context, c.getTimeInMillis(), tag);
    }

    private static void setAlarm(Context context, long timestamp, int tag) {
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        if(timestamp < System.currentTimeMillis())
            timestamp += AlarmManager.INTERVAL_DAY;

        Intent intent = new Intent("com.jktr.healthassistant.clock");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setComponent(new ComponentName("com.jktr.healthassistant",
                "com.jktr.healthassistant.clock.AlarmReceiver"));
        intent.putExtra("tag", tag);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tag, intent, 0);

        if(Build.VERSION.SDK_INT >= 23)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
        else if(Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);

        Log.d(TAG, "Alarm set at " + new Date(timestamp));

        SharedPreferences.Editor editor = context.getSharedPreferences(TIME_STORAGE, Context.MODE_PRIVATE).edit();
        editor.putLong("timestamp" + tag, timestamp);
        editor.apply();
    }

    public static void cancelAlarm(Context context, int tag) {
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent("com.jktr.healthassistant.clock");
        intent.setComponent(new ComponentName("com.jktr.healthassistant",
                "com.jktr.healthassistant.clock.AlarmReceiver"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tag, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    static void resetAlarm(Context context, int tag) {  // modified private

        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_STORAGE, Context.MODE_PRIVATE);
        long timestamp = sharedPreferences.getLong("timestamp" + tag, 0);

        if(timestamp == 0) {
            Log.d(TAG, "timestamp is 0!");
            return;
        }

        setAlarm(context, timestamp, tag);
    }
}
