package com.zeroami.commonlib.common;

import com.zeroami.commonlib.utils.LUtils;

import java.lang.reflect.Type;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：保存泛型参数的Class</p>
 */
public class LTypeOfT<T> {
    private Type type;

    public LTypeOfT(){
        type = LUtils.getSuperclassTypeParameter(getClass());
    }

    public Type getType(){
        return type;
    }
}
