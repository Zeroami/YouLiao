package com.zeroami.commonlib.mvp;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：空的Contract</p>
 */
public interface LEmptyContract {

    interface View extends LMvpView {

    }

    interface Presenter extends LMvpPresenter<View> {

    }
}
