package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.commonlib.utils.LUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.ChatMessageAdapter;
import com.zeroami.youliao.adapter.ExpressionPageAdapter;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.ChatMessage;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.activity.ChatContract;
import com.zeroami.youliao.presenter.activity.ChatPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChatActivity extends BaseMvpActivity<ChatContract.Presenter> implements ChatContract.View, View.OnClickListener {

    public static final String EXTRA_USER = "extra_user";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.rv_message)
    RecyclerView rvMessage;
    @Bind(R.id.et_chat_message)
    EditText etChatMessage;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.iv_expression)
    ImageView ivExpression;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.iv_voice)
    ImageView ivVoice;
    @Bind(R.id.iv_video)
    ImageView ivVideo;
    @Bind(R.id.fl_bottom_layout)
    FrameLayout flBottomLayout;
    @Bind(R.id.ll_expression_layout)
    LinearLayout llExpressionLayout;
    @Bind(R.id.vp_expression)
    ViewPager vpExpression;

    private User mUser;

    private List<ChatMessage> mChatMessageList;
    private ChatMessageAdapter mChatMessageAdapter;

    @Override
    protected ChatContract.Presenter createPresenter() {
        return new ChatPresenter(this);
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
        initView();
        initListener();
        initRecyclerView();
        initExpressionViewPager();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");
        tvTitle.setText(mUser.getNickname());
        setSupportActionBar(toolbar);
    }

    private void initView() {
        etChatMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    getMvpPresenter().doEditTextGetFocus();
                }
            }
        });
    }

    private void initListener() {
        ivExpression.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        tvSend.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mChatMessageList = new ArrayList<>();
        for (int i=0;i<10;i++){
            if (i % 3 == 0){
                mChatMessageList.add(new ChatMessage(null,0,null, ChatMessage.ChatMessageType.SEND));
            }else{
                mChatMessageList.add(new ChatMessage(null,0,null, ChatMessage.ChatMessageType.RECEIVE));
            }
        }
        mChatMessageAdapter = new ChatMessageAdapter(this,mChatMessageList);
        rvMessage.setLayoutManager(new LinearLayoutManager(this));
        rvMessage.setAdapter(mChatMessageAdapter);
        rvMessage.scrollToPosition(10);
        rvMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                getMvpPresenter().doOutsideClick();
                return false;
            }
        });
    }

    private void initExpressionViewPager() {
        List<List<Integer>> expressionPageList = new ArrayList<>();
        List<Integer> expressionIdList = new ArrayList<>();
        for (int i=0;i< Constant.EXPRESS_TOTAL_COUNT;i++){
            expressionIdList.add(LRUtils.getMipmapId(Constant.EXPRESS_PREFIX + i));
            if ((i+1) % (Constant.EXPRESS_COL_SIZE * Constant.EXPRESS_ROW_SIZE) == 0){
                expressionPageList.add(expressionIdList);
                expressionIdList = new ArrayList<>();
            }
        }
        // 如果最后一页不完整，将最后一页添加
        if (expressionIdList.size() != 0){
            expressionPageList.add(expressionIdList);
        }
        vpExpression.setAdapter(new ExpressionPageAdapter(this,expressionPageList));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_expression:
                getMvpPresenter().doExpressionClick();
                break;
            case R.id.iv_photo:
                LT.show("photo");
                break;
            case R.id.iv_voice:
                LT.show("voice");
                break;
            case R.id.iv_video:
                LT.show("video");
                break;
            case R.id.tv_send:
                LT.show("send");
                break;
        }
    }

    @Override
    public void showBottomLayout() {
        flBottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomLayout() {
        flBottomLayout.setVisibility(View.GONE);
    }

    @Override
    public void showExpressionLayout() {
        llExpressionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideExpressionLayout() {
        llExpressionLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideInputMethod() {
        LUtils.hideInputMethod(etChatMessage);
    }

    @Override
    public void clearEditTextFocus() {
        etChatMessage.clearFocus();
    }
}
