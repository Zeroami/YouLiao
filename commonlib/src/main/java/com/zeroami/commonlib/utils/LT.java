package com.zeroami.commonlib.utils;


import android.widget.Toast;

import com.zeroami.commonlib.CommonLib;

import java.util.Arrays;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Toast工具类</p>
 */
public class LT {

    private static Toast sToast;

    private LT(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(Object object) {
        show(object, Toast.LENGTH_SHORT);
    }

    public static void show(Object object, int duration) {
        String text = "";
        if (object == null) {
            text = "null";
        } else {
            if (object.getClass().isArray()) {
                text = Arrays.deepToString((Object[]) object);
            } else {
                text = object.toString();
            }
        }
        if (sToast == null) {
            sToast = Toast.makeText(CommonLib.getContext(), text, duration);
        } else {
            sToast.setText(text);
        }
        sToast.show();
    }
}
