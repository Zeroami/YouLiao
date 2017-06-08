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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zeroami.commonlib.rx.rxbus.LNull;
import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.rx.rxbus.LRxBusSubscriber;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.commonlib.utils.LViewFinder;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.ContactsAdapter;
import com.zeroami.youliao.base.BaseMvpFragment;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.fragment.ContactsContract;
import com.zeroami.youliao.presenter.fragment.ContactsPresenter;
import com.zeroami.youliao.view.activity.FriendDetailActivity;
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

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.rv_friend)
    RecyclerView rvFriend;

    private LinearLayout llNewFriend;
    private LinearLayout llMyGroup;
    private LinearLayout llRobotZero;

    private TextView tvNewFriendUnread;

    private ContactsAdapter mAdapter;
    private ArrayList<User> mFriendList;
    private int mPosition;

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
    }

    @Override
    protected void subscribeRxBus() {
        addSubscription(LRxBus.getDefault().toActionObservable(Constant.Action.ADD_FRIEND)
                .subscribe(new LRxBusSubscriber<LNull>() {
                    @Override
                    protected void call(LNull o) {
                        onReceiveNewFriendRequest();
                    }
                }));
        addSubscription(LRxBus.getDefault().toActionObservable(Constant.Action.NEW_FRIEND_ADDED)
                .subscribe(new LRxBusSubscriber<LNull>() {
                    @Override
                    protected void call(LNull o) {
                        onReceiveNewFriendAdded();
                    }
                }));
        addSubscription(LRxBus.getDefault().toActionObservable(Constant.Action.DELETE_FRIEND)
                .subscribe(new LRxBusSubscriber<LNull>() {
                    @Override
                    protected void call(LNull o) {
                        onReceiveDeleteFriend();
                    }
                }));
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMvpPresenter().doViewInitialized();
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
        rvFriend.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mPosition = i;
                getMvpPresenter().doFriendItemClick();
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

    /**
     * 接收到删除朋友
     */
    private void onReceiveDeleteFriend() {
        getMvpPresenter().doReceiveDeleteFriend();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMvpPresenter().doOnResume();
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
        if (!isViewDestroyed()){
            mFriendList.clear();
            mFriendList.addAll(friendList);
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateNewFriendUnread(int count) {
        ((MainActivity) getAttachActivity()).setBottomNotificationCount(1, count);
        if (!isViewDestroyed()){
            String countStr = count == 0 ? "" : count > 99 ? "99+" : count + "";
            tvNewFriendUnread.setText(countStr);
        }
    }

    @Override
    public void showNewFriendUnread() {
        if (!isViewDestroyed()){
            tvNewFriendUnread.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideNewFriendUnread() {
        if (!isViewDestroyed()){
            tvNewFriendUnread.setVisibility(View.GONE);
        }
    }

    @Override
    public void gotoNewFriend() {
        LPageUtils.startActivity(getAttachActivity(), NewFriendActivity.class, false);
    }

    @Override
    public void gotoFriendDetail() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FriendDetailActivity.EXTRA_USER,mFriendList.get(mPosition));
        LPageUtils.startActivity(getAttachActivity(), FriendDetailActivity.class, bundle, false);
    }
}
