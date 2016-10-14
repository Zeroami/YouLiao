package com.zeroami.youliao.contract.activity;

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

        /**
         * 获取添加请求
         * @return
         */
        AddRequest getAddRequest();

        void notifyDataSetChanged();

        /**
         * 跳转到聊天页
         */
        void gotoChat();
    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理加载更多
         */
        void doLoadMore();

        /**
         * 处理同意添加请求
         */
        void doAgreeAddRequest();
    }
}
