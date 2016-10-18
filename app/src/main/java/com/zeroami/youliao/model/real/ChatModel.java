package com.zeroami.youliao.model.real;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LThreadUtils;
import com.zeroami.youliao.bean.ChatMessage;
import com.zeroami.youliao.bean.Conversation;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.ChatManager;
import com.zeroami.youliao.data.http.UserManager;
import com.zeroami.youliao.data.local.SPManager;
import com.zeroami.youliao.model.IChatModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.callback.SuccessBeforeCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：ChatModel，管理聊天相关信息</p>
 */
public class ChatModel extends BaseModel implements IChatModel {

    private ChatManager mChatManager;
    private UserManager mUserManager;
    private SPManager mSPManager;
    private AVIMConversation mAVIMConversation;

    public ChatModel() {
        mChatManager = ChatManager.getInstance();
        mUserManager = UserManager.getInstance();
        mSPManager = SPManager.getInstance();
    }

    @Override
    public void createSingleConversation(final String memberId, final LeanCallback callback) {
        final String conversationId = mSPManager.getConversationIdByMemberId(memberId);
        if (!TextUtils.isEmpty(conversationId)) {
            mAVIMConversation = mChatManager.getConversationById(conversationId);
            callback.onSuccess(null);
        } else {
            mChatManager.createSingleConversation(memberId, new AVIMConversationCreatedCallback() {
                @Override
                public void done(final AVIMConversation avimConversation, AVIMException e) {
                    mAVIMConversation = avimConversation;
                    mSPManager.saveConversationIdByMemberId(memberId, avimConversation.getConversationId());
                    mSPManager.saveConversationId(avimConversation.getConversationId());
                    handleCallback(null, e, callback, null);
                }
            });
        }
    }

