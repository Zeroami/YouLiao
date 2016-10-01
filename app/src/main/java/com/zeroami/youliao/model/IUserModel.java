package com.zeroami.youliao.model;

import com.zeroami.commonlib.mvp.LMvpModel;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.model.callback.LeanCallback;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：IUserModel，管理用户相关信息</p>
 */
public interface IUserModel extends LMvpModel {

    /**
     * 登陆
     * @param username
     * @param password
     * @param callback
     */
    void login(String username, String password, LeanCallback callback);

    /**
     * 注册
     * @param username
     * @param password
     * @param callback
     */
    void register(String username, String password, LeanCallback callback);

    /**
     * 注销
     */
    void logout();

    /**
     * 获取当前用户id
     * @return
     */
    String getCurrentUserId();

    /**
     * 获取当前用户
     * @return
     */
    User getCurrentUser();

    /**
     * 更新用户的信息
     * @param user
     * @param callback
     */
    void updateUserInfo(User user,LeanCallback callback);

}
