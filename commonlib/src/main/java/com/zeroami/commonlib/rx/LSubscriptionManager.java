package com.zeroami.commonlib.rx;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：RxJava订阅管理器</p>
 */
public class LSubscriptionManager {

    private CompositeSubscription mCompositeSubscription;   // 管理RxJava的所有订阅

    /**
     * 添加一个订阅
     *
     * @param subscription
     */
    public void addSubscription(Subscription subscription) {
        if (subscription != null) {
            if (mCompositeSubscription == null) {
                synchronized (this) {
                    if (mCompositeSubscription == null) {
                        mCompositeSubscription = new CompositeSubscription();
                    }
                }
            }
            mCompositeSubscription.add(subscription);
        }
    }

    /**
     * 取消所有订阅
     */
    public void unsubscribeAllSubscription() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
