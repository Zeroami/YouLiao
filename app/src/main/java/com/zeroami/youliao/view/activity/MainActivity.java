package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.zeroami.commonlib.utils.LDisplayUtils;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.commonlib.utils.LViewFinder;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.MainContentAdapter;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.MainContract;
import com.zeroami.youliao.presenter.MainPresenter;
import com.zeroami.youliao.view.fragment.ChatFragment;
import com.zeroami.youliao.view.fragment.ContactsFragment;
import com.zeroami.youliao.view.fragment.MeFragment;
import com.zeroami.youliao.view.fragment.TopicFragment;
import com.zeroami.youliao.widget.RelativePopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：主界面</p>
 */
public class MainActivity extends BaseMvpActivity<MainContract.Presenter> implements MainContract.View, View.OnClickListener {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.cv_bottom_navigation)
    AHBottomNavigation cvBottomNavigation;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    @Bind(R.id.nv_drawer)
    NavigationView nvDrawer;
    @Bind(R.id.dl_drawer)
    DrawerLayout dlDrawer;
    @Bind(R.id.tv_topic_unread)
    TextView tvTopicUnread;
    @Bind(R.id.tv_focus_unread)
    TextView tvFocusUnread;

    private List<Fragment> mFragmentList;
    private MainContentAdapter mContentAdapter;
    private int mCurrentPosition = -1;

    private View mAddPopupView;
    private RelativePopupWindow mAddPopupWindow;
    private int mAddPopupViewWidth;

    private User mCurrentUser;

    private LinearLayout llDrawerHeaderView;
    private LCircleImageView cvCircleAvatar;
    private TextView tvUsername;
    private TextView tvNickname;
    private TextView tvSignature;
    private boolean mIsDrawerHeaderViewCreated;
    private ChatFragment mChatFragment;
    private ContactsFragment mContactsFragment;
    private TopicFragment mTopicFragment;
    private MeFragment mMeFragment;
    private BottomSheet mBottomSheet;

    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPresenter(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initToolbar();
        initDrawerLayout();
        initPopupWindow();
        initData();
        initViewPager();
        initBottomNavigation();
        setTabSelected(0);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initDrawerLayout() {
        initDrawerInnerView();
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_topic_center:

                        break;
                    case R.id.nav_my_focus:

                        break;
                    case R.id.nav_my_photos:

                        break;
                    case R.id.nav_my_collection:

                        break;
                    case R.id.nav_about_author:

                        break;
                    case R.id.nav_logout:
                        getMvpPresenter().doLogout();
                        break;
                }
                dlDrawer.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }

    /**
     * 初始化抽屉菜单内部控件
     */
    private void initDrawerInnerView() {
        final NavigationMenuView navigationMenuView = (NavigationMenuView) nvDrawer.getChildAt(0);
        navigationMenuView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                navigationMenuView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // 获取抽屉菜单顶部View
                llDrawerHeaderView = (LinearLayout) navigationMenuView.getChildAt(0);
                onDrawerHeaderViewCreated();
                NavigationMenuItemView topicMenuItem = (NavigationMenuItemView) navigationMenuView.getChildAt(1);
                NavigationMenuItemView focusMenuItem = (NavigationMenuItemView) navigationMenuView.getChildAt(2);
                // 初始化未读数量控件位置
                updateUnreadViewPosition(tvTopicUnread, topicMenuItem.getTop() + LDisplayUtils.dip2px(8), topicMenuItem.getLeft() + LDisplayUtils.dip2px(30));
                updateUnreadViewPosition(tvFocusUnread, focusMenuItem.getTop() + LDisplayUtils.dip2px(8), focusMenuItem.getLeft() + LDisplayUtils.dip2px(30));
            }
        });
        // 使未读数量控件跟随navigationMenuView（RecyclerView）滑动
        navigationMenuView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                updateUnreadViewPosition(tvTopicUnread, -dy, 0);
                updateUnreadViewPosition(tvFocusUnread, -dy, 0);
            }
        });
    }

    /**
     * 当抽屉菜单头部信息控件创建完成（NavigationView的子控件NavigationMenuView本质为RecyclerView，渲染需要时间，需要监听事件）
     */
    private void onDrawerHeaderViewCreated() {
        mIsDrawerHeaderViewCreated = true;
        initDrawerHeaderView();
        getMvpPresenter().doViewInitialized();
    }

    private void initDrawerHeaderView() {
        LViewFinder viewFinder = new LViewFinder(llDrawerHeaderView);
        cvCircleAvatar = viewFinder.find(R.id.cv_circle_avatar);
        tvNickname = viewFinder.find(R.id.tv_nickname);
        tvSignature = viewFinder.find(R.id.tv_signature);
        tvUsername = viewFinder.find(R.id.tv_username);
        cvCircleAvatar.setOnClickListener(this);
    }

    private void initPopupWindow() {
        mAddPopupView = LayoutInflater.from(this).inflate(R.layout.layout_popup_add, null);
        mAddPopupView.measure(0, 0);
        mAddPopupViewWidth = mAddPopupView.getMeasuredWidth();
        LViewFinder viewFinder = new LViewFinder(mAddPopupView);
        viewFinder.onClick(R.id.ll_scan_qrcode, this);
        viewFinder.onClick(R.id.ll_add_friend, this);
        viewFinder.onClick(R.id.ll_add_group, this);

        mAddPopupWindow = new RelativePopupWindow(mAddPopupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mAddPopupWindow.setBackgroundDrawable(LRUtils.getDrawable(R.drawable.bg_popup));
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mChatFragment = ChatFragment.newInstance();
        mContactsFragment = ContactsFragment.newInstance();
        mTopicFragment = TopicFragment.newInstance();
        mMeFragment = MeFragment.newInstance();
        mFragmentList.add(mChatFragment);
        mFragmentList.add(mContactsFragment);
        mFragmentList.add(mTopicFragment);
        mFragmentList.add(mMeFragment);
    }

    private void initViewPager() {
        mContentAdapter = new MainContentAdapter(getSupportFragmentManager(), mFragmentList);
        vpContent.setAdapter(mContentAdapter);
        vpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTabSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomNavigation() {

        // Create items
        AHBottomNavigationItem chatItem = new AHBottomNavigationItem(LRUtils.getString(R.string.chat), R.mipmap.ic_chat);
        AHBottomNavigationItem contactsItem = new AHBottomNavigationItem(LRUtils.getString(R.string.contacts), R.mipmap.ic_contacts);
        AHBottomNavigationItem topicItem = new AHBottomNavigationItem(LRUtils.getString(R.string.topic), R.mipmap.ic_topic);
        AHBottomNavigationItem meItem = new AHBottomNavigationItem(LRUtils.getString(R.string.me), R.mipmap.ic_me);

        // Add items
        cvBottomNavigation.addItem(chatItem);
        cvBottomNavigation.addItem(contactsItem);
        cvBottomNavigation.addItem(topicItem);
        cvBottomNavigation.addItem(meItem);

        // Set background color
        cvBottomNavigation.setDefaultBackgroundColor(LRUtils.getColor(R.color.bg_bottom_nav));

        // Disable the translation inside the CoordinatorLayout
        cvBottomNavigation.setBehaviorTranslationEnabled(false);

        // Change colors
        cvBottomNavigation.setAccentColor(LRUtils.getColor(R.color.nav_accent));
        cvBottomNavigation.setInactiveColor(LRUtils.getColor(R.color.nav_inactive));

        // Set listeners
        cvBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                setTabSelected(position);
            }
        });
    }


    /**
     * 设置Tab选中
     *
     * @param currentItem
     */
    private void setTabSelected(int currentItem) {
        if (mCurrentPosition != currentItem) {
            mCurrentPosition = currentItem;
            vpContent.setCurrentItem(currentItem, false);
            cvBottomNavigation.setCurrentItem(currentItem);
        }
    }

    /**
     * 更新未读数量控件位置
     *
     * @param view
     * @param vertialOffset
     * @param hotizontalOffset
     */
    private void updateUnreadViewPosition(View view, int vertialOffset, int hotizontalOffset) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = params.topMargin + vertialOffset;
        params.leftMargin = params.leftMargin + hotizontalOffset;
        view.setLayoutParams(params);   // 调用重绘
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从个人信息页返回需要重新加载当前用户的个人信息
        if (mIsDrawerHeaderViewCreated) {
            getMvpPresenter().doViewInitialized();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                LT.show("search");
                break;
            case R.id.menu_add:
                getMvpPresenter().doAdd();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_scan_qrcode:
                LT.show("scan qrcode");
                mAddPopupWindow.dismiss();
                break;
            case R.id.ll_add_friend:
                mAddPopupWindow.dismiss();
                getMvpPresenter().doAddFriend();
                break;
            case R.id.ll_add_group:
                LT.show("add group");
                mAddPopupWindow.dismiss();
                break;
            case R.id.cv_circle_avatar:
                getMvpPresenter().doAvatarClick();
                dlDrawer.closeDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_MENU){
            getMvpPresenter().doMenuKeyDown();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置底部通知数量
     * @param itemPosition
     * @param count
     */
    public void setBottomNotificationCount(int itemPosition, int count) {
        String countStr = count == 0 ? "" : count > 99 ? "99+" : count + "";
        cvBottomNavigation.setNotification(countStr, itemPosition);
    }

    // ---------------------------------- MVP View接口 ----------------------------------------------

    @Override
    public void showBottomSheet() {
        if (mBottomSheet == null){
            final BottomSheet.Builder builder = new BottomSheet.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet,null);
            NavigationView navigationView = (NavigationView) view.findViewById(R.id.nv_bottom_sheet);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.bs_check_update:
                            LT.show("check update");
                            break;
                        case R.id.bs_exit_app:
                            getMvpPresenter().doExitApp();
                            break;
                    }
                    mBottomSheet.dismiss();
                    return true;
                }
            });
            mBottomSheet = builder.setView(view).create();
            mBottomSheet.show();
        }else{
            if (!mBottomSheet.isShowing()){
                mBottomSheet.show();
            }
        }

    }

    @Override
    public void updateCurrentUserInfo(User currentUser) {
        mCurrentUser = currentUser;
        LL.d(currentUser);
        if (TextUtils.isEmpty(currentUser.getAvatar())) {
            cvCircleAvatar.setImageResource(R.mipmap.img_default_face);
        } else {
            Glide.with(this).load(currentUser.getAvatar()).centerCrop().into(cvCircleAvatar);
        }
        tvUsername.setText(String.format(LRUtils.getString(R.string.format_username), currentUser.getUsername()));
        tvNickname.setText(currentUser.getNickname());
        if (TextUtils.isEmpty(currentUser.getSignature())) {
            tvSignature.setText(LRUtils.getString(R.string.without_signature));
        } else {
            tvSignature.setText(currentUser.getSignature());
        }

        mMeFragment.updateCurrentUserInfo(currentUser);
    }

    @Override
    public void showSearchView() {
    }

    @Override
    public void showAddView() {
        mAddPopupWindow.showOnAnchor(toolbar, RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.RIGHT, -mAddPopupViewWidth - LDisplayUtils.dip2px(2), LDisplayUtils.dip2px(2));
    }

    @Override
    public void gotoLogin() {
        LPageUtils.startActivity(this, LoginActivity.class, true);
    }

    @Override
    public void gotoPersonalInfo() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PersonalInfoActivity.EXTRA_USER, mCurrentUser);
        LPageUtils.startActivity(this, PersonalInfoActivity.class, bundle, false);
    }

    @Override
    public void gotoAddFriend() {
        LPageUtils.startActivity(this, AddFriendActivity.class, false);
    }

    @Override
    public void exitApp() {
        finish();
    }


}
