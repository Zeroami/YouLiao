package com.zeroami.commonlib.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.zeroami.commonlib.CommonLib;
import com.zeroami.commonlib.bean.LAppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：App工具类，获取App相关信息</p>
 */
public class LAppUtils {

    private LAppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVerionCode() {
        int versionCode = -1;
        try {
            String packageName = CommonLib.getContext().getPackageName();
            versionCode = CommonLib.getContext().getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本字符串
     *
     * @return
     */
    public static String getVerionName() {
        String versionName = "";
        try {
            String packageName = CommonLib.getContext().getPackageName();
            versionName = CommonLib.getContext().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 安装Apk
     *
     * @param file
     */
    public static void installApk( File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        CommonLib.getContext().startActivity(intent);
    }

    /**
     * 安装Apk
     *
     * @param uri
     */
    public static void installApk( Uri uri) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        CommonLib.getContext().startActivity(intent);
    }

    /**
     * 卸载apk
     *
     * @param packageName
     */
    public static void uninstallApk( String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        CommonLib.getContext().startActivity(intent);
    }

    /**
     * 检测服务是否运行
     *
     * @param className 完整类名
     * @return
     */
    public static boolean isServiceRunning( String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) CommonLib.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 判断应用是否处于后台状态
     * @return
     */
    public static boolean isBackground() {
        ActivityManager am = (ActivityManager) CommonLib.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(CommonLib.getContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取系统所有APP应用
     *
     * @return
     */
    public static ArrayList<LAppInfo> getAllApp() {
        PackageManager manager = CommonLib.getContext().getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        ArrayList<LAppInfo> appList = new ArrayList<LAppInfo>();
        for (ResolveInfo info : apps) {
            LAppInfo appInfo = new LAppInfo();
            appInfo.setLabel(info.loadLabel(manager) + "");
            appInfo.setIcon(info.loadIcon(manager));
            appInfo.setPackageName(info.activityInfo.packageName);
            appInfo.setClassName(info.activityInfo.name);
            appList.add(appInfo);
        }

        return appList;
    }

    /**
     * 获取用户安装的APP应用
     *
     * @return
     */
    public static ArrayList<LAppInfo> getUserApp() {
        PackageManager manager = CommonLib.getContext().getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        ArrayList<LAppInfo> appList = new ArrayList<LAppInfo>();
        for (ResolveInfo info : apps) {
            ApplicationInfo ainfo = info.activityInfo.applicationInfo;
            if ((ainfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                LAppInfo appInfo = new LAppInfo();
                appInfo.setLabel(info.loadLabel(manager) + "");
                appInfo.setIcon(info.loadIcon(manager));
                appInfo.setPackageName(info.activityInfo.packageName);
                appInfo.setClassName(info.activityInfo.name);
                appList.add(appInfo);
            }
        }

        return appList;
    }

    /**
     * 根据包名和Activity启动类查询应用信息
     *
     * @param packageName
     * @param className
     * @return
     */
    public static LAppInfo getAppByPkgCls(String packageName, String className) {
        LAppInfo appInfo = new LAppInfo();

        PackageManager pm = CommonLib.getContext().getPackageManager();
        Drawable icon;
        CharSequence label = "";
        ComponentName comp = new ComponentName(packageName, className);
        try {
            ActivityInfo info = pm.getActivityInfo(comp, 0);
            icon = pm.getApplicationIcon(info.applicationInfo);
            label = pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0));
        } catch (PackageManager.NameNotFoundException e) {
            icon = pm.getDefaultActivityIcon();
        }
        appInfo.setLabel(label + "");
        appInfo.setIcon(icon);
        appInfo.setPackageName(packageName);
        appInfo.setClassName(className);

        return appInfo;
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }


}
