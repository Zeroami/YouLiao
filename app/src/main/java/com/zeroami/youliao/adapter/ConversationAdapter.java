package com.zeroami.youliao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMFileMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.Conversation;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天会话Adapter</p>
 */
public class ConversationAdapter extends BaseQuickAdapter<Conversation> {
    private Context mContext;

    public ConversationAdapter(Context context, List<Conversation> data) {
        super(R.layout.item_conversation, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Conversation conversation) {
        baseViewHolder.setText(R.id.tv_conversation_name, conversation.getConversationName());
        int unreadCount = conversation.getMessageUnreadCount();
        if (unreadCount > 0) {
            baseViewHolder.setVisible(R.id.tv_message_unread, true)
                    .setText(R.id.tv_message_unread, unreadCount + "");
        } else {
            baseViewHolder.setVisible(R.id.tv_message_unread, false);
        }
        LCircleImageView cvCircleAvatar = baseViewHolder.getView(R.id.cv_circle_avatar);
        if (conversation.getConversationType() == Conversation.TYPE_SINGLE) {
            if (TextUtils.isEmpty(conversation.getMembers().get(0).getAvatar())) {
                cvCircleAvatar.setImageResource(R.drawable.img_default_face);
            } else {
                Glide.with(mContext).load(conversation.getMembers().get(0).getAvatar()).centerCrop().into(cvCircleAvatar);
            }
        } else {

        }
        if (conversation.getLastMessage().getMessage() instanceof AVIMTextMessage) {
            baseViewHolder.setText(R.id.tv_last_message,
                    String.format(LRUtils.getString(R.string.format_last_message), conversation.getLastMessage().getSender().getNickname(), ((AVIMTextMessage) conversation.getLastMessage().getMessage()).getText()));
        } else if (conversation.getLastMessage().getMessage() instanceof AVIMFileMessage) {
            baseViewHolder.setText(R.id.tv_last_message,
                    String.format(LRUtils.getString(R.string.format_last_message), conversation.getLastMessage().getSender().getNickname(), LRUtils.getString(R.string.file_text)));
        } else if (conversation.getLastMessage().getMessage() instanceof AVIMImageMessage) {
            baseViewHolder.setText(R.id.tv_last_message,
                    String.format(LRUtils.getString(R.string.format_last_message), conversation.getLastMessage().getSender().getNickname(), LRUtils.getString(R.string.image_text)));
        } else if (conversation.getLastMessage().getMessage() instanceof AVIMAudioMessage) {
            baseViewHolder.setText(R.id.tv_last_message,
                    String.format(LRUtils.getString(R.string.format_last_message), conversation.getLastMessage().getSender().getNickname(), LRUtils.getString(R.string.audio_text)));
        } else if (conversation.getLastMessage().getMessage() instanceof AVIMVideoMessage) {
            baseViewHolder.setText(R.id.tv_last_message,
                    String.format(LRUtils.getString(R.string.format_last_message), conversation.getLastMessage().getSender().getNickname(), LRUtils.getString(R.string.video_text)));
        }
    }
}
