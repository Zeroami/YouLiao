package com.zeroami.youliao.contract.activity;

import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天Contract</p>
 */
public interface ChatContract {

    interface View extends LMvpView {
        /**
         * 显示底部布局
         */
        void showBottomLayout();

        /**
         * 隐藏底部布局
         */
        void hideBottomLayout();

        /**
         * 显示表情布局
         */
        void showExpressionLayout();

        /**
         * 隐藏表情布局
         */
        void hideExpressionLayout();

        /**
         * 隐藏输入法
         */
        void hideInputMethod();

        /**
         * 清除输入框的焦点
         */
        void clearEditTextFocus();

    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理表情点击
         */
        void doExpressionClick();

        /**
         * 处理输入框获取焦点
         */
        void doEditTextGetFocus();

        /**
         * 处理外部点击
         */
        void doOutsideClick();
    }
}
