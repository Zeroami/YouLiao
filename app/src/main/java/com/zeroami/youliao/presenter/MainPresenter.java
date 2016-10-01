package com.zeroami.youliao.presenter;

import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.youliao.contract.MainContract;
import com.zeroami.youliao.model.IUserModel;
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
        getMvpView().updateCurrentUserInfo(getMvpModel().getCurrentUser());
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
