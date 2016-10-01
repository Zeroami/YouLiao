package com.zeroami.youliao.presenter;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.youliao.contract.ContactsContract;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.FriendModel;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：联系人Contract</p>
 */
public class ContactsPresenter extends LBasePresenter<ContactsContract.View,IFriendModel> implements ContactsContract.Presenter{

    private boolean isOne = true;

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
        if (isOne){
            isOne = false;
            getMvpModel().countUnreadAddRequests(new LeanCallback<Integer>() {
                @Override
                public void onSuccess(Integer data) {
                    int count = data;
                    getMvpView().updateNewFriendUnread(count);
                    if (count > 0) {
                        getMvpView().showNewFriendUnread();
                    } else {
                        getMvpView().hideNewFriendUnread();
                    }
                }

                @Override
                public void onError(int code, AVException e) {

                }
            });
        }else{
            int count = getMvpModel().getUnreadAddRequestsCount();
            getMvpView().updateNewFriendUnread(count);
            if (count > 0) {
                getMvpView().showNewFriendUnread();
            } else {
                getMvpView().hideNewFriendUnread();
            }
        }
    }

    @Override
    public void doReceiveNewFriendRequest() {
        getMvpModel().unreadAddRequestsIncrement();
        int count = getMvpModel().getUnreadAddRequestsCount();
        getMvpView().updateNewFriendUnread(count);
        if (count > 0) {
            getMvpView().showNewFriendUnread();
        } else {
            getMvpView().hideNewFriendUnread();
        }
    }

    @Override
    public void doNewFriendClick() {
        if (getMvpModel().hasUnreadAddRequests()){
            getMvpModel().markAddRequestsRead();
        }
        getMvpView().gotoNewFriend();
    }
}
