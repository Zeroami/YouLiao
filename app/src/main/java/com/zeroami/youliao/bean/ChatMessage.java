package com.zeroami.youliao.bean;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天消息</p>
 */
public class ChatMessage implements MultiItemEntity {
    @Override
    public int getItemType() {
        return chatMessageType.ordinal();
    }

    public enum ChatMessageType {
        SEND,RECEIVE
    }
    private User sender;
    private long timestamp;
    private AVIMMessage message;
    private ChatMessageType chatMessageType;

    public ChatMessage() {
    }

    public ChatMessage(User sender, long timestamp, AVIMMessage message, ChatMessageType chatMessageType) {
        this.sender = sender;
        this.timestamp = timestamp;
        this.message = message;
        this.chatMessageType = chatMessageType;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender=" + sender +
                ", timestamp=" + timestamp +
                ", message=" + message +
                ", chatMessageType=" + chatMessageType +
                '}';
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }

    public ChatMessageType getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(ChatMessageType chatMessageType) {
        this.chatMessageType = chatMessageType;
    }
}
