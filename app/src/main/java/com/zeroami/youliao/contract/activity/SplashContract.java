package com.zeroami.youliao.contract.activity;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：闪屏页Contract</p>
 */
public interface SplashContract {
    interface View extends LMvpView {
        /**
         * 开启动画
         */
        void startAnimation();

        /**
         * 设置版本名
         *
         * @param versionName
         */
        void setVersionName(String versionName);

        /**
         * 跳转到登录页
         */
        void gotoLogin();

        /**
         * 跳转到主界面
         */
        void gotoMain();
    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理跳转
         */
        void doGoto();
    }
}
