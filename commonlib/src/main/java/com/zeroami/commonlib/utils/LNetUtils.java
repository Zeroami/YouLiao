package com.zeroami.commonlib.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zeroami.commonlib.CommonLib;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：网络工具类，获取网络连接状态</p>
 */
public class LNetUtils {

    private static volatile LNetUtils sInstance;
    private LNetworkBroadcastReceiver mReceiver;
    private IntentFilter mFilter;
    private LOnNetworkStateListener mListener;

    private LNetUtils() {
        mReceiver = new LNetworkBroadcastReceiver();
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    public static LNetUtils getInstance() {
        if (sInstance == null) {
            synchronized (LNetUtils.class) {
                if (sInstance == null) {
                    sInstance = new LNetUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 判断是否有网络连接
     *
     * @return 是否连接
     */
    public static boolean isNetworkConnected() {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) CommonLib.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空
        if (networkInfo != null)
            return networkInfo.isAvailable() && networkInfo.isConnected();
        return false;
    }


    /**
     * 判断WIFI连接是否可用
     *
     * @return 是否可用
     */
    public static boolean isWifiConnected() {
        // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) CommonLib.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空 并且类型是否为WIFI
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return networkInfo.isAvailable() && networkInfo.isConnected();
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @return 是否可用
     */
    public static boolean isMobileConnected() {
        //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) CommonLib.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            return networkInfo.isAvailable() && networkInfo.isConnected();
        return false;
    }


    /**
     * 注册广播接受者
     *
     * @return 返回本身，链式调用
     */
    public LNetUtils registerBrocastReceiver() {
        CommonLib.getContext().registerReceiver(mReceiver, mFilter);
        return this;
    }

    /**
     * 反注册广播接受者
     *
     * @return 返回本身，链式调用
     */
    public LNetUtils unregisterBrocastReceiver() {
        CommonLib.getContext().unregisterReceiver(mReceiver);
        return this;
    }

    /**
     * 注册监听器
     *
     * @param listener
     */
    public void registerListener(LOnNetworkStateListener listener) {
        mListener = listener;
    }

    /**
     * 反注册监听器，释放对象
     */
    public void unregisterListener() {
        mListener = null;
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：网络状态广播接收者</p>
     */
    private class LNetworkBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener != null) {
                if (LNetUtils.isNetworkConnected()) {
                    mListener.onConnected();
                } else {
                    mListener.onDisConnected();
                }
            }
        }
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：网络状态改变回调接口</p>
     */
    public interface LOnNetworkStateListener {
        void onConnected();

        void onDisConnected();
    }
}
