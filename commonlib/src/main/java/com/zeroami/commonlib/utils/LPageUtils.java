package com.zeroami.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：页面跳转类</p>
 */
public class LPageUtils {

	private LPageUtils(){
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 不带数据的跳转
	 * @param context
	 * @param cls
	 * @param isFinish
	 */
	public static void startActivity(Context context, Class<?> cls, boolean isFinish) {
		startActivity(context, cls, null, isFinish);
	}

	/**
	 * 带数据的跳转
	 * @param context
	 * @param cls
	 * @param bundle
	 * @param isFinish
	 */
	public static void startActivity(Context context, Class<?> cls, Bundle bundle, boolean isFinish) {
		Intent intent = new Intent(context,cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
		if (isFinish) {
			((Activity)context).finish();
		}
	}

	/**
	 * 不带数据，带返回结果的跳转
	 * @param context
	 * @param cls
	 * @param requestCode
	 */
	public static void startActivityForResult(Context context, Class<?> cls, int requestCode) {
		startActivityForResult(context, cls, null, requestCode);
	}

	/**
	 * 带数据，带返回结果的跳转
	 * @param context
	 * @param cls
	 * @param bundle
	 * @param requestCode
	 */
	public static void startActivityForResult(Context context, Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent(context,cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		((Activity)context).startActivityForResult(intent,requestCode);
	}
}
