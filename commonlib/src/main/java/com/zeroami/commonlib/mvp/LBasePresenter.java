package com.zeroami.commonlib.mvp;

import com.zeroami.commonlib.rx.LSubscriptionManager;

import rx.Subscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BasePresenter，实现MvpPresenter，完成Presenter的通用操作</p>
 */
public abstract class LBasePresenter<V extends LMvpView,M extends LMvpModel> implements LMvpPresenter<V>,LRxSupport {

    private V mMvpView;
    private M mMvpModel;
    private M mRealModel;
    private M mTestModel;
    private LSubscriptionManager mSubscriptionManager;

    public LBasePresenter(V view){
        attachView(view);
        mRealModel = getRealModel();
        mTestModel = getTestModel();
        mMvpModel = mRealModel;
        mSubscriptionManager = new LSubscriptionManager();
    }

    /**
     * View初始化完成
     */
    @Override
    public void doViewInitialized() {}

    /**
     * 关联完成调用
     */
    protected void onViewAttched(){}

    /**
     * 获取真实的数据Model
     * @return
     */
    protected abstract M getRealModel();

    /**
     * 获取测试的数据Model
     * @return
     */
    protected abstract M getTestModel();

    /**
     * 关联View
     * @param view
     */
    @Override
    public void attachView(V view) {
        this.mMvpView = view;
        onViewAttched();
    }

    /**
     * 与View解除关联
     */
    @Override
    public void detachView() {
        this.mMvpView = null;
        mSubscriptionManager.unsubscribeAllSubscription();
    }

    /**
     * 切换数据仓库
     * @param isTest
     */
    protected void switchModel(boolean isTest){
        if (isTest){
            mMvpModel = mTestModel;
        }else{
            mMvpModel = mRealModel;
        }
    }

    /**
     * 添加一个订阅
     * @param subscription
     */
    @Override
    public void addSubscription(Subscription subscription) {
        mSubscriptionManager.addSubscription(subscription);
    }

    /**
     * 获取MvpView
     * @return
     */
    public V getMvpView(){
        return mMvpView;
    }

    /**
     * 获取MvpModel
     * @return
     */
    public M getMvpModel(){
        return mMvpModel;
    }

}
