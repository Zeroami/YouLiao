package com.zeroami.youliao.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.rx.rxbus.LRxBusSubscriber;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.commonlib.utils.LViewFinder;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.ContactsAdapter;
import com.zeroami.youliao.base.BaseMvpFragment;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.ContactsContract;
import com.zeroami.youliao.presenter.ContactsPresenter;
import com.zeroami.youliao.view.activity.MainActivity;
import com.zeroami.youliao.view.activity.NewFriendActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：联系人</p>
 */
public class ContactsFragment extends BaseMvpFragment<ContactsContract.Presenter> implements ContactsContract.View, View.OnClickListener {

    @Bind(R.id.swlRefresh)
    SwipeRefreshLayout swlRefresh;
    @Bind(R.id.rv_friend)
    RecyclerView rvFriend;

    private LinearLayout llNewFriend;
    private LinearLayout llMyGroup;
    private LinearLayout llRobotZero;

    private TextView tvNewFriendUnread;

    private ContactsAdapter mAdapter;
    private ArrayList<User> mFriendList;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    protected ContactsContract.Presenter createPresenter() {
        return new ContactsPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initialize(View layoutView, Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        initAdapter();
        initRecyclerView();
        initRxBusListener();
    }

    @Override
    protected void onInitialized() {
        getMvpPresenter().doViewInitialized();
    }

    private void initSwipeRefreshLayout() {
        swlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMvpPresenter().doViewInitialized();
                swlRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swlRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void initAdapter() {
        mFriendList = new ArrayList<>();
        mAdapter = new ContactsAdapter(getAttachActivity(), mFriendList);
        initHeaderView();
    }


    private void initRecyclerView() {
        rvFriend.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        rvFriend.setAdapter(mAdapter);
        rvFriend.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                LT.show(i);
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

    private void initHeaderView() {
        View headerView = LayoutInflater.from(getAttachActivity()).inflate(R.layout.part_contacts_header, null);
        mAdapter.addHeaderView(headerView);
        LViewFinder viewFinder = new LViewFinder(headerView);
        llNewFriend = viewFinder.find(R.id.ll_new_friend);
        llMyGroup = viewFinder.find(R.id.ll_my_group);
        llRobotZero = viewFinder.find(R.id.ll_robot_zero);
        tvNewFriendUnread = viewFinder.find(R.id.tv_new_friend_unread);
        llNewFriend.setOnClickListener(this);
        llMyGroup.setOnClickListener(this);
        llRobotZero.setOnClickListener(this);
    }


    private void initRxBusListener() {
        LRxBus.getDefault().toTagObservable(Constant.Action.ADD_FRIEND)
                .subscribe(new LRxBusSubscriber<Object>() {
                    @Override
                    protected void call(Object o) {
                        onReceiveNewFriendRequest();
                    }
                });
        LRxBus.getDefault().toTagObservable(Constant.Action.NEW_FRIEND_ADDED)
                .subscribe(new LRxBusSubscriber<Object>() {
                    @Override
                    protected void call(Object o) {
                        onReceiveNewFriendAdded();
                    }
                });
    }

    /**
     * 接收到好友添加请求会回调
     */
    private void onReceiveNewFriendRequest() {
        getMvpPresenter().doReceiveNewFriendRequest();
    }

    /**
     * 接收到新朋友被添加
     */
    private void onReceiveNewFriendAdded() {
        getMvpPresenter().doReceiveNewFriendAdded();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_new_friend:
                getMvpPresenter().doNewFriendClick();
                break;
            case R.id.ll_my_group:
                LT.show("my group");
                break;
            case R.id.ll_robot_zero:
                LT.show("robot zero");
                break;
        }
    }

    @Override
    public void updateFriendList(List<User> friendList) {
        mFriendList.clear();
        mFriendList.addAll(friendList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateNewFriendUnread(int count) {
        ((MainActivity) getAttachActivity()).setBottomNotificationCount(1, count);
        String countStr = count == 0 ? "" : count > 99 ? "99+" : count + "";
        tvNewFriendUnread.setText(countStr);
    }

    @Override
    public void showNewFriendUnread() {
        tvNewFriendUnread.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNewFriendUnread() {
        tvNewFriendUnread.setVisibility(View.GONE);
    }

    @Override
    public void gotoNewFriend() {
        LPageUtils.startActivity(getAttachActivity(), NewFriendActivity.class, false);
    }
}
