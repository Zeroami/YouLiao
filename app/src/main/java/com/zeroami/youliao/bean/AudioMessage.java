package com.zeroami.youliao.bean;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：音频消息</p>
 */
public class AudioMessage {
    private AVIMConversation conversation;
    private AVIMAudioMessage audioMessage;

    public AudioMessage() {
    }

    public AudioMessage(AVIMConversation conversation, AVIMAudioMessage audioMessage) {
        this.conversation = conversation;
        this.audioMessage = audioMessage;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }

    public AVIMAudioMessage getAudioMessage() {
        return audioMessage;
    }

    public void setAudioMessage(AVIMAudioMessage audioMessage) {
        this.audioMessage = audioMessage;
    }
}
