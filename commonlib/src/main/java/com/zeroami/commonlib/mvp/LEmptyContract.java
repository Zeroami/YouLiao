package com.zeroami.commonlib.mvp;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

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
