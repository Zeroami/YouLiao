package com.zeroami.commonlib.mvp;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.zeroami.commonlib.R;
import com.zeroami.commonlib.base.LBaseActivity;
import com.zeroami.commonlib.utils.LT;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseMvpActivity，实现MvpView，完成View的通用操作</p>
 */
public abstract class LBaseMvpActivity<P extends LMvpPresenter> extends LBaseActivity implements LMvpView {

    private P mMvpPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    }

    /**
     * 创建Presenter
     *
     * @return
     */
    protected abstract P createPresenter();

    @Override
    public void showLoading() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void showMessage(CharSequence text) {
        LT.show(text);
    }


    @Override
    protected void handleExtras(Bundle extras) {
        if (mMvpPresenter != null) {
            mMvpPresenter.doHandleExtras(extras);
        }
    }

    @Override
    protected void onInitialized() {
        if (mMvpPresenter != null){
            getMvpPresenter().doViewInitialized();
        }
    }

    /**
     * 获取MvpPresenter
     *
     * @return
     */
    protected P getMvpPresenter() {
        return mMvpPresenter;
    }

    /**
     * 获取SwipeRefreshLayout
     *
     * @return
     */
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

}
