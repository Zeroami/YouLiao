package com.zeroami.commonlib.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Activity工具类</p>
 */
public class LActivityUtils {

	private LActivityUtils(){
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static List<Activity> mActivityList = new ArrayList<Activity>();

	/**
	 * 将Activity加入Activity列表
	 * @param activity
	 */
	public static void addActivity(Activity activity){
		mActivityList.add(activity);
	}

	/**
	 * 将Activity移出Activity列表
	 * @param activity
	 */
	public static void removeActivity(Activity activity){
		mActivityList.remove(activity);
	}

	/**
	 * 退出所有Activity
	 */
	public static void finishAllActivity(){
		for (Activity activity : mActivityList) {
			if (activity != null && !activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
