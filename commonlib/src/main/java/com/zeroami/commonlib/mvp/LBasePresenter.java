package com.zeroami.commonlib.mvp;

import com.zeroami.commonlib.rx.LSubscriptionManager;
import com.zeroami.commonlib.utils.LL;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import rx.Subscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BasePresenter，实现MvpPresenter，完成Presenter的通用操作</p>
 */
public abstract class LBasePresenter<V extends LMvpView,M extends LMvpModel> implements LMvpPresenter<V>,LRxSupport {

    private V mMvpView;
    private V mEmptyMvpView;    // 一个空实现的MvpView，避免V和P解除绑定时P持有的V的MvpView引用为空导致空指针
    private M mMvpModel;
    private M mRealModel;
    private M mTestModel;
    private LSubscriptionManager mSubscriptionManager;

    public LBasePresenter(V view){
        attachView(view);
        createEmptyMvpView();
        mRealModel = getRealModel();
        mTestModel = getTestModel();
        mMvpModel = mRealModel;
        mSubscriptionManager = new LSubscriptionManager();
    }

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

    @Override
    public void doViewInitialized() {}

    @Override
    public void attachView(V view) {
        this.mMvpView = view;
        onViewAttched();
    }

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

    @Override
    public void addSubscription(Subscription subscription) {
        mSubscriptionManager.addSubscription(subscription);
    }

    /**
     * 获取MvpView
     * @return
     */
    public V getMvpView(){
        return mMvpView == null ? mEmptyMvpView : mMvpView;
    }

    /**
     * 获取MvpModel
     * @return
     */
    public M getMvpModel(){
        return mMvpModel;
    }

    /**
     * 创建空实现的MvpView
     */
    private void createEmptyMvpView(){
        mEmptyMvpView = (V) Proxy.newProxyInstance(getClass().getClassLoader(), mMvpView.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                LL.i("EmptyMvpView的%s方法被调用",method.getName());
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                return null;
            }
        });
    }

}
