package com.zeroami.youliao.model.real;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LThreadUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.AddRequestManager;
import com.zeroami.youliao.data.http.PushManager;
import com.zeroami.youliao.data.http.UserManager;
import com.zeroami.youliao.data.local.SPManager;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.callback.SuccessBeforeCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：FriendModel，管理朋友相关信息</p>
 */
public class FriendModel extends BaseModel implements IFriendModel {

    private UserManager mUserManager;
    private PushManager mPushManager;
    private AddRequestManager mAddRequestManager;
    private SPManager mSPManager;

    public FriendModel() {
        mUserManager = UserManager.getInstance();
        mPushManager = PushManager.getInstance();
        mAddRequestManager = AddRequestManager.getInstance();
        mSPManager = SPManager.getInstance();
    }


    @Override
    public void findUserByUsername(String username, final LeanCallback callback) {
        mUserManager.findUserByUsername(username, new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                handleCallback(User.convertToUserList(list), e, callback, null);
            }
        });
    }

    @Override
    public void findUsersByNickname(String nickname, int skip, int limit, final LeanCallback callback) {
        mUserManager.findUsersByNickname(nickname, skip, limit, new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                handleCallback(User.convertToUserList(list), e, callback, null);
            }
        });
    }

    @Override
    public void sendFriendAddRequest(final User toUser, String extra, final LeanCallback callback) {
        mAddRequestManager.createAddRequest(toUser, extra, new SaveCallback() {
            @Override
            public void done(AVException e) {
                handleCallback(null, e, callback, new SuccessBeforeCallback<Object>() {
                    @Override
                    public void call(Object data) {
                        // 发送一条推送告诉对方，我向你发送了添加请求
                        mPushManager.pushMessage(toUser.getObjectId(),
                                String.format(LRUtils.getString(R.string.notification_receive_add_request), mSPManager.getCurrentUser().getNickname()),
                                Constant.Action.ADD_FRIEND);
                    }
                });
            }
        });
    }

    @Override
    public void findFriendById(String objectId, final LeanCallback callback) {
        AVUser avUser = mUserManager.getCurrentUser();
        mUserManager.findFriendById(avUser, objectId, new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                handleCallback(User.convertToUserList(list), e, callback, null);
            }
        });
    }

    @Override
    public void findAddRequestByToUser(User toUser, final LeanCallback callback) {
        mAddRequestManager.findAddRequestByToUser(toUser, new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                handleCallback(AddRequest.convertToAddRequestList(list), e, callback, null);
            }
        });
    }

    @Override
    public void countUnreadAddRequests(final LeanCallback callback) {
        mAddRequestManager.countUnreadAddRequests(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                handleCallback(i, e, callback, null);
            }
        });
    }

    @Override
    public int getUnreadAddRequestsCount() {
        return mAddRequestManager.getUnreadAddRequestsCount();
    }

    @Override
    public void unreadAddRequestsIncrement() {
        mAddRequestManager.unreadAddRequestsIncrement();
    }

    @Override
    public boolean hasUnreadAddRequests() {
        return mAddRequestManager.hasUnreadAddRequests();
    }

    @Override
    public void markAddRequestsRead() {
        mAddRequestManager.markAddRequestsRead();
    }

    @Override
    public void findAddRequests(int skip, final int limit, final LeanCallback callback) {
        mAddRequestManager.findAddRequests(skip, limit, new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (callback != null) {
                    if (e == null) {
                        final List<AddRequest> addRequestList = AddRequest.convertToAddRequestList(list);
                        LThreadUtils.doInBackground(new Runnable() {
                            @Override
                            public void run() {
                                final CountDownLatch countDownLatch = new CountDownLatch(addRequestList.size());
                                for (final AddRequest addRequest : addRequestList) {
                                    mUserManager.findUserByObectId(addRequest.getFromUserId(), new FindCallback<AVUser>() {
                                        @Override
                                        public void done(List<AVUser> list, AVException e) {
                                            if (e == null && list.size() > 0){
                                                addRequest.setFromUser(User.convertToUser(list.get(0)));
                                            }
                                            countDownLatch.countDown();
                                        }
                                    });
                                }
                                try {
                                    countDownLatch.await();
                                    LThreadUtils.doInUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onSuccess(addRequestList);
                                        }
                                    });
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    } else {
                        LL.e(e);
                        callback.onError(e.getCode(), e);
                    }
                }
            }
        });
    }

    @Override
    public void agreeAddRequest(final AddRequest addRequest, final LeanCallback callback) {
        mAddRequestManager.agreeAddRequest(addRequest, new SaveCallback() {
            @Override
            public void done(AVException e) {
                handleCallback(null, e, callback, new SuccessBeforeCallback<Object>() {
                    @Override
                    public void call(Object data) {
                        // 发送一条推送告诉对方，我同意添加了你
                        mPushManager.pushMessage(addRequest.getFromUserId(),
                                String.format(LRUtils.getString(R.string.format_agree_add_request), mSPManager.getCurrentUser().getNickname()),
                                Constant.Action.NEW_FRIEND_ADDED);
                    }
                });
            }
        });
    }

    @Override
    public void findFriends(final LeanCallback callback) {
        mUserManager.findFriends(new FindCallback<AVUser>() {
            @Override
            public void done(final List<AVUser> list, AVException e) {
                handleCallback(User.convertToUserList(list),e,callback,null);
            }
        });
    }

}
