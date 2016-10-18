package com.zeroami.youliao.contract.activity;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.zeroami.commonlib.mvp.LMvpPresenter;
import com.zeroami.commonlib.mvp.LMvpView;
import com.zeroami.youliao.bean.ChatMessage;
import com.zeroami.youliao.bean.User;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：聊天Contract</p>
 */
public interface ChatContract {

    interface View extends LMvpView {

        /**
         * 获取聊天的对象
         * @return
         */
        User getUser();

        /**
         * 显示底部布局
         */
        void showBottomLayout();

        /**
         * 隐藏底部布局
         */
        void hideBottomLayout();

        /**
         * 显示表情布局
         */
        void showExpressionLayout();

        /**
         * 隐藏表情布局
         */
        void hideExpressionLayout();

        /**
         * 隐藏输入法
         */
        void hideInputMethod();

        /**
         * 清除输入框的焦点
         */
        void clearEditTextFocus();

        /**
         * 清楚输入框内容
         */
        void clearEditText();

        /**
         * 获取要发送的消息
         * @return
         */
        String getSendMessage();

        /**
         * 在开始处追加历史消息记录
         * @param chatMessageList
         */
        void prependChatMessageList(List<ChatMessage> chatMessageList);

        /**
         * 追加发送的消息
         * @param chatMessage
         */
        void appendChatMessage(ChatMessage chatMessage);

        /**
         * 获取接受到的消息
         * @return
         */
        AVIMMessage getReceiveMessage();

        /**
         * 获取第一条消息
         * @return
         */
        AVIMMessage getFirstMessage();
    }

    interface Presenter extends LMvpPresenter<View> {
        /**
         * 处理表情点击
         */
        void doExpressionClick();

        /**
         * 处理输入框获取焦点
         */
        void doEditTextGetFocus();

        /**
         * 处理外部点击
         */
        void doOutsideClick();

        /**
         * 处理发送消息
         */
        void doSendMessage();

        /**
         * 处理接受到消息
         */
        void doReceiveMessage();

        /**
         * 加载更多历史消息记录
         */
        void doLoadMoreHistoryMessages();
    }
}
