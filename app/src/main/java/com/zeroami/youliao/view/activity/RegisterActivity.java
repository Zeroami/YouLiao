package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.contract.activity.RegisterContract;
import com.zeroami.youliao.presenter.activity.RegisterPresenter;

import butterknife.Bind;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：注册页</p>
 */
public class RegisterActivity extends BaseMvpActivity<RegisterContract.Presenter> implements RegisterContract.View, View.OnClickListener {

    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_register)
    Button btnRegister;

    @Override
    protected RegisterContract.Presenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
    public void showRegisterSuccessTips() {
        new MaterialDialog(this)
        .setTitle(LRUtils.getString(R.string.dialog_tips))
        .setMessage(LRUtils.getString(R.string.register_success))
        .setPositiveButton(LRUtils.getString(R.string.confirm), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).show();
    }

}
