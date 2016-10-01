package com.zeroami.commonlib.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.zeroami.commonlib.base.LBaseFragment;
import com.zeroami.commonlib.utils.LT;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：MvpFragment</p>
 */
public abstract class LBaseMvpFragment<P extends LMvpPresenter> extends LBaseFragment implements LMvpView {

    private P mMvpPresenter;
    private ProgressDialog mProgressDialog;
    private CharSequence mProgressMessage = "正在加载...";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMvpPresenter = createPresenter();
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
    public void setLoadingMessage(CharSequence message){
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
