package com.zeroami.youliao.contract;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：注册Contract</p>
 */
public interface RegisterContract {
    interface View extends LMvpView {
        /**
         * 获取账户
         */
        String getUsername();

        /**
         * 获取密码
         */
        String getPassword();

        /**
         * 显示注册成功提示
         */
        void showRegisterSuccessTips();
    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理注册
         */
        void doRegister();
    }
}
