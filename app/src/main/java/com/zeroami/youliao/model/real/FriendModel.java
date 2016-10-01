package com.zeroami.youliao.model.real;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.data.http.AddRequestManager;
import com.zeroami.youliao.data.http.PushManager;
import com.zeroami.youliao.data.http.UserManager;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.callback.SuccessBeforeCallback;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：FriendModel，管理朋友相关信息</p>
 */
public class FriendModel extends BaseModel implements IFriendModel {

    private UserManager mUserManager;
    private PushManager mPushManager;
    private AddRequestManager mAddRequestManager;

    public FriendModel() {
        mUserManager = UserManager.getInstance();
        mPushManager = PushManager.getInstance();
        mAddRequestManager = AddRequestManager.getInstance();
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
                        mPushManager.pushMessage(toUser.getObjectId(),
                                String.format(LRUtils.getString(R.string.notification_receive_add_request), mUserManager.getCurrentUser().getNickname()),
                                Constant.Action.ADD_FRIEND);
                    }
                });
            }
        });
    }

    @Override
    public void findFriendByUsername(String username, final LeanCallback callback) {
        AVUser avUser = User.convertToAVUser(mUserManager.getCurrentUser());
        mUserManager.findFriendByUsername(avUser, username, AVQuery.CachePolicy.CACHE_ELSE_NETWORK, new FindCallback<AVUser>() {
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
                        Observable.OnSubscribe<List<AddRequest>> onSubscribe = new Observable.OnSubscribe<List<AddRequest>>() {
                            @Override
                            public void call(Subscriber<? super List<AddRequest>> subscriber) {

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
                                    subscriber.onNext(addRequestList);
                                    subscriber.onCompleted();
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        };
                        Observable.create(onSubscribe)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<List<AddRequest>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<AddRequest> addRequestList) {
                                        callback.onSuccess(addRequestList);
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

}
