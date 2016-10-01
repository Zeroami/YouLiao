package com.zeroami.commonlib.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.zeroami.commonlib.base.LBaseActivity;
import com.zeroami.commonlib.utils.LT;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：MvpActivity，实现MvpView，完成View的通用操作</p>
 */
public abstract class LBaseMvpActivity<P extends LMvpPresenter> extends LBaseActivity implements LMvpView {

    private P mMvpPresenter;
    private ProgressDialog mProgressDialog;
    private CharSequence mProgressMessage = "正在加载...";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mMvpPresenter != null) {
            mMvpPresenter.detachView();
        }
        super.onDestroy();
    }

    /**
     * 创建Presenter
     *
     * @return
     */
    protected abstract P createPresenter();


    @Override
    public void showLoading() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(mProgressMessage);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void showToast(CharSequence text) {
        LT.show(text);
    }

    /**
     * 设置加载显示文字
     * @param message
     */
    public void setLoadingMessage(CharSequence message){
        mProgressMessage = message;
    }

    /**
     * 获取MvpPresenter
     *
     * @return
     */
    public P getMvpPresenter() {
        return mMvpPresenter;
    }

}