    @Override
    public void sendMessage(String text, final LeanCallback callback) {
        mChatManager.sendMessage(mAVIMConversation, text, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                handleCallback(null, e, callback, new SuccessBeforeCallback<Object>() {
                    @Override
                    public void call(Object data) {
                        // 发送一个事件告诉自己去更新聊天会话
                        LRxBus.getDefault().post(mAVIMConversation.getConversationId(), Constant.Action.UPDATE_CONVERSATION);
                    }
                });
            }
        });
    }

    @Override
    public void loadHistoryMessages(final String messageId, long timestamp, final int limit, final LeanCallback callback) {
        AVIMMessagesQueryCallback avimMessagesQueryCallback = new AVIMMessagesQueryCallback() {
            @Override
            public void done(final List<AVIMMessage> list, AVIMException e) {
                if (callback != null) {
                    if (e == null) {
                        LThreadUtils.doInBackground(new Runnable() {
                            @Override
                            public void run() {
                                final List<ChatMessage> chatMessageList = new ArrayList<>();
                                final CountDownLatch countDownLatch = new CountDownLatch(list.size());
                                for (final AVIMMessage avimMessage : list) {
                                    if (avimMessage.getFrom().equals(mUserManager.getCurrentUserId())) {     // 发送者是本人
                                        User sender = mSPManager.getCurrentUser();
                                        chatMessageList.add(new ChatMessage(sender, avimMessage.getTimestamp(), avimMessage, ChatMessage.ChatMessageType.SEND));
                                        countDownLatch.countDown();
                                    } else {
                                        mUserManager.findUserByObectId(avimMessage.getFrom(), new FindCallback<AVUser>() {
                                            @Override
                                            public void done(List<AVUser> list, AVException e) {
                                                if (e == null && list.size() > 0) {
                                                    User sender = User.convertToUser(list.get(0));
                                                    chatMessageList.add(new ChatMessage(sender, avimMessage.getTimestamp(), avimMessage, ChatMessage.ChatMessageType.RECEIVE));
                                                }
                                                countDownLatch.countDown();
                                            }
                                        });
                                    }
                                }

                                try {
                                    countDownLatch.await();
                                    LThreadUtils.doInUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onSuccess(chatMessageList);
                                        }
                                    });
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                    } else {
                        LL.e(e);
                        callback.onError(e.getCode(), e);
                    }
                }
            }
        };
        if (TextUtils.isEmpty(messageId)) {
            mChatManager.queryMessages(mAVIMConversation, limit, avimMessagesQueryCallback);
        } else {
            mChatManager.queryMessages(mAVIMConversation, messageId, timestamp - 1, limit, avimMessagesQueryCallback);
        }
    }

    @Override
    public void loadConversations(final LeanCallback callback) {
        final List<String> conversationIds = mSPManager.getConversationIds();
        if (conversationIds.size() == 0) {
            callback.onSuccess(new ArrayList<>());
        } else {
            LThreadUtils.doInBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        final List<Conversation> conversationList = new ArrayList<>();
                        for (String conversationId : conversationIds) {
                            final Conversation conversation = new Conversation();
                            AVIMConversation avimConversation = mChatManager.getConversationById(conversationId);
                            LL.d(avimConversation.getMembers());
                            final List<User> members = new ArrayList<>();
                            final CountDownLatch countDownLatch = new CountDownLatch(avimConversation.getMembers().size() + 1);
                            for (String memberId : avimConversation.getMembers()) {
                                if(memberId.equals(mUserManager.getCurrentUserId())){
                                    countDownLatch.countDown();
                                    continue;
                                }
                                mUserManager.findUserByObectId(memberId, new FindCallback<AVUser>() {
                                    @Override
                                    public void done(List<AVUser> list, AVException e) {
                                        if (e == null && list.size() > 0) {
                                            members.add(User.convertToUser(list.get(0)));
                                        }
                                        countDownLatch.countDown();
                                    }
                                });
                            }
                            avimConversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
                                @Override
                                public void done(final AVIMMessage avimMessage, AVIMException e) {
                                    if (avimMessage.getFrom().equals(mUserManager.getCurrentUserId())) {     // 发送者是本人
                                        User sender = mSPManager.getCurrentUser();
                                        conversation.setLastMessage(new ChatMessage(sender, avimMessage.getTimestamp(), avimMessage, ChatMessage.ChatMessageType.SEND));
                                        countDownLatch.countDown();
                                    } else {
                                        mUserManager.findUserByObectId(avimMessage.getFrom(), new FindCallback<AVUser>() {
                                            @Override
                                            public void done(List<AVUser> list, AVException e) {
                                                if (e == null && list.size() > 0) {
                                                    User sender = User.convertToUser(list.get(0));
                                                    conversation.setLastMessage(new ChatMessage(sender, avimMessage.getTimestamp(), avimMessage, ChatMessage.ChatMessageType.RECEIVE));
                                                }
                                                countDownLatch.countDown();
                                            }
                                        });
                                    }
                                }
                            });
                            countDownLatch.await();
                            conversation.setConversationId(conversationId);
                            conversation.setMembers(members);
                            conversation.setMessageUnreadCount(mSPManager.getConversationUnreadCount(conversationId));
                            conversation.setConversationType((Integer) avimConversation.getAttribute(ChatManager.TYPE));
                            conversation.setConversationName(members.get(0).getNickname());
                            conversationList.add(conversation);
                        }
                        LThreadUtils.doInUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(conversationList);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean isConversationIdSaved(String conversationId) {
        return mSPManager.getConversationIds().contains(conversationId);
    }

    @Override
    public void loadConversation(final String conversationId, final LeanCallback callback) {
        LThreadUtils.doInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    final Conversation conversation = new Conversation();
                    AVIMConversation avimConversation = mChatManager.getConversationById(conversationId);
                    final List<User> members = new ArrayList<>();
                    final CountDownLatch countDownLatch = new CountDownLatch(avimConversation.getMembers().size() + 1);
                    for (String memberId : avimConversation.getMembers()) {
                        if(memberId.equals(mUserManager.getCurrentUserId())){
                            countDownLatch.countDown();
                            continue;
                        }
                        mUserManager.findUserByObectId(memberId, new FindCallback<AVUser>() {
                            @Override
                            public void done(List<AVUser> list, AVException e) {
                                if (e == null && list.size() > 0) {
                                    members.add(User.convertToUser(list.get(0)));
                                }
                                countDownLatch.countDown();
                            }
                        });
                    }
                    avimConversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
                        @Override
                        public void done(final AVIMMessage avimMessage, AVIMException e) {
                            if (avimMessage.getFrom().equals(mUserManager.getCurrentUserId())) {     // 发送者是本人
                                User sender = mSPManager.getCurrentUser();
                                conversation.setLastMessage(new ChatMessage(sender, avimMessage.getTimestamp(), avimMessage, ChatMessage.ChatMessageType.SEND));
                                countDownLatch.countDown();
                            } else {
                                mUserManager.findUserByObectId(avimMessage.getFrom(), new FindCallback<AVUser>() {
                                    @Override
                                    public void done(List<AVUser> list, AVException e) {
                                        if (e == null && list.size() > 0) {
                                            User sender = User.convertToUser(list.get(0));
                                            conversation.setLastMessage(new ChatMessage(sender, avimMessage.getTimestamp(), avimMessage, ChatMessage.ChatMessageType.RECEIVE));
                                        }
                                        countDownLatch.countDown();
                                    }
                                });
                            }
                        }
                    });
                    countDownLatch.await();
                    conversation.setConversationId(conversationId);
                    conversation.setMembers(members);
                    conversation.setMessageUnreadCount(mSPManager.getConversationUnreadCount(conversationId));
                    conversation.setConversationType((Integer) avimConversation.getAttribute(ChatManager.TYPE));
                    conversation.setConversationName(members.get(0).getNickname());
                    LThreadUtils.doInUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(conversation);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void increamentConversationUnreadCount(String conversationId) {
        mSPManager.increamentConversationUnreadCount(conversationId);
    }

    @Override
    public void markConversationRead(String conversationId) {
        mSPManager.markConversationRead(conversationId);
    }
}
