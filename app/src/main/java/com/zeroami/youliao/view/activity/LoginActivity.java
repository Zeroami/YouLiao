package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.contract.activity.LoginContract;
import com.zeroami.youliao.presenter.activity.LoginPresenter;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：登陆页</p>
 */
public class LoginActivity extends BaseMvpActivity<LoginContract.Presenter> implements LoginContract.View, View.OnClickListener {

    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_register)
    Button btnRegister;

    @Override
    protected LoginContract.Presenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                getMvpPresenter().doLogin();
                break;
            case R.id.btn_register:
                getMvpPresenter().doRegister();
                break;
        }
    }

    @Override
    public String getUsername() {
        return etUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void gotoRegister() {
        LPageUtils.startActivity(this,RegisterActivity.class,false);
    }

    @Override
    public void gotoMain() {
        LPageUtils.startActivity(this, MainActivity.class, true);
    }

}
