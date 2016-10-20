package com.zeroami.youliao.presenter.fragment;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.youliao.bean.ChatMessage;
import com.zeroami.youliao.bean.Conversation;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.fragment.ConversationContract;
import com.zeroami.youliao.model.IChatModel;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.ChatModel;
import com.zeroami.youliao.model.real.FriendModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天会话Presenter</p>
 */
public class ConversationPresenter extends LBasePresenter<ConversationContract.View, IChatModel> implements ConversationContract.Presenter {

    private IFriendModel mFriendModel;
    private Comparator<Conversation> mComparator;

    public ConversationPresenter(ConversationContract.View view) {
        super(view);
        mFriendModel = new FriendModel();
        // 按时间戳从大到小排序
        mComparator = new Comparator<Conversation>() {
            @Override
            public int compare(Conversation c1, Conversation c2) {
                if (c1.getLastMessage().getTimestamp() > c2.getLastMessage().getTimestamp()) {
                    return -1;
                } else if (c1.getLastMessage().getTimestamp() < c2.getLastMessage().getTimestamp()) {
                    return 1;
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
        getMvpModel().loadConversations(new LeanCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> data) {
                Collections.sort(data, mComparator);
                getMvpView().updateConversationList(data);
                int count = 0;
                for (Conversation conversation : data) {
                    count += conversation.getMessageUnreadCount();
                }
                getMvpView().updateBottomNatificationCount(count);
            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().updateConversationList(new ArrayList<Conversation>());
            }
        });
    }

    @Override
    public void doReceiveUpdateConversation() {
        getMvpModel().loadConversation(getMvpView().getSendConversationId(), new LeanCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation data) {
                List<Conversation> list = new ArrayList<>(getMvpView().getConversationList());
                for (Conversation c : list) {
                    if (c.getConversationId().equals(data.getConversationId())) {
                        list.remove(c);
                        break;
                    }
                }
                list.add(0, data);
                getMvpView().updateConversationList(list);
                int count = 0;
                for (Conversation conversation : list) {
                    count += conversation.getMessageUnreadCount();
                }
                getMvpView().updateBottomNatificationCount(count);
            }

            @Override
            public void onError(int code, AVException e) {

            }
        });
    }

    @Override
    public void doReceiveMessage() {
        final AVIMConversation avimConversation = getMvpView().getReceiveConversation();
        final AVIMMessage avimMessage = getMvpView().getReceiveMessage();
        getMvpModel().increamentConversationUnreadCount(avimConversation.getConversationId());
        if (getMvpModel().isConversationIdSaved(avimConversation.getConversationId())) {
            mFriendModel.findFriendByObjectId(avimMessage.getFrom(), new LeanCallback<User>() {
                @Override
                public void onSuccess(User data) {
                    List<Conversation> list = new ArrayList<>(getMvpView().getConversationList());
                    for (Conversation conversation : list) {
                        if (conversation.getConversationId().equals(avimConversation.getConversationId())) {
                            conversation.setMessageUnreadCount(conversation.getMessageUnreadCount() + 1);
                            conversation.setLastMessage(new ChatMessage(data,avimMessage.getTimestamp(),avimMessage, ChatMessage.ChatMessageType.RECEIVE));
                        }
                    }
                    Collections.sort(list, mComparator);
                    getMvpView().updateConversationList(list);
                    int count = 0;
                    for (Conversation conversation : list){
                        count += conversation.getMessageUnreadCount();
                    }
                    getMvpView().updateBottomNatificationCount(count);
                }

                @Override
                public void onError(int code, AVException e) {

                }
            });
        } else {
            getMvpModel().createSingleConversation(avimMessage.getFrom(), new LeanCallback() {
                @Override
                public void onSuccess(Object data) {
                    getMvpModel().loadConversation(avimConversation.getConversationId(), new LeanCallback<Conversation>() {
                        @Override
                        public void onSuccess(Conversation data) {
                            List<Conversation> list = new ArrayList<>(getMvpView().getConversationList());
                            list.add(0, data);
                            getMvpView().updateConversationList(list);
                            int count = 0;
                            for (Conversation conversation : list){
                                count += conversation.getMessageUnreadCount();
                            }
                            getMvpView().updateBottomNatificationCount(count);
                        }

                        @Override
                        public void onError(int code, AVException e) {

                        }
                    });
                }

                @Override
                public void onError(int code, AVException e) {

                }
            });
        }

    }

    @Override
    public void doConversationItemClick() {
        List<Conversation> list = new ArrayList<>(getMvpView().getConversationList());
        for (Conversation c :list){
            if (c.getConversationId().equals(getMvpView().getClickConversation().getConversationId())){
                c.setMessageUnreadCount(0);
                break;
            }
        }
        getMvpView().updateConversationList(list);
        int count = 0;
        for (Conversation conversation : list){
            count += conversation.getMessageUnreadCount();
        }
        getMvpView().updateBottomNatificationCount(count);
        getMvpView().gotoChat();
    }

    @Override
    public void doDeleteConversation() {

    }
}
