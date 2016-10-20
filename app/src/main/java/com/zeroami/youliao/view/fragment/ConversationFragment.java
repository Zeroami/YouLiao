package com.zeroami.youliao.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.rx.rxbus.LRxBusSubscriber;
import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.youliao.R;
import com.zeroami.youliao.adapter.ConversationAdapter;
import com.zeroami.youliao.base.BaseMvpFragment;
import com.zeroami.youliao.bean.AudioMessage;
import com.zeroami.youliao.bean.Conversation;
import com.zeroami.youliao.bean.FileMessage;
import com.zeroami.youliao.bean.ImageMessage;
import com.zeroami.youliao.bean.TextMessage;
import com.zeroami.youliao.bean.VideoMessage;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.fragment.ConversationContract;
import com.zeroami.youliao.presenter.fragment.ConversationPresenter;
import com.zeroami.youliao.view.activity.ChatActivity;
import com.zeroami.youliao.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天会话</p>
 */
public class ConversationFragment extends BaseMvpFragment<ConversationContract.Presenter> implements ConversationContract.View {

    @Bind(R.id.rv_conversation)
    RecyclerView rvConversation;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Conversation> mConversationList;
    private ConversationAdapter mAdapter;
    private AVIMConversation mReceiveConversation;
    private AVIMMessage mReceiveMessage;
    private String mSendConversationId;
    private Conversation mClickConversation;

    public static ConversationFragment newInstance() {
        return new ConversationFragment();
    }

    @Override
    protected ConversationContract.Presenter createPresenter() {
        return new ConversationPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void initialize(View layoutView, Bundle savedInstanceState) {
        initRecycleView();
        initListener();
    }

    @Override
    protected void initializeRxBusListener() {
        addSubscription(LRxBus.getDefault().toObservable(String.class, Constant.Action.UPDATE_CONVERSATION)
                .subscribe(new LRxBusSubscriber<String>() {
                    @Override
                    protected void call(String conversationId) {
                        onReceiveUpdateConversation(conversationId);
                    }
                }));

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

    @Override
    protected void onInitialized() {
        getMvpPresenter().doViewInitialized();
    }

    private void initRecycleView() {
        mConversationList = new ArrayList<>();
        mAdapter = new ConversationAdapter(getAttachActivity(),mConversationList);
        rvConversation.setLayoutManager(new LinearLayoutManager(getAttachActivity()));
        rvConversation.setAdapter(mAdapter);
        rvConversation.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mClickConversation = mConversationList.get(i);
                getMvpPresenter().doConversationItemClick();
            }
        });
    }

    private void initListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMvpPresenter().doViewInitialized();
            }
        });
    }

    private void onReceiveUpdateConversation(String conversationId) {
        mSendConversationId = conversationId;
        getMvpPresenter().doReceiveUpdateConversation();
    }

    private void onReceiveTextMessage(TextMessage textMessage) {
        mReceiveConversation = textMessage.getConversation();
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
    public void updateConversationList(List<Conversation> conversationList) {
        if (!isViewDestoryed()){
            mConversationList.clear();
            mConversationList.addAll(conversationList);
            mAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public List<Conversation> getConversationList() {
        return mConversationList;
    }

    @Override
    public AVIMConversation getReceiveConversation() {
        return mReceiveConversation;
    }

    @Override
    public AVIMMessage getReceiveMessage() {
        return mReceiveMessage;
    }

    @Override
    public String getSendConversationId() {
        return mSendConversationId;
    }

    @Override
    public void updateBottomNatificationCount(int count) {
        ((MainActivity)getAttachActivity()).setBottomNotificationCount(0,count);
    }

    @Override
    public Conversation getClickConversation() {
        return mClickConversation;
    }

    @Override
    public void gotoChat() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChatActivity.EXTRA_USER, mClickConversation.getMembers().get(0));
        LPageUtils.startActivity(getAttachActivity(), ChatActivity.class, bundle, false);
    }
}
