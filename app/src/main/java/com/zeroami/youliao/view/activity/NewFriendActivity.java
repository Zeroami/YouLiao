package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.AddRequestAdapter;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.contract.NewFriendContract;
import com.zeroami.youliao.presenter.NewFriendPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：新的朋友页</p>
 */
public class NewFriendActivity extends BaseMvpActivity<NewFriendContract.Presenter> implements NewFriendContract.View {

    @Bind(R.id.rv_add_request)
    RecyclerView rvAddRequest;

    private AddRequestAdapter mAdapter;
    private List<AddRequest> mAddRequestList;

    @Override
    protected NewFriendContract.Presenter createPresenter() {
        return new NewFriendPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_friend;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        setSwipeBackEnable(true);
        initRecyclerView();
    }

    @Override
    protected void onInitialized() {
        getMvpPresenter().doViewInitialized();
    }

    private void initRecyclerView() {
        mAddRequestList = new ArrayList<>();
        mAdapter = new AddRequestAdapter(this,mAddRequestList);
        rvAddRequest.setLayoutManager(new LinearLayoutManager(this));
        rvAddRequest.setAdapter(mAdapter);
        rvAddRequest.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                LL.d(i);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                LL.d(i + "--agree");
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
    }

    @Override
    public void updateAddRequestList(List<AddRequest> addRequestList) {
        mAddRequestList.clear();
        mAddRequestList.addAll(addRequestList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void appendAddRequestList(List<AddRequest> addRequestList) {

    }
}
