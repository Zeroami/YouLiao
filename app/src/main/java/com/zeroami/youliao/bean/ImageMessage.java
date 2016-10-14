package com.zeroami.youliao.bean;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：图片消息</p>
 */
public class ImageMessage {
    private AVIMConversation conversation;
    private AVIMImageMessage imageMessage;

    public ImageMessage() {
    }

    public ImageMessage(AVIMConversation conversation, AVIMImageMessage imageMessage) {
        this.conversation = conversation;
        this.imageMessage = imageMessage;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public AVIMImageMessage getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(AVIMImageMessage imageMessage) {
        this.imageMessage = imageMessage;
    }
}
