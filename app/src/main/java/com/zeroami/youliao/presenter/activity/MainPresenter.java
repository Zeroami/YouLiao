package com.zeroami.youliao.presenter.activity;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.youliao.contract.activity.MainContract;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.UserModel;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：主界面Presenter</p>
 */
public class MainPresenter extends LBasePresenter<MainContract.View, IUserModel> implements MainContract.Presenter {
    public MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    protected IUserModel getRealModel() {
        return new UserModel();
    }

    @Override
    protected IUserModel getTestModel() {
        return null;
    }


    @Override
    public void doViewInitialized() {
        getMvpModel().enterApp();
        getMvpModel().connectServer(new LeanCallback() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(int code, AVException e) {

            }
        });
        getMvpView().updateCurrentUserInfo(getMvpModel().getCurrentUser());
    }

    @Override
    public void doMenuKeyDown() {
        getMvpView().showBottomSheet();
    }

    @Override
    public void doExitApp() {
        getMvpModel().exitApp();
        getMvpView().exitApp();
    }

    @Override
    public void doSearch() {

    }

    @Override
    public void doAdd() {
        getMvpView().showAddView();
    }

    @Override
    public void doLogout() {
        getMvpModel().logout();
        getMvpView().gotoLogin();
    }

    @Override
    public void doAvatarClick() {
        getMvpView().gotoPersonalInfo();
    }

    @Override
    public void doAddFriend() {
        getMvpView().gotoAddFriend();
    }
}
