package com.zeroami.commonlib;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：CommonLib启动配置类</p>
 */
public class CommonLib {
    private static Context sContext;
    public static final String TAG = "CommonLib";
    public static boolean sIsDebug;

    /**
     * 初始化Context，使CommonLib正常工作
     *
     * @param context
     */
    public static void initialize(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new RuntimeException("请自定义Application并继承LBaseApplication或者调用CommonLib.initialize(context)确保CommonLib正常工作");
        }
        return sContext;
    }

    /**
     * 日志开关
     *
     * @param isEnableLog
     */
    public static void setEnableLog(boolean isEnableLog) {
        setEnableLog(isEnableLog, TAG);
    }

    /**
     * 日志开关
     *
     * @param isEnableLog
     * @param tag
     */
    public static void setEnableLog(boolean isEnableLog, String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        if (isEnableLog) {
            Logger.init(TAG).logLevel(LogLevel.FULL).methodCount(1).methodOffset(1);
        } else {
            Logger.init(TAG).logLevel(LogLevel.NONE).methodCount(1).methodOffset(1);
        }
    }
}
