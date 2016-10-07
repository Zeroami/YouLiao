package com.zeroami.commonlib.base;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;

import com.zeroami.commonlib.library.swipebacklayout.SwipeBackActivity;
import com.zeroami.commonlib.mvp.LRxSupport;
import com.zeroami.commonlib.rx.LSubscriptionManager;
import com.zeroami.commonlib.utils.LActivityUtils;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseActivity，完成共有操作，定义操作流程</p>
 */
public abstract class LBaseActivity extends SwipeBackActivity implements LRxSupport {


    private LSubscriptionManager mSubscriptionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LActivityUtils.addActivity(this);
        mSubscriptionManager = new LSubscriptionManager();
        if (isFullScreenLayout()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
        onSetContentViewBefore();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        onViewCreated();
        if (getIntent() != null) {
            handleIntent(getIntent());
            if (getIntent().getExtras() != null) {
                handleExtras(getIntent().getExtras());
            }
        }
        //避免重复添加Fragment
        if (getSupportFragmentManager().getFragments() == null) {
            Fragment firstFragment = getFirstFragment();
            if (firstFragment != null) {
                replaceFragment(firstFragment, false);
            }
        }
        setSwipeBackEnable(false);      // 默认不带滑动退出的效果，让子类根据需要设置
        initialize(savedInstanceState);
        onInitialized();
    }

    @Override
    protected void onDestroy() {
        mSubscriptionManager.unsubscribeAllSubscription();
        LActivityUtils.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        setupExitAnimation();
    }

    /**
     * 获取布局文件id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化操作
     *
     * @param savedInstanceState
     */
    protected abstract void initialize(Bundle savedInstanceState);

    /**
     * 在setContentView之前
     */
    protected void onSetContentViewBefore(){}

    /**
     * 是否全屏显示布局，会将布局延伸到状态栏
     * @return
     */
    protected boolean isFullScreenLayout() {
        return true;
    }

    /**
     * 获取Fragment容器id
     *
     * @return
     */
    protected int getFragmentContainerId() {
        return 0;
    }

    /**
     * setContentView完成
     */
    protected void onViewCreated() {
    }

    /**
     * initialize完成
     */
    protected void onInitialized() {
    }

    /**
     * 处理Intent
     *
     * @param intent
     */
    protected void handleIntent(Intent intent) {
    }

    /**
     * 处理携带的数据
     *
     * @param extras
     */
    protected void handleExtras(Bundle extras) {
    }

    /**
     * 获取显示的第一个Fragment
     *
     * @return
     */
    protected Fragment getFirstFragment() {
        return null;
    }


    /**
     * 设置退出默认动画
     */
    protected void setupExitAnimation(){
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    /**
     * 添加fragment
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment ,boolean isAddToBackStack) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName());
            if (isAddToBackStack){
                transaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            transaction.commit();
        }
    }

    /**
     * 移除fragment
     */
    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * 替换fragment
     *
     * @param fragment
     */
    public void replaceFragment(Fragment fragment ,boolean isAddToBackStack) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName());
            if (isAddToBackStack){
                transaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            transaction.commit();
        }
    }

    /**
     * 显示fragment
     *
     * @param fragment
     */
    public void showFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .show(fragment)
                    .commit();
        }
    }

    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    public void hideFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .hide(fragment)
                    .commit();
        }
    }

    /**
     * 添加一个订阅
     * @param subscription
     */
    @Override
    public void addSubscription(Subscription subscription) {
        mSubscriptionManager.addSubscription(subscription);
    }
}
