package com.zeroami.youliao.data.http;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
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
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_GROUP = 1;
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
        attrs.put(TYPE, TYPE_SINGLE);
        StorageUtils.getAVIMClient().createConversation(Arrays.asList(memberId), "", attrs, false, true, callback);
    }

    public void createGroupConversation(List<String> menberIds, AVIMConversationCreatedCallback callback) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(TYPE, TYPE_GROUP);
        StorageUtils.getAVIMClient().createConversation(menberIds, "", attrs, false, true, callback);
    }

    public void findGroupConversationsIncludeMe(AVIMConversationQueryCallback callback) {
        AVIMConversationQuery conversationQuery = StorageUtils.getAVIMClient().getQuery();
        if (conversationQuery != null) {
            conversationQuery.containsMembers(Arrays.asList(UserManager.getInstance().getCurrentUserId()));
            conversationQuery.whereEqualTo(ATTR_TYPE_KEY, TYPE_GROUP);
            conversationQuery.orderByDescending(AVObject.UPDATED_AT);
            conversationQuery.limit(1000);
            conversationQuery.findInBackground(callback);
        } else if (callback != null) {
            callback.done(new ArrayList<AVIMConversation>(), null);
        }
    }

    public void sendMessage(AVIMConversation avimConversation,String text,AVIMConversationCallback avimConversationCallback){
        AVIMTextMessage avimTextMessage = new AVIMTextMessage();
        avimTextMessage.setText(text);
        avimConversation.sendMessage(avimTextMessage,avimConversationCallback);
    }

}
