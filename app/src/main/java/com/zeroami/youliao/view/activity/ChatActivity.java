package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zeroami.commonlib.mvp.LEmptyContract;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends BaseMvpActivity<LEmptyContract.Presenter> implements LEmptyContract.View {

    public static final String EXTRA_USER = "extra_user";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private User mUser;

    @Override
    protected LEmptyContract.Presenter createPresenter() {
        return null;
    }

    @Override
    protected void handleExtras(Bundle extras) {
        mUser = (User) extras.getSerializable(EXTRA_USER);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        setSwipeBackEnable(true);
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");
        tvTitle.setText(mUser.getNickname());
        setSupportActionBar(toolbar);
    }
}
