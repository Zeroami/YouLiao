package com.zeroami.commonlib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.zeroami.commonlib.CommonLib;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：系统显示相关工具类</p>
 */
public class LDisplayUtils {

    private LDisplayUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        final float scale = CommonLib.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = CommonLib.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        float fontScale = CommonLib.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        float fontScale = CommonLib.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) CommonLib.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        return w;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) CommonLib.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int h = dm.heightPixels;
        return h;
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight() {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int heightId = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = CommonLib.getContext().getResources().getDimensionPixelSize(heightId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取View的测量宽高
     *
     * @param view
     * @param callback
     */
    public static void getViewMeasureSize(final View view, final LMeasureCallback callback) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (callback != null) {
                    callback.call(view.getMeasuredWidth(), view.getMeasuredHeight());
                }
            }
        });
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：获取View测量宽高回调</p>
     */
    public interface LMeasureCallback {
        void call(int width, int height);
    }
}
