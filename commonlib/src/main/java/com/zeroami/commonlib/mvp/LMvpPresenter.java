package com.zeroami.commonlib.mvp;


import android.os.Bundle;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：MVP的Presenter接口</p>
 */
public interface LMvpPresenter<V extends LMvpView> {

    /**
     * 关联View
     * @param view
     */
    void attachView(V view);

    /**
     * 与View解除关联
     */
    void detachView();

    /**
     * View初始化完成
     */
    void doViewInitialized();

    /**
     * 处理携带数据
     * @param extras
     */
    void doHandleExtras(Bundle extras);

}