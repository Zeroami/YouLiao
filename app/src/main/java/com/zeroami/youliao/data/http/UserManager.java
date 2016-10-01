package com.zeroami.youliao.data.http;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.gson.Gson;
import com.zeroami.commonlib.utils.LSPUtils;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：用户数据管理者</p>
 */
public class UserManager {
    public static final String LOCATION = "location";
    private static volatile UserManager sInstance;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @param logInCallback
     */
    public void logIn(String username, String password, LogInCallback<AVUser> logInCallback) {
        AVUser.logInInBackground(username, password, logInCallback);
    }

    /**
     * 注册
     *
     * @param avUser
     * @param callback
     */
    public void signUp(AVUser avUser, SignUpCallback callback) {
        avUser.signUpInBackground(callback);
    }

    /**
     * 退出登陆
     */
    public void logOut() {
        AVUser.logOut();
    }

    /**
     * 获取当前登陆用户id
     *
     * @return
     */
    public String getCurrentUserId() {
        AVUser currentUser = AVUser.getCurrentUser(AVUser.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }

    public User getCurrentUser(){
        String userJson = LSPUtils.get(Constant.PreferenceKey.KEY_CURRENT_USER, "");
        return new Gson().fromJson(userJson, User.class);
    }

    /**
     * 关联用户的推送频道（即User表里保存Installation表的ObjectId，发送特定的Channel时，
     * 根据Installation表的Channels字段找到Installation表的ObjectId，根据Installation表的ObjectId找到相应的User并推送）
     *
     * @param avUser
     */
    public void attachUserToPushChannel(AVUser avUser) {
        AVInstallation installation = AVInstallation.getCurrentInstallation();
        if (installation != null) {
            avUser.put(Constant.User.INSTALLATION, installation);
            avUser.saveInBackground();
        }
    }

    /**
     * 获取用户头像url
     *
     * @param avUser
     * @return
     */
    public String getAvatarUrl(AVUser avUser) {
        AVFile avatar = avUser.getAVFile(Constant.User.AVATAR);
        if (avatar != null) {
            return avatar.getUrl();
        } else {
            return null;
        }
    }

    /**
     * 保存用户头像并更新信息
     *
     * @param avUser
     * @param path
     * @param saveCallback
     */
    public void saveUserWithAvatar(final AVUser avUser, String path, final SaveCallback saveCallback) {
        final AVFile file;
        try {
            file = AVFile.withAbsoluteLocalPath(avUser.getUsername(), path);
            avUser.put(Constant.User.AVATAR, file);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (null == e) {
                        String avatarUrl = getAvatarUrl(avUser);
                        avUser.put(Constant.User.AVATAR, avatarUrl);
                        avUser.saveInBackground(saveCallback);
                    } else {
                        if (null != saveCallback) {
                            saveCallback.done(e);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AVGeoPoint getGeoPoint(AVUser avUser) {
        return avUser.getAVGeoPoint(LOCATION);
    }

    public void setGeoPoint(AVUser avUser, AVGeoPoint point) {
        avUser.put(LOCATION, point);
    }

    /**
     * 删除我的朋友
     *
     * @param avUser
     * @param friendId
     * @param saveCallback
     */
    public void removeFriend(AVUser avUser, String friendId, final SaveCallback saveCallback) {
        avUser.unfollowInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }

    /**
     * 查找出我的全部朋友
     *
     * @param avUser
     * @param cachePolicy
     * @param findCallback
     */
    public void findFriends(AVUser avUser, AVQuery.CachePolicy cachePolicy, FindCallback<AVUser>
            findCallback) {
        AVQuery<AVUser> q = null;
        try {
            q = avUser.followeeQuery(AVUser.class);
        } catch (Exception e) {
        }
        q.setCachePolicy(cachePolicy);
//        q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
        q.findInBackground(findCallback);
    }

    /**
     * 根据账号查找我的朋友
     * @param avUser
     * @param username
     * @param cachePolicy
     * @param findCallback
     */
    public void findFriendByUsername(AVUser avUser, String username,AVQuery.CachePolicy cachePolicy, FindCallback<AVUser>
            findCallback) {
        AVQuery<AVUser> q = null;
        try {
            q = avUser.followeeQuery(AVUser.class);
        } catch (Exception e) {
        }
        q.setCachePolicy(cachePolicy);
//        q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
        q.whereEqualTo(Constant.User.USERNAME,username);
        q.findInBackground(findCallback);
    }

    /**
     * 根据账号搜索网络上的人
     *
     * @param username
     * @param findCallback
     */
    public void findUserByUsername(String username, FindCallback<AVUser>
            findCallback) {
        AVQuery<AVUser> q = AVUser.getQuery(AVUser.class);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
        q.whereEqualTo(Constant.User.USERNAME, username);
        q.findInBackground(findCallback);
    }
    /**
     * 根据昵称搜索网络上的人
     *
     * @param nickname
     * @param skip
     * @param limit
     * @param findCallback
     */
    public void findUsersByNickname(String nickname, int skip, int limit, FindCallback<AVUser>
            findCallback) {
        AVQuery<AVUser> q = AVUser.getQuery(AVUser.class);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
        q.whereContains(Constant.User.NICKNAME, nickname);
        q.orderByDescending(AVObject.CREATED_AT);
        q.limit(limit);
        q.skip(skip);
        q.findInBackground(findCallback);
    }


    /**
     * 根据ObjectId搜索网络上的人
     * @param objectId
     * @param findCallback
     */
    public void findUserByObectId(String objectId, FindCallback<AVUser>
            findCallback) {
        AVQuery<AVUser> q = AVUser.getQuery(AVUser.class);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
        q.whereEqualTo(AVObject.OBJECT_ID, objectId);
        q.findInBackground(findCallback);
    }

}
