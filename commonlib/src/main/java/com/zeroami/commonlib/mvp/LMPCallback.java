package com.zeroami.commonlib.mvp;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Presenter调用Model方法的回调接口</p>
 */
public abstract class LMPCallback<T> {

    private LMvpView mMvpView;

    public LMPCallback(){
        this(null);
    }

    public LMPCallback(LMvpView mvpView){
        mMvpView = mvpView;
    }

    public void onStart(){
        if (mMvpView != null){
            mMvpView.showLoading();
        }
    }

    public void onFinish(){
        if (mMvpView != null){
            mMvpView.hideLoading();
        }
    }

    public abstract void onSuccess(T data);

    public abstract void onFailure(int code, Throwable e);
}
