package com.zeroami.youliao.data.handler;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMFileMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.youliao.bean.AudioMessage;
import com.zeroami.youliao.bean.FileMessage;
import com.zeroami.youliao.bean.ImageMessage;
import com.zeroami.youliao.bean.TextMessage;
import com.zeroami.youliao.bean.VideoMessage;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.UserManager;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天消息处理者</p>
 */
public class ChatMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        // 仅处理发送给当前用户的消息
        if (!client.getClientId().equals(UserManager.getInstance().getCurrentUserId())){
            return ;
        }
        switch (message.getMessageType()) {
            case -1:
                AVIMTextMessage textMsg = (AVIMTextMessage) message;
                LL.i("收到"+message.getFrom()+"的文本消息。conversationId="+conversation.getConversationId()+", messageId=" + textMsg.getMessageId() + ", text=" + textMsg.getText());
                LRxBus.getDefault().post(new TextMessage(conversation,textMsg), Constant.Action.RECEIVE_CHAT_MESSAGE);
                break;

            case -6:
                AVIMFileMessage fileMsg = (AVIMFileMessage) message;
                LL.i("收到"+message.getFrom()+"的文件消息。conversationId="+conversation.getConversationId()+", messageId=" + fileMsg.getMessageId() + ", url=" + fileMsg.getFileUrl() + ", size=" + fileMsg.getSize());
                LRxBus.getDefault().post(new FileMessage(conversation, fileMsg), Constant.Action.RECEIVE_CHAT_MESSAGE);
                break;

            case -2:
                AVIMImageMessage imageMsg = (AVIMImageMessage) message;
                LL.i("收到"+message.getFrom()+"的图片消息。conversationId="+conversation.getConversationId()+", messageId=" + imageMsg.getMessageId() + ", url=" + imageMsg.getFileUrl() + ", width=" + imageMsg.getWidth() + ", height=" + imageMsg.getHeight());
                LRxBus.getDefault().post(new ImageMessage(conversation, imageMsg), Constant.Action.RECEIVE_CHAT_MESSAGE);
                break;

            case -3:
                AVIMAudioMessage audioMsg = (AVIMAudioMessage) message;
                LL.i("收到"+message.getFrom()+ "的音频消息。conversationId="+conversation.getConversationId()+", messageId=" + audioMsg.getMessageId() + ", url=" + audioMsg.getFileUrl() + ", duration=" + audioMsg.getDuration());
                LRxBus.getDefault().post(new AudioMessage(conversation, audioMsg), Constant.Action.RECEIVE_CHAT_MESSAGE);
                break;

            case -4:
                AVIMVideoMessage videoMsg = (AVIMVideoMessage) message;
                LL.i("收到"+message.getFrom()+"的视频消息。conversationId="+conversation.getConversationId()+", messageId=" + videoMsg.getMessageId() + ", url=" + videoMsg.getFileUrl() + ", duration=" + videoMsg.getDuration());
                LRxBus.getDefault().post(new VideoMessage(conversation, videoMsg), Constant.Action.RECEIVE_CHAT_MESSAGE);
                break;

        }
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {

    }
}