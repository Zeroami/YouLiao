package com.zeroami.commonlib.mvp;

import rx.Subscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseModel，实现MvpModel，完成Model的通用操作</p>
 */
public class LBaseModel<P extends LMvpPresenter> implements LMvpModel{
    private P mMvpPresenter;

    public LBaseModel(P presenter){
        mMvpPresenter = presenter;
    }

    /**
     * 添加一个订阅
     * @param subscription
     */
    protected void addSubscription(Subscription subscription) {
        if (mMvpPresenter instanceof LRxSupport){
            ((LRxSupport)mMvpPresenter).addSubscription(subscription);
        }
    }
}
