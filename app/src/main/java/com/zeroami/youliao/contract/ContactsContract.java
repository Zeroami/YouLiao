package com.zeroami.youliao.contract;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：联系人Contract</p>
 */
public interface ContactsContract {
    interface View extends LMvpView{
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
    }

    interface Presenter extends LMvpPresenter<View>{
        /**
         * 处理接收到新的朋友添加请求
         */
        void doReceiveNewFriendRequest();

        /**
         * 处理新的朋友点击
         */
        void doNewFriendClick();
    }
}
