package com.zeroami.youliao.presenter.activity;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LNetUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.activity.LoginContract;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.real.UserModel;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：登陆Presenter</p>
 */
public class LoginPresenter extends LBasePresenter<LoginContract.View, IUserModel> implements LoginContract.Presenter {
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    protected IUserModel createRealModel() {
        return new UserModel();
    }

    @Override
    protected IUserModel createTestModel() {
        return null;
    }

    @Override
    public void doLogin() {

        if (!LNetUtils.isNetworkConnected()){
            getMvpView().showMessage(LRUtils.getString(R.string.please_check_network));
            return;
        }

        String username = getMvpView().getUsername();
        String password = getMvpView().getPassword();

        if (TextUtils.isEmpty(username)) {
            getMvpView().showMessage(LRUtils.getString(R.string.username_must_not_empty));
            return;
        }

        if (username.length() < 6 || username.length() > 16) {
            getMvpView().showMessage(LRUtils.getString(R.string.username_length_error));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            getMvpView().showMessage(LRUtils.getString(R.string.password_must_not_empty));
            return;
        }

        if (password.length() < 6 || password.length() > 16) {
            getMvpView().showMessage(LRUtils.getString(R.string.password_length_error));
            return;
        }

        getMvpModel().login(username, password, new LeanCallback<User>() {
            @Override
            public void onSuccess(User data) {
                getMvpView().gotoMain();
            }

            @Override
            public void onError(int code,AVException e) {
                getMvpView().showMessage(LRUtils.getString(R.string.login_failure));
            }
        });
    }

    @Override
    public void doRegister() {
        getMvpView().gotoRegister();
    }
}
