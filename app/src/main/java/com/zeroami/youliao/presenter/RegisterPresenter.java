package com.zeroami.youliao.presenter;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.RegisterContract;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.UserModel;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：注册Presenter</p>
 */
public class RegisterPresenter extends LBasePresenter<RegisterContract.View, IUserModel> implements RegisterContract.Presenter {
    public RegisterPresenter(RegisterContract.View view) {
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
    public void doRegister() {
        String username = getMvpView().getUsername();
        String password = getMvpView().getPassword();

        if (TextUtils.isEmpty(username)) {
            getMvpView().showToast(LRUtils.getString(R.string.username_must_not_empty));
            return;
        }

        if (username.length() < 6 || username.length() > 16) {
            getMvpView().showToast(LRUtils.getString(R.string.username_length_error));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            getMvpView().showToast(LRUtils.getString(R.string.password_must_not_empty));
            return;
        }

        if (password.length() < 6 || password.length() > 16) {
            getMvpView().showToast(LRUtils.getString(R.string.password_length_error));
            return;
        }

        getMvpModel().register(username, password, new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                getMvpView().showRegisterSuccessTips();
            }

            @Override
            public void onError(int code, AVException e) {
                if (code == Constant.CODE_USERNAME_IS_EXIST) {
                    getMvpView().showToast(LRUtils.getString(R.string.username_already_exist));
                } else {
                    getMvpView().showToast(LRUtils.getString(R.string.register_failure));
                }
            }
        });
    }
}
