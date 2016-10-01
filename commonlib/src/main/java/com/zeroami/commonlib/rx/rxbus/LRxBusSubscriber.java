package com.zeroami.commonlib.rx.rxbus;


import rx.Subscriber;
/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：为RxBus使用的Subscriber, 主要提供next事件的try,catch</p>
 */
public abstract class LRxBusSubscriber<T> extends Subscriber<T> {

    @Override
    public void onNext(T t) {
        try {
            call(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    protected abstract void call(T t);
}