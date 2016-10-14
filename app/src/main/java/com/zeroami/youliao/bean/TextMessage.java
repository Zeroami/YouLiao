package com.zeroami.youliao.bean;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：文本消息</p>
 */
public class TextMessage {
    private AVIMConversation conversation;
    private AVIMTextMessage textMessage;

    public TextMessage() {
    }

    public TextMessage(AVIMConversation conversation, AVIMTextMessage textMessage) {
        this.conversation = conversation;
        this.textMessage = textMessage;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public AVIMTextMessage getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(AVIMTextMessage textMessage) {
        this.textMessage = textMessage;
    }
}
