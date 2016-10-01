package com.zeroami.youliao.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpFragment;
import com.zeroami.youliao.contract.ChatContract;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天</p>
 */
public class ChatFragment extends BaseMvpFragment<ChatContract.Presenter> implements ChatContract.View {

    public static ChatFragment newInstance(){
        return new ChatFragment();
    }

    @Override
    protected ChatContract.Presenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initialize(View layoutView, Bundle savedInstanceState) {

    }

}
