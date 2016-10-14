package com.zeroami.youliao.bean;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：视频消息</p>
 */
public class VideoMessage {
    private AVIMConversation conversation;
    private AVIMVideoMessage videoMessage;

    public VideoMessage() {
    }

    public VideoMessage(AVIMConversation conversation, AVIMVideoMessage videoMessage) {
        this.conversation = conversation;
        this.videoMessage = videoMessage;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public AVIMVideoMessage getVideoMessage() {
        return videoMessage;
    }

    public void setVideoMessage(AVIMVideoMessage videoMessage) {
        this.videoMessage = videoMessage;
    }
}
