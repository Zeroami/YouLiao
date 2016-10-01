package com.zeroami.youliao.contract;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.bean.User;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：新的朋友Contract</p>
 */
public interface NewFriendContract {

    interface View extends LMvpView {
        /**
         * 更新朋友添加请求列表
         * @param addRequestList
         */
        void updateAddRequestList(List<AddRequest> addRequestList);

        /**
         * 追加朋友添加请求列表
         * @param addRequestList
         */
        void appendAddRequestList(List<AddRequest> addRequestList);
    }

    interface Presenter extends LMvpPresenter<View> {

    }
}
