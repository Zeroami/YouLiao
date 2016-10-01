package com.zeroami.commonlib.utils;

import java.lang.reflect.Field;

import android.content.DialogInterface;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：对话框工具类</p>
 */
public class LDialogUtils {

	private LDialogUtils(){
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 设置对话框是否点击消失
	 * @param dialog
	 * @param dismissible
	 */
	public static void setDialogDismissible(DialogInterface dialog, boolean dismissible) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, dismissible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
