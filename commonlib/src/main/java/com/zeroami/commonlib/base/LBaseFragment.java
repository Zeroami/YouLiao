package com.zeroami.commonlib.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeroami.commonlib.mvp.LRxSupport;
import com.zeroami.commonlib.rx.LSubscriptionManager;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseFragment</p>
 */
public abstract class LBaseFragment extends Fragment implements LRxSupport {

    private Activity mActivity;
    private View mLayoutView;

    private LSubscriptionManager mSubscriptionManager;

    private boolean mIsViewDestoryed = false;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscriptionManager = new LSubscriptionManager();
        subscribeRxBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mLayoutView);
        mIsViewDestoryed = false;
        initialize(mLayoutView, savedInstanceState);
        onInitialized();
        return mLayoutView;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        mIsViewDestoryed = true;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mSubscriptionManager.unsubscribeAllSubscription();
        super.onDestroy();
    }

    /**
     * 获取布局文件id
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化操作
     * @param savedInstanceState
     */
    protected abstract void initialize(View layoutView,Bundle savedInstanceState);

    /**
     * 订阅RxBus
     */
    protected void subscribeRxBus() {
    }

    /**
     * initialize完成
     */
    protected void onInitialized() {
    }

    /**
     * 获取宿主Activity
     * @return
     */
    public Activity getAttachActivity(){
        return mActivity;
    }

    /**
     * 获取布局View
     * @return
     */
    public View getLayoutView(){
        return mLayoutView;
    }

    /**
     * View是否已经被销毁
     * @return
     */
    public boolean isViewDestoryed(){
        return mIsViewDestoryed;
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
