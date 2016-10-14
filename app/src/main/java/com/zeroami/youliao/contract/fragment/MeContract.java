package com.zeroami.youliao.contract.fragment;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：我的Contract</p>
 */
public interface MeContract {
    interface View extends LMvpView{
        /**
         * 跳转到个人信息页
         */
        void gotoPersonalInfo();

        /**
         * 跳转到登录页
         */
        void gotoLogin();
    }

    interface Presenter extends LMvpPresenter<View>{
        /**
         * 处理个人信息区域点击
         */
        void doPersonalInfoAreaClick();

        /**
         * 处理退出登陆
         */
        void doLogout();
    }
}
