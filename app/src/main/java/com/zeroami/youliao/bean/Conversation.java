package com.zeroami.youliao.bean;

import com.avos.avoscloud.im.v2.AVIMMessage;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天会话</p>
 */
public class Conversation {

    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_GROUP = 1;

    private String conversationId;
    private String conversationName;
    private List<User> members;
    private ChatMessage lastMessage;
    private int messageUnreadCount;
    private int conversationType;

    public Conversation() {
    }

    public Conversation(String conversationId,String conversationName, List<User> members, ChatMessage lastMessage, int messageUnreadCount,int conversationType) {
        this.conversationId = conversationId;
        this.conversationName = conversationName;
        this.members = members;
        this.lastMessage = lastMessage;
        this.messageUnreadCount = messageUnreadCount;
        this.conversationType = conversationType;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "conversationId='" + conversationId + '\'' +
                ", conversationName='" + conversationName + '\'' +
                ", members=" + members +
                ", lastMessage=" + lastMessage +
                ", messageUnreadCount=" + messageUnreadCount +
                ", conversationType=" + conversationType +
                '}';
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getMessageUnreadCount() {
        return messageUnreadCount;
    }

    public void setMessageUnreadCount(int messageUnreadCount) {
        this.messageUnreadCount = messageUnreadCount;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }
}
