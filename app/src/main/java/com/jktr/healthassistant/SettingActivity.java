package com.jktr.healthassistant;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jktr.healthassistant.clock.Alarm;
import com.jktr.healthassistant.utils.WakeLockUtil;

import java.util.Calendar;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private TextView dayInfo;
    private TextView nightInfo;
    private String dayInfoText;
    private String nightInfoText;

    public static final String TAG = "SettingActivity";

    public static final int DAY = 0;
    public static final int NIGHT = 1;
    public static final int NIGHT_EXPIRE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        pref = getPreferences(MODE_PRIVATE);

        Button dayButton = findViewById(R.id.day_button);
        Button nightButton = findViewById(R.id.night_button);

        dayInfo = findViewById(R.id.day_info);
        nightInfo = findViewById(R.id.night_info);

        Switch clock_switch = findViewById(R.id.clock_switch);

        dayInfoText = pref.getString("day_info", "无");
        nightInfoText = pref.getString("night_info", "无");
        dayInfo.setText(dayInfoText);
        nightInfo.setText(nightInfoText);

        boolean is_clock_enabled = pref.getBoolean("clock_switch", false);
        if(is_clock_enabled) {
            clock_switch.setChecked(true);
        }

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dayInfoText = String.format(Locale.CHINA, "%2d:%2d", hourOfDay, minute);
                        dayInfo.setText(dayInfoText);
                        setPref("day_info", dayInfoText);
                        Log.d(TAG, "onTimeSet: " + hourOfDay + ":" + minute);

                        // Alarm.cancelAlarm(SettingActivity.this, DAY);  // 可能不需要
                        Alarm.setAlarm(SettingActivity.this, hourOfDay, minute, DAY);
                        Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        nightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        nightInfoText = String.format(Locale.CHINA, "%2d:%2d", hourOfDay, minute);
                        nightInfo.setText(nightInfoText);
                        setPref("night_info", nightInfoText);
                        Log.d(TAG, "onTimeSet: " + hourOfDay + ":" + minute);

                        // Alarm.cancelAlarm(SettingActivity.this, NIGHT);  // 可能不需要
                        Alarm.setAlarm(SettingActivity.this, (hourOfDay + 23) % 24, minute, NIGHT);
                        Alarm.setAlarm(SettingActivity.this, hourOfDay, minute, NIGHT_EXPIRE);
                        Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        clock_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setPref("clock_switch", true);
                } else {
                    setPref("clock_switch", false);
                }
                Log.d(TAG, "onCheckedChanged: " + isChecked);
            }
        });
    }

    private void setPref(String key, String value) {
        editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void setPref(String key, boolean value) {
        editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    protected void onResume() {
        WakeLockUtil.acquireWakeLock(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        WakeLockUtil.releaseWakeLock();
        super.onPause();
    }
}
