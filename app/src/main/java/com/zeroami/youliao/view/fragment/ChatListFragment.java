package com.zeroami.youliao.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.rx.rxbus.LRxBusSubscriber;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpFragment;
import com.zeroami.youliao.bean.AudioMessage;
import com.zeroami.youliao.bean.FileMessage;
import com.zeroami.youliao.bean.ImageMessage;
import com.zeroami.youliao.bean.TextMessage;
import com.zeroami.youliao.bean.VideoMessage;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.fragment.ChatListContract;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天列表</p>
 */
public class ChatListFragment extends BaseMvpFragment<ChatListContract.Presenter> implements ChatListContract.View {

    public static ChatListFragment newInstance(){
        return new ChatListFragment();
    }

    @Override
    protected ChatListContract.Presenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initialize(View layoutView, Bundle savedInstanceState) {
        initRxBusListener();
    }

    private void initRxBusListener() {
        LRxBus.getDefault().toObservable(TextMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<TextMessage>() {
                    @Override
                    protected void call(TextMessage textMessage) {
                        onReceiveTextMessage(textMessage);
                    }
                });
        LRxBus.getDefault().toObservable(FileMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<FileMessage>() {
                    @Override
                    protected void call(FileMessage fileMessage) {
                        onReceiveFileMessage(fileMessage);
                    }
                });
        LRxBus.getDefault().toObservable(ImageMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<ImageMessage>() {
                    @Override
                    protected void call(ImageMessage imageMessage) {
                        onReceiveImageMessage(imageMessage);
                    }
                });
        LRxBus.getDefault().toObservable(AudioMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<AudioMessage>() {
                    @Override
                    protected void call(AudioMessage audioMessage) {
                        onReceiveAudioMessage(audioMessage);
                    }
                });
        LRxBus.getDefault().toObservable(VideoMessage.class, Constant.Action.RECEIVE_CHAT_MESSAGE)
                .subscribe(new LRxBusSubscriber<VideoMessage>() {
                    @Override
                    protected void call(VideoMessage videoMessage) {
                        onReceiveVideoMessage(videoMessage);
                    }
                });
    }

    private void onReceiveTextMessage(TextMessage textMessage) {
        LT.show("onReceiveTextMessage");
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

}
