package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zeroami.commonlib.recycleview.LAutoLoadOnScrollListener;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.AddRequestAdapter;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.contract.activity.NewFriendContract;
import com.zeroami.youliao.presenter.activity.NewFriendPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：新的朋友页</p>
 */
public class NewFriendActivity extends BaseMvpActivity<NewFriendContract.Presenter> implements NewFriendContract.View {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_new_friend)
    RecyclerView rvNewFriend;

    private AddRequestAdapter mAdapter;
    private List<AddRequest> mAddRequestList;
    private LAutoLoadOnScrollListener mAutoLoadOnScrollListener;

    private AddRequest mAddRequest;

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
        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void onInitialized() {
        getMvpPresenter().doViewInitialized();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("");
        tvTitle.setText(LRUtils.getString(R.string.new_friend));
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView() {
        mAddRequestList = new ArrayList<>();
        mAdapter = new AddRequestAdapter(this,mAddRequestList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvNewFriend.setLayoutManager(linearLayoutManager);
        rvNewFriend.setAdapter(mAdapter);

        mAutoLoadOnScrollListener = new LAutoLoadOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getMvpPresenter().doLoadMore();
            }
        };
        rvNewFriend.addOnScrollListener(mAutoLoadOnScrollListener);

        rvNewFriend.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mAddRequest = mAddRequestList.get(i);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mAddRequest = mAddRequestList.get(i);
                getMvpPresenter().doAgreeAddRequest();
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void updateAddRequestList(List<AddRequest> addRequestList) {
        mAddRequestList.clear();
        mAddRequestList.addAll(addRequestList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void appendAddRequestList(List<AddRequest> addRequestList) {
        mAutoLoadOnScrollListener.setLoading(false);
        mAddRequestList.addAll(addRequestList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public AddRequest getAddRequest() {
        return mAddRequest;
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void gotoChat() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChatActivity.EXTRA_USER,mAddRequest.getFromUser());
        LPageUtils.startActivity(this,ChatActivity.class,bundle,false);
    }
}
