package com.zeroami.commonlib.common;

import android.app.Application;
import android.content.Context;

import com.zeroami.commonlib.CommonLib;


/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseApplication</p>
 */
public abstract class LBaseApplication extends Application {

	private static Context sApplicationContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sApplicationContext = getApplicationContext();
		CommonLib.initialize(sApplicationContext);
		CommonLib.sIsDebug = isDebug();
		CommonLib.setEnableLog(CommonLib.sIsDebug);
	}

	public static Context getContext() {
		return sApplicationContext;
	}

	public abstract boolean isDebug();
}
