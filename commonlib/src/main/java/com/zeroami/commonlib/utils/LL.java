package com.zeroami.commonlib.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.zeroami.commonlib.CommonLib;

import java.util.Objects;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：日志打印类</p>
 */
public class LL {


    private LL() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object message) {
        Logger.d(message == null ? "null" : message.toString());
    }

    public static void e(String message, Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(Throwable throwable) {
        Logger.e(throwable, null);
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }

    public static void sd(String message){
        if (CommonLib.sIsDebug){
            Log.d(CommonLib.TAG, message);
        }
    }

    public static void sd(Object message){
        if (CommonLib.sIsDebug){
            Log.d(CommonLib.TAG, message == null ? "null" : message.toString());
        }
    }

    public static void se(String message){
        if (CommonLib.sIsDebug){
            Log.e(CommonLib.TAG, message);
        }
    }

    public static void si(String message){
        if (CommonLib.sIsDebug){
            Log.i(CommonLib.TAG, message);
        }
    }

    public static void sv(String message) {
        if (CommonLib.sIsDebug) {
            Log.v(CommonLib.TAG, message);
        }
    }

    public static void sw(String message){
        if (CommonLib.sIsDebug){
            Log.w(CommonLib.TAG, message);
        }
    }
}
