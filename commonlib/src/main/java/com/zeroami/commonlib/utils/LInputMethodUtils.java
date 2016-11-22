package com.zeroami.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zeroami.commonlib.CommonLib;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：输入法工具类</p>
 */
public class LInputMethodUtils {

    private LInputMethodUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) CommonLib.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 点击空白隐藏输入法
     * @param activity
     */
    public static void hideInputMethodOnTouchSpace(final Activity activity){
        hideInputMethodOnTouchSpace(activity,null);
    }
    /**
     * 点击空白隐藏输入法
     * @param activity
     * @param onTouchListener
     */
    public static void hideInputMethodOnTouchSpace(final Activity activity, final View.OnTouchListener onTouchListener){
        activity.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (activity.getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
                }else {
                    imm.hideSoftInputFromWindow((activity.findViewById(android.R.id.content)).getWindowToken(),0);
                }
                if (onTouchListener != null){
                    return onTouchListener.onTouch(v,event);
                }
                return false;
            }
        });
    }
}
