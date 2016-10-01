package com.zeroami.commonlib.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：轮询服务工具类</p>
 */
public final class LPollingUtils {


    private LPollingUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断是否存在轮询服务
     *
     * @param context 上下文
     * @param cls     Class
     * @return 是否存在
     */
    public static boolean isPollingServiceExist(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    /**
     * 开启轮询服务
     *
     * @param context  上下文
     * @param interval 时间间隔，单位为秒
     * @param cls      Class
     */
    public static void startPollingService(Context context, int interval,
                                           Class<?> cls) {
        startPollingService(context, interval, cls, null, true);
    }

    /**
     * 开启轮询服务
     *
     * @param context  上下文
     * @param interval 时间间隔，单位为秒
     * @param cls      Class
     * @param action   Action
     * @param isWakeup 是否唤醒
     */
    public static void startPollingService(Context context, int interval,
                                           Class<?> cls, String action, boolean isWakeup) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = System.currentTimeMillis();
        int type = isWakeup ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC;
        manager.setRepeating(type, triggerAtTime,
                interval * 1000, pendingIntent);
    }

    /**
     * 停止轮询服务
     *
     * @param context 上下文
     * @param cls     Class
     */
    public static void stopPollingService(Context context, Class<?> cls) {
        stopPollingService(context, cls, null);
    }

    /**
     * 停止轮询服务
     *
     * @param context 上下文
     * @param cls     Class
     * @param action  Action
     */
    public static void stopPollingService(Context context, Class<?> cls,
                                          String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }

}