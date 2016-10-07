package com.zeroami.commonlib.utils;


import android.graphics.drawable.Drawable;

import com.zeroami.commonlib.CommonLib;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：R资源工具类</p>
 */
public class LRUtils {
    public static final String POINT = ".";
    public static final String R = "R";
    public static final String JOIN = "$";
    public static final String ANIM = "anim";
    public static final String ATTR = "attr";
    public static final String COLOR = "color";
    public static final String DIMEN = "dimen";
    public static final String DRAWABLE = "drawable";
    public static final String ID = "id";
    public static final String LAYOUT = "layout";
    public static final String MENU = "menu";
    public static final String RAW = "raw";
    public static final String STRING = "string";
    public static final String STYLE = "style";
    public static final String STYLEABLE = "styleable";

    private LRUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取资源Id内容
     * @param resId
     * @return
     */
    public static String getString(int resId){
        return CommonLib.getContext().getResources().getString(resId);
    }

    /**
     * 获取资源Id内容
     * @param resId
     * @return
     */
    public static int getColor(int resId){
        return CommonLib.getContext().getResources().getColor(resId);
    }

    /**
     * 获取资源Id内容
     * @param resId
     * @return
     */
    public static float getDimension(int resId){
        return CommonLib.getContext().getResources().getDimension(resId);
    }

    /**
     * 获取资源Id内容
     * @param resId
     * @return
     */
    public static int getDimensionPixelSize(int resId){
        return CommonLib.getContext().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取资源Id内容
     * @param resId
     * @return
     */
    public static Drawable getDrawable(int resId){
        return CommonLib.getContext().getResources().getDrawable(resId);
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getAnimId(String name) {
        try {
            return (Integer) Class
                    .forName(CommonLib.getContext().getPackageName() + POINT + R + JOIN + ANIM)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getAttrId(String name) {
        try {
            return (Integer) Class
                    .forName(CommonLib.getContext().getPackageName() + POINT + R + JOIN + ATTR)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getColorId(String name) {
        try {
            return (Integer) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN + COLOR)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getDimenId(String name) {
        try {
            return (Integer) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN + DIMEN)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getDrawableId(String name) {
        try {
            return (Integer) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN
                                    + DRAWABLE).getDeclaredField(name)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getId(String name) {
        try {
            return (Integer) Class
                    .forName(CommonLib.getContext().getPackageName() + POINT + R + JOIN + ID)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getLayoutId(String name) {
        try {
            return (Integer) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN
                                    + LAYOUT).getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getMenuId(String name) {
        try {
            return (Integer) Class
                    .forName(CommonLib.getContext().getPackageName() + POINT + R + JOIN + MENU)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getRawId(String name) {
        try {
            return (Integer) Class
                    .forName(CommonLib.getContext().getPackageName() + POINT + R + JOIN + RAW)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getStringId(String name) {
        try {
            return (Integer) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN
                                    + STRING).getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int getStyleId(String name) {
        try {
            return (Integer) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN + STYLE)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据字符串获取Id
     * @param name
     * @return
     */
    public static int[] getStyleable(String name) {
        try {
            return (int[]) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN
                                    + STYLEABLE).getDeclaredField(name)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据字符串获取Id
     * @param styleableName
     * @param attributeName
     * @return
     */
    public static int getStyleableAttribute(
            String styleableName, String attributeName) {
        try {
            return (Integer) Class
                    .forName(
                            CommonLib.getContext().getPackageName() + POINT + R + JOIN
                                    + STYLEABLE)
                    .getDeclaredField(styleableName + "_" + attributeName)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
