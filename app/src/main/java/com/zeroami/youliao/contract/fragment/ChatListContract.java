package com.zeroami.youliao.contract.fragment;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天列表Contract</p>
 */
public interface ChatListContract {
    interface View extends LMvpView{
    }

    interface Presenter extends LMvpPresenter<View>{
    }
}
