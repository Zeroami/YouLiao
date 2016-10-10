package com.zeroami.youliao.presenter;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.ContactsContract;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.FriendModel;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：联系人Contract</p>
 */
public class ContactsPresenter extends LBasePresenter<ContactsContract.View, IFriendModel> implements ContactsContract.Presenter {

    public ContactsPresenter(ContactsContract.View view) {
        super(view);
    }

    @Override
    protected IFriendModel getRealModel() {
        return new FriendModel();
    }

    @Override
    protected IFriendModel getTestModel() {
        return null;
    }

    @Override
    public void doViewInitialized() {
        initFriendList();
        initNewFriendUnreadCount();
    }

    private void initFriendList() {
        getMvpModel().findFriends(new LeanCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (getMvpView() != null) {
                    getMvpView().updateFriendList(data);
                }
            }

            @Override
            public void onError(int code, AVException e) {

            }
        });
    }

    private void initNewFriendUnreadCount() {
        getMvpModel().countUnreadAddRequests(new LeanCallback<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                if (getMvpView() != null){
                    int count = data;
                    getMvpView().updateNewFriendUnread(count);
                    if (count > 0) {
                        getMvpView().showNewFriendUnread();
                    } else {
                        getMvpView().hideNewFriendUnread();
                    }
                }
            }

            @Override
            public void onError(int code, AVException e) {

            }
        });
    }

    @Override
    public void doReceiveNewFriendRequest() {
        initNewFriendUnreadCount();
    }

    @Override
    public void doNewFriendClick() {
        getMvpView().gotoNewFriend();
    }

    @Override
    public void doReceiveNewFriendAdded() {
        initFriendList();
    }
}
