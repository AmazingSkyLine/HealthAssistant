package com.jktr.healthassistant;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jktr.healthassistant.utils.WakeLockUtil;

public class MainActivity extends AppCompatActivity {

    private PowerManager.WakeLock wakeLock = null;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, MyFrontGroundService.class));
        } else {
            startService(new Intent(this, MyFrontGroundService.class));
        }

        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.clock_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
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
