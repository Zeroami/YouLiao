package com.zeroami.commonlib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.internal.$Gson$Types;
import com.zeroami.commonlib.CommonLib;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：通用工具类</p>
 */
public class LUtils {

    /**
     * 根据子类的字节码获取泛型参数类型，如 new ArrayList<String>(){}.getClass();
     * @param subclass 子类的字节码
     * @return 泛型参数类型
     */
    public static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    /**
     * 复制文本到剪贴板
     * @param text
     */
    public static void copyToClipboard(String text){
        if(Build.VERSION.SDK_INT >= 11){
            ClipboardManager cbm = (ClipboardManager) CommonLib.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cbm.setPrimaryClip(ClipData.newPlainText(CommonLib.getContext().getPackageName(), text));
        }else {
            android.text.ClipboardManager cbm = (android.text.ClipboardManager) CommonLib.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cbm.setText(text);
        }
    }

    /**
     * 关闭输入法
     * @param view
     */
    public static void closeInputMethod(View view){
        InputMethodManager imm = (InputMethodManager) CommonLib.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
