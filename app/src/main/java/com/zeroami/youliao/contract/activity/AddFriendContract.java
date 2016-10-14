package com.zeroami.youliao.contract.activity;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.User;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：添加朋友Contract</p>
 */
public interface AddFriendContract {
    interface View extends LMvpView{

        /**
         * 显示账号搜索布局
         */
        void showUsernameSearch();

        /**
         * 隐藏账号搜索布局
         */
        void hideUsernameSearch();

        /**
         * 显示昵称搜索布局
         */
        void showNicknameSearch();

        /**
         * 隐藏昵称搜索布局
         */
        void hideNicknameSearch();

        /**
         * 显示列表
         */
        void showList();

        /**
         * 隐藏列表
         */
        void hideList();

        /**
         * 显示空布局
         */
        void showEmpty();

        /**
         * 隐藏空布局
         */
        void hideEmpty();

        /**
         * 隐藏输入法
         */
        void hideInputMethod();

        /**
         * 获取输入的账号
         * @return
         */
        String getUsername();

        /**
         * 获取输入的昵称
         * @return
         */
        String getNickname();

        /**
         * 更新朋友列表
         * @param friendList
         */
        void updateFriendList(List<User> friendList);

        /**
         * 追加朋友列表
         * @param friendList
         */
        void appendFriendList(List<User> friendList);

        /**
         * 跳转到朋友详情页
         * @param position
         */
        void gotoFriendDetail(int position);

    }

    interface Presenter extends LMvpPresenter<View>{
        /**
         * 处理切换搜索条件
         */
        void doChangeSearch();

        /**
         * 处理通过账号搜索
         */
        void doSearchByUsername();

        /**
         * 处理通过昵称搜索
         */
        void doSearchByNickname();

        /**
         * 处理列表上拉加载
         */
        void doSearchByNicknameLoadMore();

        /**
         * 处理列表item点击
         * @param position
         */
        void doListItemClick(int position);
    }
}
