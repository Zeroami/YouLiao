package com.zeroami.commonlib.rx;

import com.zeroami.commonlib.utils.LL;

import rx.Subscriber;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：RxJava订阅者适配器</p>
 */
public abstract class LSubscriberAdapter<T> extends Subscriber<T>{
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        LL.e(e);
    }

    @Override
    public void onNext(T t) {

    }
}
