package com.zeroami.youliao.model.callback;

import com.avos.avoscloud.AVException;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：LeanCloud数据回调接口</p>
 */
public interface LeanCallback<T> {
    void onSuccess(T data);
    void onError(int code,AVException e);
}