package com.zeroami.youliao.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zeroami.commonlib.glide.LGlideRoundTransform;
import com.zeroami.commonlib.utils.LDateUtils;
import com.zeroami.commonlib.widget.LCircleImageView;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.ChatMessage;
import com.zeroami.youliao.bean.TextMessage;
import com.zeroami.youliao.config.Constant;

import java.util.Date;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天消息Adapter</p>
 */
public class ChatMessageAdapter extends BaseMultiItemQuickAdapter<ChatMessage> {

    private Context mContext;

    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(data);
        mContext = context;
        addItemType(ChatMessage.ChatMessageType.RECEIVE.ordinal(), R.layout.item_chat_message_receive);
        addItemType(ChatMessage.ChatMessageType.SEND.ordinal(), R.layout.item_chat_message_send);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChatMessage chatMessage) {
        baseViewHolder.setText(R.id.tv_nickname,chatMessage.getSender().getNickname())
                .setText(R.id.tv_time, LDateUtils.formatDate(new Date(chatMessage.getTimestamp()),LDateUtils.FORMAT_yyyyMMddHHmmss));
        ImageView ivAvatar = baseViewHolder.getView(R.id.iv_avatar);
        if (TextUtils.isEmpty(chatMessage.getSender().getAvatar())) {
            Glide.with(mContext).load(R.drawable.img_default_face).transform(new LGlideRoundTransform(mContext, Constant.AVATAR_ROUND_SIZE)).into(ivAvatar);
        } else {
            Glide.with(mContext).load(chatMessage.getSender().getAvatar()).centerCrop().transform(new LGlideRoundTransform(mContext, Constant.AVATAR_ROUND_SIZE)).into(ivAvatar);
        }
        if (chatMessage.getMessage() instanceof AVIMTextMessage) {
            baseViewHolder.setText(R.id.tv_message, ((AVIMTextMessage) chatMessage.getMessage()).getText());
        }
    }
}
