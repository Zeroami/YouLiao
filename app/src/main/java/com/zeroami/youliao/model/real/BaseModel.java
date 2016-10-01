package com.zeroami.youliao.model.real;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.callback.SuccessBeforeCallback;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseModel</p>
 */
public class BaseModel {

    /**
     * 处理回调
     * @param data
     * @param e
     * @param callback
     * @param successBeforeCallback
     * @param <T>
     */
    protected <T> void handleCallback(T data,AVException e,LeanCallback callback,SuccessBeforeCallback<T> successBeforeCallback){
        if (callback != null) {
            if (e == null) {
                if (successBeforeCallback != null){
                    successBeforeCallback.call(data);
                }
                callback.onSuccess(data);
            } else {
                LL.e(e);
                callback.onError(e.getCode(), e);
            }
        }
    }
}
