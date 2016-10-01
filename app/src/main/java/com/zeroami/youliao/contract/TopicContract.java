package com.zeroami.youliao.contract;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：话题Contract</p>
 */
public interface TopicContract {
    interface View extends LMvpView{
    }

    interface Presenter extends LMvpPresenter<View>{
    }
}
