package com.zeroami.youliao.data.http;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zeroami.youliao.bean.Conversation;
import com.zeroami.youliao.utils.StorageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天管理者</p>
 */
public class ChatManager {

    public static final String TYPE = "type";
    public static final String ATTR_TYPE_KEY = "attr.type";

    private static volatile ChatManager sInstance;

    private ChatManager() {
    }

    public static ChatManager getInstance() {
        if (sInstance == null) {
            synchronized (ChatManager.class) {
                if (sInstance == null) {
                    sInstance = new ChatManager();
                }
            }
        }
        return sInstance;
    }

    public void createSingleConversation(String memberId, AVIMConversationCreatedCallback callback) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(TYPE, Conversation.TYPE_SINGLE);
        AVIMClient.getInstance(UserManager.getInstance().getCurrentUserId()).createConversation(Arrays.asList(memberId), "", attrs, false, true, callback);
    }

    public void createGroupConversation(List<String> menberIds, AVIMConversationCreatedCallback callback) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(TYPE, Conversation.TYPE_GROUP);
        AVIMClient.getInstance(UserManager.getInstance().getCurrentUserId()).createConversation(menberIds, "", attrs, false, true, callback);
    }

    public void findGroupConversationsIncludeMe(AVIMConversationQueryCallback callback) {
        AVIMConversationQuery conversationQuery = AVIMClient.getInstance(UserManager.getInstance().getCurrentUserId()).getQuery();
        if (conversationQuery != null) {
            conversationQuery.containsMembers(Arrays.asList(UserManager.getInstance().getCurrentUserId()));
            conversationQuery.whereEqualTo(ATTR_TYPE_KEY, Conversation.TYPE_GROUP);
            conversationQuery.orderByDescending(AVObject.UPDATED_AT);
            conversationQuery.limit(1000);
            conversationQuery.findInBackground(callback);
        } else if (callback != null) {
            callback.done(new ArrayList<AVIMConversation>(), null);
        }
    }

    public AVIMConversation getConversationById(String conversationId){
        return AVIMClient.getInstance(UserManager.getInstance().getCurrentUserId()).getConversation(conversationId);
    }

    public void sendMessage(AVIMConversation avimConversation,String text,AVIMConversationCallback avimConversationCallback){
        AVIMTextMessage avimTextMessage = new AVIMTextMessage();
        avimTextMessage.setText(text);
        avimConversation.sendMessage(avimTextMessage,avimConversationCallback);
    }

    /**
     * 查询第一页历史消息
     * @param avimConversation
     * @param limit
     * @param avimMessagesQueryCallback
     */
    public void queryMessages(AVIMConversation avimConversation,int limit,AVIMMessagesQueryCallback avimMessagesQueryCallback){
        avimConversation.queryMessages(limit, avimMessagesQueryCallback);
    }

    /**
     * 分页查询历史消息
     * @param avimConversation
     * @param messageId
     * @param timestamp
     * @param limit
     * @param avimMessagesQueryCallback
     */
    public void queryMessages(AVIMConversation avimConversation,String messageId,long timestamp,int limit,AVIMMessagesQueryCallback avimMessagesQueryCallback){
        avimConversation.queryMessages(messageId,timestamp,limit, avimMessagesQueryCallback);
    }

}
