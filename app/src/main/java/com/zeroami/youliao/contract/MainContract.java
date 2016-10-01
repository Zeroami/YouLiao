package com.zeroami.youliao.contract;

import com.avos.avoscloud.AVUser;
import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.User;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：主界面Contract</p>
 */
public interface MainContract {
    interface View extends LMvpView {
        /**
         * 更新当前用户信息
         */
        void updateCurrentUserInfo(User currentUser);

        /**
         * 显示搜索的控件
         */
        void showSearchView();

        /**
         * 显示添加的控件
         */
        void showAddView();

        /**
         * 跳转到登录页
         */
        void gotoLogin();

        /**
         * 跳转到个人信息页
         */
        void gotoPersonalInfo();

        /**
         * 跳转到添加朋友页
         */
        void gotoAddFriend();
    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理搜索
         */
        void doSearch();

        /**
         * 处理添加
         */
        void doAdd();

        /**
         * 处理退出登陆
         */
        void doLogout();

        /**
         * 处理头像点击
         */
        void doAvatarClick();

        /**
         * 处理添加朋友
         */
        void doAddFriend();
    }
}
