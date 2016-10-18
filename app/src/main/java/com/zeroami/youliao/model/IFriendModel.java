package com.zeroami.youliao.model;

import com.zeroami.commonlib.mvp.LMvpModel;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.model.callback.LeanCallback;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：IFriendModel，管理朋友相关信息</p>
 */
public interface IFriendModel extends LMvpModel {

    /**
     * 根据账号搜索网络上的人
     * @param username
     * @param callback
     */
    void findUserByUsername(String username, LeanCallback callback);

    /**
     * 根据昵称搜索网络上的人
     * @param nickname
     * @param skip
     * @param limit
     * @param callback
     */
    void findUsersByNickname(String nickname, int skip, int limit, LeanCallback callback);

    /**
     * 发送朋友添加请求
     * @param toUser
     * @param extra
     * @param callback
     */
    void sendFriendAddRequest(User toUser, String extra, LeanCallback callback);

    /**
     * 根据id查找我的好友
     * @param objectId
     * @param callback
     */
    void findFriendByObjectId(String objectId, LeanCallback callback);

    /**
     * 查找我向某个人发起的朋友添加请求
     * @param toUser
     * @param callback
     */
    void findAddRequestByToUser(User toUser,LeanCallback callback);

    /**
     * 统计新的朋友未读添加请求
     * @param callback
     */
    void countUnreadAddRequests(LeanCallback callback);

    /**
     * 标记所有朋友添加请求为已读
     * @param callback
     */
    void markAddRequestsRead(LeanCallback callback);

    /**
     * 加载朋友添加请求
     * @param skip
     * @param limit
     * @param callback
     */
    void loadAddRequests(int skip, int limit, LeanCallback callback);

    /**
     * 同意朋友添加请求
     * @param addRequest
     * @param callback
     */
    void agreeAddRequest(AddRequest addRequest,LeanCallback callback);

    /**
     * 加载我的朋友
     * @param callback
     */
    void loadFriends(LeanCallback callback);

    /**
     * 删除我的朋友
     * @param friendId
     * @param callback
     */
    void deleteFriend(String friendId, LeanCallback callback);

}
