package com.zeroami.youliao.contract.fragment;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.Conversation;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天会话Contract</p>
 */
public interface ConversationContract {
    interface View extends LMvpView{
        /**
         * 更新聊天会话列表
         * @param conversationList
         */
        void updateConversationList(List<Conversation> conversationList);

        /**
         * 获取聊天会话列表
         * @return
         */
        List<Conversation> getConversationList();

        /**
         * 获取接受到的会话
         * @return
         */
        AVIMConversation getReceiveConversation();

        /**
         * 获取接受到的消息
         * @return
         */
        AVIMMessage getReceiveMessage();

        String getSendConversationId();

        /**
         * 更新底部未读数量
         * @param count
         */
        void updateBottomNatificationCount(int count);

        /**
         * 跳转到聊天页
         */
        void gotoChat();
    }

    interface Presenter extends LMvpPresenter<View>{
        /**
         * 处理更新聊天会话
         */
        void doReceiveUpdateConversation();

        /**
         * 处理接受到消息
         */
        void doReceiveMessage();

        /**
         * 处理会话item点击
         */
        void doConversationItemClick();
    }
}
