package com.zeroami.youliao.contract;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.User;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：个人信息Contract</p>
 */
public interface PersonalInfoContract {
    interface View extends LMvpView {

        /**
         * 隐藏显示信息的控件
         */
        void hideTextView();

        /**
         * 显示编辑信息的控件
         */
        void showEditView();

        /**
         * 显示显示信息的控件
         */
        void showTextView();

        /**
         * 隐藏编辑信息的控件
         */
        void hideEditView();

        /**
         * 判断内容是否改变
         *
         * @return
         */
        boolean isContentChanged();

        /**
         * 显示放弃修改的提示
         */
        void showAbandonChangeTips();

        /**
         * 恢复控件的内容
         */
        void restoreView();

        /**
         * 更新控件的内容
         */
        void updateView();
    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理编辑
         */
        void doEdit();

        /**
         * 处理放弃改变
         */
        void doAbandonChange();

        /**
         * 处理保存修改
         */
        void doSaveChange(User user);
    }
}
