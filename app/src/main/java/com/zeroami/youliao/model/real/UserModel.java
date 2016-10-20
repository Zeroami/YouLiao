package com.zeroami.youliao.model.real;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.zeroami.commonlib.utils.LFileUtils;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;

import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.PushManager;
import com.zeroami.youliao.data.http.UserManager;
import com.zeroami.youliao.data.local.SPManager;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.callback.SuccessBeforeCallback;
import com.zeroami.youliao.utils.RandomUtils;
import com.zeroami.youliao.utils.StorageUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：UserModel，管理用户相关信息</p>
 */
public class UserModel extends BaseModel implements IUserModel {

    private UserManager mUserManager;
    private PushManager mPushManager;
    private SPManager mSPManager;

    public UserModel() {
        mUserManager = UserManager.getInstance();
        mPushManager = PushManager.getInstance();
        mSPManager = SPManager.getInstance();
    }

    @Override
    public void login(String username, String password, final LeanCallback callback) {
        mUserManager.login(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(final AVUser avUser, AVException e) {
                handleCallback(User.convertToUser(avUser), e, callback, new SuccessBeforeCallback<User>() {
                    @Override
                    public void call(User data) {
                        mSPManager.initSPFileName(data.getObjectId());
                        mSPManager.login();
                        mSPManager.saveCurrentUser(data);
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
        mUserManager.register(avUser, new SignUpCallback() {
            @Override
            public void done(AVException e) {
                handleCallback(null, e, callback, null);
            }
        });
    }

    @Override
    public void logout() {
        mPushManager.unsubscribePushChannel(User.convertToAVUser(getCurrentUser()));
        mUserManager.disconnectServer(null);
        mUserManager.logout();
        mSPManager.logout();
    }

    @Override
    public boolean getLoginStatus() {
        return mSPManager.getLoginStatus();
    }

    @Override
    public void connectServer(final LeanCallback callback) {
       mUserManager.connectServer(new AVIMClientCallback(){

           @Override
           public void done(final AVIMClient avimClient, AVIMException e) {
               handleCallback(null, e, callback,null);
           }
       });
    }

    @Override
    public String getCurrentUserId() {
        return mUserManager.getCurrentUserId();
    }

    @Override
    public User getCurrentUser() {
        return mSPManager.getCurrentUser();
    }


    @Override
    public void updateUserInfo(final User user, final LeanCallback callback) {
        final AVUser avUser = User.convertToAVUser(getCurrentUser());
        avUser.put(Constant.User.NICKNAME, user.getNickname());
        avUser.put(Constant.User.SIGNATURE, user.getSignature());
        if (TextUtils.isEmpty(user.getAvatar()) || !LFileUtils.isFileExist(user.getAvatar())) {
            mUserManager.updateUser(avUser, new SaveCallback() {
                @Override
                public void done(AVException e) {
                    handleCallback(null, e, callback, new SuccessBeforeCallback<Object>() {
                        @Override
                        public void call(Object data) {
                            mSPManager.saveCurrentUser(User.convertToUser(avUser));
                        }
                    });
                }
            });
        } else {
            mUserManager.updateUserWithAvatar(avUser, user.getAvatar(), new SaveCallback() {
                @Override
                public void done(AVException e) {
                    handleCallback(null, e, callback, new SuccessBeforeCallback<Object>() {
                        @Override
                        public void call(Object data) {
                            mSPManager.saveCurrentUser(User.convertToUser(avUser));
                        }
                    });
                }
            });
        }
    }

    @Override
    public boolean getExitAppStatus() {
        return mSPManager.getExitAppStatus();
    }

    @Override
    public void enterApp() {
        mSPManager.enterApp();
    }

    @Override
    public void exitApp() {
        mSPManager.exitApp();
    }
}
