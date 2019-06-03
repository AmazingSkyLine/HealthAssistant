package com.jktr.healthassistant.clock;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jktr.healthassistant.R;
import com.jktr.healthassistant.utils.NotificationChannelUtil;

public class MusicService extends Service {
    private MediaPlayer mp;
    private static final String TAG = "MusicService";
    private int notifyId = 11;

    @Override
    public void onCreate() {
        super.onCreate();

        // 此处应该不需要创建渠道
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, NotificationChannelUtil.DEFAULT_CHANNEL);
        startForeground(notifyId, notification.build()); //这个id不要和应用内的其他id一样

        mp = MediaPlayer.create(this, R.raw.clock);
        mp.start();
        Log.d(TAG, "MusicService started");

        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            if (mp.isPlaying())
                mp.stop();
            mp.release();
        }
        Log.d(TAG, "MusicService stopped");
    }
}
