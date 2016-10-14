package com.zeroami.youliao.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.ChatMessage;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天消息Adapter</p>
 */
public class ChatMessageAdapter extends BaseMultiItemQuickAdapter<ChatMessage> {

    private Context mContext;
    public ChatMessageAdapter(Context context,List<ChatMessage> data) {
        super(data);
        mContext = context;
        addItemType(ChatMessage.ChatMessageType.RECEIVE.ordinal(), R.layout.item_chat_message_receive);
        addItemType(ChatMessage.ChatMessageType.SEND.ordinal(), R.layout.item_chat_message_send);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChatMessage chatMessage) {

    }
}
