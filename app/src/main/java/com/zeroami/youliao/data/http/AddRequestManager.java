package com.zeroami.youliao.data.http;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;

import java.util.List;


/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：添加请求管理者</p>
 */
public class AddRequestManager {
    private static volatile AddRequestManager sInstance;

    /**
     * 用户端未读的邀请消息的数量
     */
    private int unreadAddRequestsCount = 0;

    private AddRequestManager() {
    }

    public static AddRequestManager getInstance() {
        if (sInstance == null) {
            synchronized (AddRequestManager.class) {
                if (sInstance == null) {
                    sInstance = new AddRequestManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 是否有未读的消息
     * @return
     */
    public boolean hasUnreadAddRequests() {
        return unreadAddRequestsCount > 0;
    }

    /**
     * 获取未读请求数量
     * @return
     */
    public int getUnreadAddRequestsCount() {
        return unreadAddRequestsCount;
    }

    /**
     * 推送过来时自增
     */
    public void unreadAddRequestsIncrement() {
        ++unreadAddRequestsCount;
    }

    /**
     * 从 server 获取未读消息的数量
     * @param countCallback
     */
    public void countUnreadAddRequests(final CountCallback countCallback) {
        AVQuery<AVObject> addRequestAVQuery = new AVQuery<>(Constant.AddRequest.CLASS_NAME);
        addRequestAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
        addRequestAVQuery.whereEqualTo(Constant.AddRequest.TO_USER_ID, UserManager.getInstance().getCurrentUser().getObjectId());
        addRequestAVQuery.whereEqualTo(Constant.AddRequest.IS_READ, false);
        addRequestAVQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                unreadAddRequestsCount = i;
                if (null != countCallback) {
                    countCallback.done(i, e);
                }
            }
        });
    }

    /**
     * 标记消息为已读，标记完后会刷新未读消息数量
     */
    public void markAddRequestsRead() {
        AVQuery<AVObject> addRequestAVQuery = new AVQuery<>(Constant.AddRequest.CLASS_NAME);
        addRequestAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
        addRequestAVQuery.whereEqualTo(Constant.AddRequest.TO_USER_ID, UserManager.getInstance().getCurrentUser().getObjectId());
        addRequestAVQuery.whereEqualTo(Constant.AddRequest.IS_READ, false);
        addRequestAVQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null){
                    for (AVObject avObject : list) {
                        avObject.put(Constant.AddRequest.IS_READ, true);
                    }
                    AVObject.saveAllInBackground(list, new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                countUnreadAddRequests(null);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 查找向我发起的朋友添加请求
     * @param skip
     * @param limit
     * @param findCallback
     */
    public void findAddRequests(int skip, int limit, FindCallback<AVObject> findCallback) {
        User user = UserManager.getInstance().getCurrentUser();
        AVQuery<AVObject> q = new AVQuery<>(Constant.AddRequest.CLASS_NAME);
        q.skip(skip);
        q.limit(limit);
        q.whereEqualTo(Constant.AddRequest.TO_USER_ID, user.getObjectId());
        q.orderByDescending(AVObject.CREATED_AT);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(findCallback);
    }

    /**
     * 查找我向某个人发起的朋友添加请求
     * @param toUser
     * @param findCallback
     */
    public void findAddRequestByToUser(User toUser,FindCallback<AVObject> findCallback){
        User user = UserManager.getInstance().getCurrentUser();
        AVQuery<AVObject> q = new AVQuery<>(Constant.AddRequest.CLASS_NAME);
        q.whereEqualTo(Constant.AddRequest.FROM_USER_ID, user.getObjectId());
        q.whereEqualTo(Constant.AddRequest.TO_USER_ID, toUser.getObjectId());
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(findCallback);
    }

    /**
     * 同意朋友添加请求
     * @param addRequest
     * @param saveCallback
     */
    public void agreeAddRequest(final AddRequest addRequest, final SaveCallback saveCallback) {
        addFriend(addRequest.getFromUserId(), new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    if (e.getCode() == AVException.DUPLICATE_VALUE) {
                        addRequest.setStatus(AddRequest.STATUS_DONE);
                        AddRequest.convertToAVObject(addRequest).saveInBackground(saveCallback);
                    } else {
                        saveCallback.done(e);
                    }
                } else {
                    addRequest.setStatus(AddRequest.STATUS_DONE);
                    AddRequest.convertToAVObject(addRequest).saveInBackground(saveCallback);
                }
            }
        });
    }

    /**
     * 添加朋友
     * @param friendId
     * @param saveCallback
     */
    public void addFriend(String friendId, final SaveCallback saveCallback) {
        AVUser avUser = User.convertToAVUser(UserManager.getInstance().getCurrentUser());
        avUser.followInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }

    /**
     * 创建朋友添加请求
     * @param toUser
     * @param extra
     * @param saveCallback
     */
    public void createAddRequest(User toUser,String extra,SaveCallback saveCallback){
        User curUser = UserManager.getInstance().getCurrentUser();
        AddRequest addRequest = new AddRequest();
        addRequest.setFromUserId(curUser.getObjectId());
        addRequest.setToUserId(toUser.getObjectId());
        addRequest.setStatus(AddRequest.STATUS_WAIT);
        addRequest.setExtra(extra);
        addRequest.setIsRead(false);
        AddRequest.convertToAVObject(addRequest).saveInBackground(saveCallback);
    }

}
