package com.zeroami.youliao.presenter.activity;

import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.mvp.LMvpModel;
import com.zeroami.youliao.contract.activity.ChatContract;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：</p>
 */
public class ChatPresenter extends LBasePresenter<ChatContract.View,LMvpModel> implements ChatContract.Presenter {
    public ChatPresenter(ChatContract.View view) {
        super(view);
    }

    @Override
    protected LMvpModel getRealModel() {
        return null;
    }

    @Override
    protected LMvpModel getTestModel() {
        return null;
    }

    @Override
    public void doExpressionClick() {
        getMvpView().hideInputMethod();
        getMvpView().showBottomLayout();
        getMvpView().showExpressionLayout();
        getMvpView().clearEditTextFocus();
    }

    @Override
    public void doEditTextGetFocus() {
        getMvpView().hideBottomLayout();
        getMvpView().hideExpressionLayout();
    }

    @Override
    public void doOutsideClick() {
        getMvpView().clearEditTextFocus();
        getMvpView().hideInputMethod();
        getMvpView().hideBottomLayout();
        getMvpView().hideExpressionLayout();
    }
}
