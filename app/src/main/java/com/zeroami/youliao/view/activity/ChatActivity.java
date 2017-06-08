package com.zeroami.youliao.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.rx.rxbus.LRxBusSubscriber;
import com.zeroami.commonlib.utils.LInputMethodUtils;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.commonlib.utils.LUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.ChatMessageAdapter;
import com.zeroami.youliao.adapter.ExpressionPageAdapter;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.bean.AudioMessage;
import com.zeroami.youliao.bean.ChatMessage;
import com.zeroami.youliao.bean.FileMessage;
import com.zeroami.youliao.bean.ImageMessage;
import com.zeroami.youliao.bean.TextMessage;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.bean.VideoMessage;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.activity.ChatContract;
import com.zeroami.youliao.presenter.activity.ChatPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天页</p>
 */
public class ChatActivity extends BaseMvpActivity<ChatContract.Presenter> implements ChatContract.View, View.OnClickListener {

    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_IS_FROM_NEW_FRIEND = "extra_is_from_new_friend";

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tv_not_friend_tips)
    TextView tvNotFriendTips;
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
    @Bind(R.id.iv_file)
    ImageView ivFile;
    @Bind(R.id.fl_bottom_layout)
    FrameLayout flBottomLayout;
    @Bind(R.id.ll_expression_layout)
    LinearLayout llExpressionLayout;
    @Bind(R.id.vp_expression)
    ViewPager vpExpression;

    private User mUser;
    private boolean mIsFromNewFriend;
    private AVIMMessage mReceiveMessage;

    private List<ChatMessage> mChatMessageList;
    private ChatMessageAdapter mChatMessageAdapter;
    private LinearLayoutManager mLinearManager;

    @Override
    protected ChatContract.Presenter createPresenter() {
        return new ChatPresenter(this);
    }

    @Override
    protected void handleExtras(Bundle extras) {
        mUser = (User) extras.getSerializable(EXTRA_USER);
        mIsFromNewFriend = extras.getBoolean(EXTRA_IS_FROM_NEW_FRIEND,false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        initToolbar();
        initView();
        initListener();
        initRecyclerView();
        initExpressionViewPager();
    }



    @Override
    protected void subscribeRxBus() {
        addSubscription(LRxBus.getDefault().toObservable(TextMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<TextMessage>() {
                    @Override
                    protected void call(TextMessage textMessage) {
                        onReceiveTextMessage(textMessage);
                    }
                }));
        addSubscription(LRxBus.getDefault().toObservable(FileMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<FileMessage>() {
                    @Override
                    protected void call(FileMessage fileMessage) {
                        onReceiveFileMessage(fileMessage);
                    }
                }));
        addSubscription(LRxBus.getDefault().toObservable(ImageMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<ImageMessage>() {
                    @Override
                    protected void call(ImageMessage imageMessage) {
                        onReceiveImageMessage(imageMessage);
                    }
                }));
        addSubscription(LRxBus.getDefault().toObservable(AudioMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<AudioMessage>() {
                    @Override
                    protected void call(AudioMessage audioMessage) {
                        onReceiveAudioMessage(audioMessage);
                    }
                }));
        addSubscription(LRxBus.getDefault().toObservable(VideoMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<VideoMessage>() {
                    @Override
                    protected void call(VideoMessage videoMessage) {
                        onReceiveVideoMessage(videoMessage);
                    }
                }));
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
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
        if (mIsFromNewFriend){
            etChatMessage.setText(LRUtils.getString(R.string.new_friend_default_message));
        }
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMvpPresenter().doLoadMoreHistoryMessages();
            }
        });
        tvSend.setOnClickListener(this);
        ivExpression.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        ivFile.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mChatMessageList = new ArrayList<>();
        mChatMessageAdapter = new ChatMessageAdapter(this,mChatMessageList);
        mLinearManager = new LinearLayoutManager(this);
        rvMessage.setLayoutManager(mLinearManager);
        rvMessage.setAdapter(mChatMessageAdapter);
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
        for (int i=0;i< Constant.EXPRESSION_TOTAL_COUNT;i++){
            expressionIdList.add(LRUtils.getDrawableId(Constant.EXPRESSION_PREFIX + i));
            if ((i+1) % (Constant.EXPRESSION_COL_SIZE * Constant.EXPRESSION_ROW_SIZE) == 0){
                expressionPageList.add(expressionIdList);
                expressionIdList = new ArrayList<>();
            }
        }
        // 如果最后一页不完整，将最后一页添加
        if (expressionIdList.size() != 0){
            expressionPageList.add(expressionIdList);
        }
        vpExpression.setAdapter(new ExpressionPageAdapter(this, expressionPageList));
    }

    private void onReceiveTextMessage(TextMessage textMessage) {
        mReceiveMessage = textMessage.getTextMessage();
        getMvpPresenter().doReceiveMessage();
    }

    private void onReceiveFileMessage(FileMessage fileMessage) {
        LT.show("onReceiveFileMessage");
    }

    private void onReceiveImageMessage(ImageMessage imageMessage) {
        LT.show("onReceiveImageMessage");
    }

    private void onReceiveAudioMessage(AudioMessage audioMessage) {
        LT.show("onReceiveAudioMessage");
    }

    private void onReceiveVideoMessage(VideoMessage videoMessage) {
        LT.show("onReceiveVideoMessage");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_send:
                getMvpPresenter().doSendMessage();
                break;
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
            case R.id.iv_file:
                LT.show("file");
                break;
        }
    }

    @Override
    public User getUser() {
        return mUser;
    }

    @Override
    public boolean isFromNewFriend() {
        return mIsFromNewFriend;
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
        LInputMethodUtils.hideInputMethod(etChatMessage);
    }

    @Override
    public void clearEditTextFocus() {
        etChatMessage.clearFocus();
    }

    @Override
    public void clearEditText() {
        etChatMessage.setText("");
    }

    @Override
    public String getSendMessage() {
        return etChatMessage.getText().toString();
    }

    @Override
    public void prependChatMessageList(List<ChatMessage> chatMessageList) {
        mChatMessageList.addAll(0, chatMessageList);
        mChatMessageAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        rvMessage.scrollToPosition(chatMessageList.size() - 1);
    }

    @Override
    public void appendChatMessage(ChatMessage chatMessage) {
        mChatMessageList.add(chatMessage);
        mChatMessageAdapter.notifyDataSetChanged();
        rvMessage.scrollToPosition(mChatMessageList.size()-1);
    }

    @Override
    public AVIMMessage getReceiveMessage() {
        return mReceiveMessage;
    }

    @Override
    public AVIMMessage getFirstMessage() {
        if (mChatMessageList.size() > 0){
            return mChatMessageList.get(0).getMessage();
        }
        return null;
    }

    @Override
    public void showNotFriendTips() {
        tvNotFriendTips.setVisibility(View.VISIBLE);
    }
}
