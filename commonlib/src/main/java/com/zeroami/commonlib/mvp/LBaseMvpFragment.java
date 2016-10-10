package com.zeroami.commonlib.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeroami.commonlib.base.LBaseFragment;
import com.zeroami.commonlib.utils.LT;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseMvpFragment，实现MvpView，完成View的通用操作</p>
 */
public abstract class LBaseMvpFragment<P extends LMvpPresenter> extends LBaseFragment implements LMvpView {

    private P mMvpPresenter;
    private ProgressDialog mProgressDialog;
    private CharSequence mProgressMessage = "正在加载...";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mMvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMvpPresenter = createPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
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

    /**
     * 设置加载显示文字
     * @param message
     */
    protected void setLoadingMessage(CharSequence message){
        mProgressMessage = message;
    }

    @Override
    public void showLoading() {
        mProgressDialog = new ProgressDialog(getAttachActivity());
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
     * 获取MvpPresenter
     *
     * @return
     */
    public P getMvpPresenter() {
        return mMvpPresenter;
    }
}
