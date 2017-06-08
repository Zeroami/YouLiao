package com.zeroami.commonlib.mvp;

import com.zeroami.commonlib.utils.LL;

import rx.Subscriber;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：MP回调处理者</p>
 */
public class LMPCallbackHandler<T> extends Subscriber<T> {
    private LMPCallback<T> mCallback;

    public LMPCallbackHandler(LMPCallback<T> cb){
        this.mCallback = cb;
    }

    @Override
    public void onStart() {
        mCallback.onStart();
    }

    @Override
    public void onCompleted() {
        mCallback.onFinish();
    }

    @Override
    public void onError(Throwable e) {
        LL.e(e);
        if (e instanceof LApiException) {
            mCallback.onFailure(((LApiException) e).getCode(), e);
        } else {
            mCallback.onFailure(-1, e);
        }
        mCallback.onFinish();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public void onSuccess(T t){
        mCallback.onSuccess(t);
    }
}