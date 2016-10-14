package com.zeroami.youliao.bean;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.messages.AVIMFileMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：文件消息</p>
 */
public class FileMessage {
    private AVIMConversation conversation;
    private AVIMFileMessage fileMessage;

    public FileMessage() {
    }

    public FileMessage(AVIMConversation conversation, AVIMFileMessage fileMessage) {
        this.conversation = conversation;
        this.fileMessage = fileMessage;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public AVIMFileMessage getFileMessage() {
        return fileMessage;
    }

    public void setFileMessage(AVIMFileMessage fileMessage) {
        this.fileMessage = fileMessage;
    }
}
