package com.jktr.healthassistant.clock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jktr.healthassistant.R;
import com.jktr.healthassistant.SettingActivity;

public class AlarmActivity extends AppCompatActivity {

    private static final String TAG = "AlarmActivity";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        int tag = getIntent().getIntExtra("tag", -1);

        switch(tag) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }

        Alarm.resetAlarm(this, SettingActivity.DAY);

        SharedPreferences pref = this.getSharedPreferences(SettingActivity.TAG, Context.MODE_PRIVATE);
        if(!pref.getBoolean("clock_switch", false)) return;

        intent = new Intent(this, MusicService.class);
        stopService(intent);
    }
}
