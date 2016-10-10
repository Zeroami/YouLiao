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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：用户管理者</p>
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
    public void login(String username, String password, LogInCallback<AVUser> logInCallback) {
        AVUser.logInInBackground(username, password, logInCallback);
    }

    /**
     * 注册
     *
     * @param avUser
     * @param callback
     */
    public void register(AVUser avUser, SignUpCallback callback) {
        avUser.signUpInBackground(callback);
    }

    /**
     * 退出登陆
     */
    public void logout() {
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

    /**
     * 获取当前登陆用户
     *
     * @return
     */
    public AVUser getCurrentUser(){
        return AVUser.getCurrentUser(AVUser.class);
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
    public void updateUserWithAvatar(final AVUser avUser, String path, final SaveCallback saveCallback) {
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

    /**
     * 更新信息
     *
     * @param avUser
     * @param saveCallback
     */
    public void updateUser(final AVUser avUser, final SaveCallback saveCallback) {
            avUser.saveInBackground(saveCallback);
    }

    /**
     * 添加朋友
     * @param friendId
     * @param saveCallback
     */
    public void addFriend(String friendId, final SaveCallback saveCallback) {
        AVUser.getCurrentUser().followInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
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
     * @param findCallback
     */
    public void findFriends(final FindCallback<AVUser>
            findCallback) {
        try {
            AVQuery<AVUser> followeeQuery = getCurrentUser().followeeQuery(AVUser.class);
            final AVQuery<AVUser> followerQuery = getCurrentUser().followerQuery(AVUser.class);
            followeeQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            followerQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            followeeQuery.include(Constant.FOLLOWEE);
            followerQuery.include(Constant.FOLLOWER);
            followeeQuery.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(final List<AVUser> followeeList, AVException e) {
                    if (e == null){
                        followerQuery.findInBackground(new FindCallback<AVUser>() {
                            @Override
                            public void done(List<AVUser> followerList, AVException e) {
                                List<AVUser> list = new ArrayList<AVUser>();
                                list.addAll(followeeList);
                                list.addAll(followerList);
                                findCallback.done(list,e);
                            }
                        });
                    }else{
                        findCallback.done(followeeList,e);
                    }
                }
            });
        } catch (Exception e) {
        }

    }

    /**
     * 根据id查找我的朋友
     * @param avUser
     * @param friendId
     * @param findCallback
     */
    public void findFriendById(AVUser avUser, String friendId, final FindCallback<AVUser>
            findCallback) {
        try {
            AVQuery<AVUser> followeeQuery = avUser.followeeQuery(AVUser.class);
            final AVQuery<AVUser> followerQuery = avUser.followerQuery(AVUser.class);
            followeeQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            followerQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            followeeQuery.whereEqualTo(Constant.FOLLOWEE, AVUser.createWithoutData("_User", friendId));
            followerQuery.whereEqualTo(Constant.FOLLOWER, AVUser.createWithoutData("_User", friendId));
            followeeQuery.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(final List<AVUser> followeeList, AVException e) {
                    if (e == null){
                        followerQuery.findInBackground(new FindCallback<AVUser>() {
                            @Override
                            public void done(List<AVUser> followerList, AVException e) {
                                List<AVUser> list = new ArrayList<AVUser>();
                                list.addAll(followeeList);
                                list.addAll(followerList);
                                findCallback.done(list,e);
                            }
                        });
                    }else{
                        findCallback.done(followeeList,e);
                    }
                }
            });
        } catch (Exception e) {
        }
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
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
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
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
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
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.whereEqualTo(AVObject.OBJECT_ID, objectId);
        q.findInBackground(findCallback);
    }

}
