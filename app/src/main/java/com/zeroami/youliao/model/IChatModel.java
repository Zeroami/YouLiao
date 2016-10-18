package com.zeroami.youliao.model;

import com.zeroami.commonlib.mvp.LMvpModel;
import com.zeroami.youliao.model.callback.LeanCallback;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：IChatModel，管理聊天相关信息</p>
 */
public interface IChatModel extends LMvpModel {

    /**
     * 创建单聊会话
     * @param memberId
     * @param callback
     */
    void createSingleConversation(String memberId,LeanCallback callback);
    /**
     * 发送消息
     * @param text
     * @param callback
     */
    void sendMessage(String text,LeanCallback callback);

    /**
     * 加载历史消息记录
     * @param messageId
     * @param timestamp
     * @param limit
     */
    void loadHistoryMessages(String messageId,long timestamp,int limit,LeanCallback callback);

    /**
     * 加载聊天会话
     * @param callback
     */
    void loadConversations(LeanCallback callback);

    /**
     * 会话是否已经保存
     * @param conversationId
     * @return
     */
    boolean isConversationIdSaved(String conversationId);

    /**
     * 加载聊天会话
     * @param conversationId
     * @param callback
     */
    void loadConversation(String conversationId,LeanCallback callback);

    /**
     * 会话未读数量加一
     * @param conversationId
     */
    void increamentConversationUnreadCount(String conversationId);

    /**
     * 标记会话为已读
     * @param conversationId
     */
    void markConversationRead(String conversationId);
}
