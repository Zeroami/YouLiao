package com.zeroami.youliao.contract.fragment;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.User;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：联系人Contract</p>
 */
public interface ContactsContract {
    interface View extends LMvpView{

        /**
         * 更新朋友列表
         * @param friendList
         */
        void updateFriendList(List<User> friendList);

        /**
         * 更新新的朋友请求未读数量
         * @param count
         */
        void updateNewFriendUnread(int count);

        /**
         * 显示新的朋友请求未读数量
         */
        void showNewFriendUnread();

        /**
         * 隐藏新的朋友请求未读数量
         */
        void hideNewFriendUnread();

        /**
         * 跳转到新的朋友页
         */
        void gotoNewFriend();

        /**
         * 跳转到朋友详情页
         */
        void gotoFriendDetail();
    }

    interface Presenter extends LMvpPresenter<View>{

        /**
         * 处理接收到新的朋友添加请求
         */
        void doReceiveNewFriendRequest();

        /**
         * 处理接收到新朋友被添加
         */
        void doReceiveNewFriendAdded();

        /**
         * 处理接收到删除朋友
         */
        void doReceiveDeleteFriend();

        /**
         * 处理页面恢复
         */
        void doOnResume();

        /**
         * 处理新的朋友点击
         */
        void doNewFriendClick();

        /**
         * 处理朋友item点击
         */
        void doFriendItemClick();
    }
}
