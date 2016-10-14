package com.zeroami.youliao.contract.activity;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：登陆Contract</p>
 */
public interface LoginContract {
    interface View extends LMvpView {
        /**
         * 获取账户
         *
         * @return
         */
        String getUsername();

        /**
         * 获取密码
         *
         * @return
         */
        String getPassword();

        /**
         * 跳转到注册页
         */
        void gotoRegister();

        /**
         * 跳转到主界面
         */
        void gotoMain();
    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理登陆
         */
        void doLogin();

        /**
         * 处理注册
         */
        void doRegister();
    }
}
