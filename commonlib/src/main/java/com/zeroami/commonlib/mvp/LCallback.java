package com.zeroami.commonlib.mvp;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：数据回调接口</p>
 */
public interface LCallback<T> {
    void onSuccess(T data);
    void onError(Throwable e);
}
