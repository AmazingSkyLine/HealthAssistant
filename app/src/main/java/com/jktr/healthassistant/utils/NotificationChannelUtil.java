package com.jktr.healthassistant.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.RequiresApi;

public class NotificationChannelUtil {

    private static NotificationChannelUtil instance = null;

    public static final String DEFAULT_CHANNEL = "channel_01";

    private String id;
    private CharSequence name;
    private String description;
    private int importance;
    private boolean enableLights;
    private boolean enableVibration;

    @RequiresApi(api = 26)
    private NotificationChannelUtil() {

        id = DEFAULT_CHANNEL;
        name = "闹钟提醒";
        description = "闹钟提醒通道";
        importance = NotificationManager.IMPORTANCE_HIGH;
        enableLights = true;
        enableVibration = true;
    }

    @RequiresApi(api = 26)
    public static NotificationChannelUtil getInstance() {
        if(instance == null)
            instance = new NotificationChannelUtil();
        return instance;
    }

    @RequiresApi(api = 26)
    public void createChannel(Context context) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 通知渠道组的id
        // String group = "group_id";
        // 用户可见的通知渠道组名称.
        // CharSequence group_name = "生活健康小助手";
        // 创建通知渠道组
        // manager.createNotificationChannelGroup(new NotificationChannelGroup(group, group_name));

        // 渠道id
        // String id = "channel_01";
        // 用户可以看到的通知渠道的名字
        // CharSequence name = "闹钟";
        // 用户可以看到的通知渠道的描述
        // String description = "闹钟提醒通道";
        // int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        // 配置通知渠道的属性
        channel.setDescription(description);
        // 设置通知出现时的闪灯（如果android设备支持的话）
        if(enableLights) {
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
        }
        // 设置通知出现时的震动（如果androd设备支持的话）
        if(enableVibration) {
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
        }
        // 绑定通知渠道组
        // channel.setGroup(group);
        // 在NotificationManager中创建该通知渠道
        manager.createNotificationChannel(channel);
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setEnableLights(boolean enableLights) {
        this.enableLights = enableLights;
    }

    public void setEnableVibration(boolean enableVibration) {
        this.enableVibration = enableVibration;
    }

    public String getId() {
        return id;
    }
}
