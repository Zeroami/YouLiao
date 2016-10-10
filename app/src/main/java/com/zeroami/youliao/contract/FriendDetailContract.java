package com.zeroami.youliao.contract;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.User;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：朋友详情Contract</p>
 */
public interface FriendDetailContract {
    interface View extends LMvpView {
        /**
         * 获取当前打开的朋友
         *
         * @return
         */
        User getUser();

        /**
         * 编辑额外信息
         */
        void editExtra();

        /**
         * 设置默认额外信息
         *
         * @param extra
         */
        void setDefaultExtra(String extra);

        /**
         * 获取额外信息
         *
         * @return
         */
        String getExtra();

        /**
         * 显示添加朋友按钮
         */
        void showAddFriend();

        /**
         * 隐藏添加朋友按钮
         */
        void hideAddFriend();

        /**
         * 显示发送消息按钮
         */
        void showSendMessage();

        /**
         * 隐藏发送信息按钮
         */
        void hideSendMessage();

        /**
         * 显示已发送添加请求提示
         */
        void showAlreadSendRequestTips();

        /**
         * 隐藏已发送添加请求提示
         */
        void hideAlreadSendRequestTips();

        /**
         * 跳转到大图页
         */
        void gotoImage();
    }

    interface Presenter extends LMvpPresenter<View> {

        /**
         * 处理编辑额外信息
         */
        void doEditExtra();

        /**
         * 处理添加朋友
         */
        void doAddFriend();

        /**
         * 处理头像点击
         */
        void doAvatarClick();
    }
}
