package com.zeroami.commonlib.mvp;

import rx.Subscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Rx支持相关功能接口</p>
 */
public interface LRxSupport {

    /**
     * 添加一个订阅
     * @param subscription
     */
    void addSubscription(Subscription subscription);
}
