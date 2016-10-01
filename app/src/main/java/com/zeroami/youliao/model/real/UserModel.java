package com.zeroami.youliao.model.real;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.gson.Gson;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LSPUtils;
import com.zeroami.youliao.R;

import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.PushManager;
import com.zeroami.youliao.data.http.UserManager;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.callback.SuccessBeforeCallback;
import com.zeroami.youliao.utils.RandomUtils;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：UserModel，管理用户相关信息</p>
 */
public class UserModel extends BaseModel implements IUserModel {

    private UserManager mUserManager;
    private PushManager mPushManager;

    public UserModel() {
        mUserManager = UserManager.getInstance();
        mPushManager = PushManager.getInstance();
    }

    @Override
    public void login(String username, String password, final LeanCallback callback) {
        mUserManager.logIn(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(final AVUser avUser, AVException e) {
                handleCallback(User.convertToUser(avUser), e, callback, new SuccessBeforeCallback<User>() {
                    @Override
                    public void call(User data) {
                        LSPUtils.put(Constant.PreferenceKey.KEY_IS_LOGIN, true);
                        LSPUtils.put(Constant.PreferenceKey.KEY_CURRENT_USER, new Gson().toJson(data));
                        mPushManager.subscribePushChannel(avUser);
                    }
                });
            }
        });
    }

    @Override
    public void register(String username, String password, final LeanCallback callback) {
        AVUser avUser = new AVUser();
        avUser.setUsername(username);
        avUser.setPassword(password);
        avUser.put(Constant.User.NICKNAME, String.format(LRUtils.getString(R.string.format_default_nickname), RandomUtils.createRandomMD5String(8)));
        avUser.put(Constant.User.SIGNATURE, "");
        avUser.put(Constant.User.AVATAR, "");
        mUserManager.signUp(avUser, new SignUpCallback() {
            @Override
            public void done(AVException e) {
                handleCallback(null,e,callback,null);
            }
        });
    }

    @Override
    public void logout() {
        mPushManager.unsubscribePushChannel(User.convertToAVUser(getCurrentUser()));
        mUserManager.logOut();
        LSPUtils.remove(Constant.PreferenceKey.KEY_IS_LOGIN);
    }

    @Override
    public String getCurrentUserId() {
        return mUserManager.getCurrentUserId();
    }

    @Override
    public User getCurrentUser() {
        return mUserManager.getCurrentUser();
    }


    @Override
    public void updateUserInfo(final User user, final LeanCallback callback) {
        final AVUser avUser = User.convertToAVUser(getCurrentUser());
        avUser.put(Constant.User.NICKNAME, user.getNickname());
        avUser.put(Constant.User.SIGNATURE, user.getSignature());
        avUser.put(Constant.User.AVATAR, user.getAvatar());
        mUserManager.saveUserWithAvatar(avUser, user.getAvatar(), new SaveCallback() {
            @Override
            public void done(AVException e) {
                handleCallback(null, e, callback, new SuccessBeforeCallback<Object>() {
                    @Override
                    public void call(Object data) {
                        LSPUtils.put(Constant.PreferenceKey.KEY_CURRENT_USER, new Gson().toJson(User.convertToUser(avUser)));
                    }
                });
            }
        });
    }
}
