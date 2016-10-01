package com.zeroami.youliao.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpFragment;
import com.zeroami.youliao.contract.ChatContract;
import com.zeroami.youliao.contract.TopicContract;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：话题</p>
 */
public class TopicFragment extends BaseMvpFragment<TopicContract.Presenter> implements TopicContract.View {

    public static TopicFragment newInstance(){
        return new TopicFragment();
    }

    @Override
    protected TopicContract.Presenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void initialize(View layoutView, Bundle savedInstanceState) {

    }

}
