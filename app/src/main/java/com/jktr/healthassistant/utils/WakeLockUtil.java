package com.jktr.healthassistant.utils;

import android.content.Context;
import android.os.PowerManager;

public class WakeLockUtil {

    private static PowerManager.WakeLock wakeLock = null;

    public static void acquireWakeLock(Context context)
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "my_app:my_wake_lock_tag");
            if (wakeLock != null)
            {
                wakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    public static void releaseWakeLock()
    {
        if (wakeLock != null)
        {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
