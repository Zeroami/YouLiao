package com.zeroami.youliao.model.callback;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：LeanCallback成功前回调</p>
 */
public interface SuccessBeforeCallback<T> {
    void call(T data);
}
