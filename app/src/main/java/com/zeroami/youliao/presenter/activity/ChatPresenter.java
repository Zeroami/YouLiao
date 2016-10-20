package com.zeroami.youliao.presenter.activity;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LNetUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.ChatMessage;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.activity.ChatContract;
import com.zeroami.youliao.model.IChatModel;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.ChatModel;
import com.zeroami.youliao.model.real.FriendModel;
import com.zeroami.youliao.model.real.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天Presenter</p>
 */
public class ChatPresenter extends LBasePresenter<ChatContract.View,IChatModel> implements ChatContract.Presenter {

    private IUserModel mUserModel;
    private IFriendModel mFriendModel;

    private Comparator<ChatMessage> mComparator;

    public ChatPresenter(ChatContract.View view) {
        super(view);
        mUserModel = new UserModel();
        mFriendModel = new FriendModel();
        // 按时间戳从小到大排序
        mComparator = new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage c1, ChatMessage c2) {
                if (c1.getMessage().getTimestamp() > c2.getMessage().getTimestamp()) {
                    return 1;
                } else if (c1.getMessage().getTimestamp() < c2.getMessage().getTimestamp()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
    }

    @Override
    protected IChatModel getRealModel() {
        return new ChatModel();
    }

    @Override
    protected IChatModel getTestModel() {
        return null;
    }

    @Override
    public void doViewInitialized() {
        mFriendModel.findFriendByObjectId(getMvpView().getUser().getObjectId(), new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data == null){
                    getMvpView().showNotFriendTips();
                }
            }

            @Override
            public void onError(int code, AVException e) {

            }
        });
        getMvpModel().createSingleConversation(getMvpView().getUser().getObjectId(), new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                getMvpModel().markConversationRead();
                if (getMvpView().isFromNewFriend()){
                    doSendMessage();
                }else {
                    // 加载历史消息记录
                    loadHistoryMessages("", 0, Constant.INIT_SIZE);
                }
            }

            @Override
            public void onError(int code, AVException e) {

            }
        });
    }


    @Override
    public void doExpressionClick() {
        getMvpView().hideInputMethod();
        getMvpView().showBottomLayout();
        getMvpView().showExpressionLayout();
        getMvpView().clearEditTextFocus();
    }

    @Override
    public void doEditTextGetFocus() {
        getMvpView().hideBottomLayout();
        getMvpView().hideExpressionLayout();
    }

    @Override
    public void doOutsideClick() {
        resetViews();
    }

    @Override
    public void doSendMessage() {
        String text = getMvpView().getSendMessage();

        if (TextUtils.isEmpty(text)){
            getMvpView().showMessage(LRUtils.getString(R.string.input_empry_error));
            return;
        }

        if (!LNetUtils.isNetworkConnected()){
            getMvpView().showMessage(LRUtils.getString(R.string.please_check_network));
            return;
        }

        AVIMTextMessage avimTextMessage = new AVIMTextMessage();
        avimTextMessage.setText(text);
        final ChatMessage chatMessage = new ChatMessage(mUserModel.getCurrentUser(),System.currentTimeMillis(), avimTextMessage, ChatMessage.ChatMessageType.SEND);
        getMvpModel().sendMessage(text, new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                getMvpView().appendChatMessage(chatMessage);
                resetViews();
                getMvpView().clearEditText();

            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().showMessage(LRUtils.getString(R.string.send_failure));
            }
        });
    }

    @Override
    public void doReceiveMessage() {
        AVIMMessage avimMessage = getMvpView().getReceiveMessage();
        getMvpView().appendChatMessage(new ChatMessage(getMvpView().getUser(), avimMessage.getTimestamp(), avimMessage, ChatMessage.ChatMessageType.RECEIVE));
    }

    @Override
    public void doLoadMoreHistoryMessages() {
        AVIMMessage avimMessage = getMvpView().getFirstMessage();
        if (avimMessage == null){
            getMvpView().prependChatMessageList(new ArrayList<ChatMessage>());
        }else{
            loadHistoryMessages(avimMessage.getMessageId(),avimMessage.getTimestamp(),Constant.PAGE_SIZE);
        }
    }


    /**
     * 重置Views
     */
    private void resetViews() {
        getMvpView().clearEditTextFocus();
        getMvpView().hideInputMethod();
        getMvpView().hideBottomLayout();
        getMvpView().hideExpressionLayout();
    }


    /**
     * 加载历史消息记录
     * @param messageId
     * @param timestamp
     * @param limit
     */
    private void loadHistoryMessages(String messageId,long timestamp,int limit){
        getMvpModel().loadHistoryMessages(messageId, timestamp, limit, new LeanCallback<List<ChatMessage>>() {
            @Override
            public void onSuccess(List<ChatMessage> data) {
                Collections.sort(data,mComparator);
                LL.d(data);
                getMvpView().prependChatMessageList(data);
            }

            @Override
            public void onError(int code, AVException e) {

            }
        });
    }
}
