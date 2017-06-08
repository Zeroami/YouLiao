package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zeroami.commonlib.recycleview.LAutoLoadOnScrollListener;
import com.zeroami.commonlib.utils.LInputMethodUtils;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LUtils;
import com.zeroami.commonlib.widget.LCircleProgressView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.AddFriendAdapter;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.activity.AddFriendContract;
import com.zeroami.youliao.presenter.activity.AddFriendPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：添加朋友页</p>
 */
public class AddFriendActivity extends BaseMvpActivity<AddFriendContract.Presenter> implements AddFriendContract.View, View.OnClickListener {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.iv_search_by_username)
    ImageView ivSearchByUsername;
    @Bind(R.id.ll_username)
    LinearLayout llUsername;
    @Bind(R.id.et_nickname)
    EditText etNickname;
    @Bind(R.id.iv_search_by_nickname)
    ImageView ivSearchByNickname;
    @Bind(R.id.ll_nickname)
    LinearLayout llNickname;
    @Bind(R.id.rv_friend)
    RecyclerView rvFriend;
    @Bind(R.id.cv_circle_progress)
    LCircleProgressView cvCircleProgress;
    @Bind(R.id.tv_empty_tips)
    TextView tvEmptyTips;

    private AddFriendAdapter mAdapter;
    private List<User> mFriendList;
    private LAutoLoadOnScrollListener mAutoLoadOnScrollListener;


    @Override
    protected AddFriendContract.Presenter createPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend;
    }


    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initToolbar();
        initListener();
        initRecycleView();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("");
        tvTitle.setText(LRUtils.getString(R.string.add_friend));
        setSupportActionBar(toolbar);
    }

    private void initListener() {
        ivSearchByUsername.setOnClickListener(this);
        ivSearchByNickname.setOnClickListener(this);
    }

    private void initRecycleView() {
        mFriendList = new ArrayList<>();
        mAdapter = new AddFriendAdapter(this, mFriendList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFriend.setLayoutManager(linearLayoutManager);
        rvFriend.setAdapter(mAdapter);
        mAutoLoadOnScrollListener = new LAutoLoadOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getMvpPresenter().doSearchByNicknameLoadMore();
            }
        };
        rvFriend.addOnScrollListener(mAutoLoadOnScrollListener);
        rvFriend.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                getMvpPresenter().doFriendItemClick(i);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_change_search:
                getMvpPresenter().doChangeSearch();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_by_username:
                getMvpPresenter().doSearchByUsername();
                break;
            case R.id.iv_search_by_nickname:
                getMvpPresenter().doSearchByNickname();
                break;
        }
    }

    @Override
    public void showUsernameSearch() {
        llUsername.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUsernameSearch() {
        llUsername.setVisibility(View.GONE);
    }

    @Override
    public void showNicknameSearch() {
        llNickname.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNicknameSearch() {
        llNickname.setVisibility(View.GONE);
    }

    @Override
    public void showFriendList() {
        rvFriend.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFriendList() {
        rvFriend.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        tvEmptyTips.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        tvEmptyTips.setVisibility(View.GONE);
    }

    @Override
    public void hideInputMethod() {
        LInputMethodUtils.hideInputMethod(etUsername);
        LInputMethodUtils.hideInputMethod(etNickname);
    }

    @Override
    public String getUsername() {
        return etUsername.getText().toString();
    }

    @Override
    public String getNickname() {
        return etNickname.getText().toString();
    }

    @Override
    public void showLoading() {
        cvCircleProgress.setVisibility(View.VISIBLE);
        cvCircleProgress.spin();
    }

    @Override
    public void hideLoading() {
        cvCircleProgress.setVisibility(View.GONE);
        cvCircleProgress.stopSpinning();
    }

    @Override
    public void updateFriendList(List<User> friendList) {
        mFriendList.clear();
        mFriendList.addAll(friendList);
        mAdapter.notifyDataSetChanged();
        rvFriend.scrollToPosition(0);
    }

    @Override
    public void appendFriendList(List<User> friendList) {
        mAutoLoadOnScrollListener.setLoading(false);
        mFriendList.addAll(friendList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void gotoFriendDetail(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FriendDetailActivity.EXTRA_USER, mFriendList.get(position));
        LPageUtils.startActivity(this, FriendDetailActivity.class, bundle, false);
    }
}