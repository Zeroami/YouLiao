package com.zeroami.commonlib.mvp;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Presenter调用Model方法的回调接口</p>
 */
public interface LMPCallback<T> {
    void onSuccess(T data);
    void onFailure(int code,Throwable e);
}
