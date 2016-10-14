package com.zeroami.youliao.presenter.activity;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LT;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.activity.FriendDetailContract;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.FriendModel;
import com.zeroami.youliao.model.real.UserModel;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：朋友详情Presenter</p>
 */
public class FriendDetailPresenter extends LBasePresenter<FriendDetailContract.View,IFriendModel> implements FriendDetailContract.Presenter {

    private IUserModel mUserModel;

    public FriendDetailPresenter(FriendDetailContract.View view) {
        super(view);
        mUserModel = new UserModel();
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
        User currentUser = mUserModel.getCurrentUser();
        final User toUser = getMvpView().getUser();
        getMvpView().setDefaultExtra(String.format(LRUtils.getString(R.string.format_extra), currentUser.getNickname()));
        if (currentUser.getObjectId().equals(toUser.getObjectId())){ // 判断是否为本人

        }else{
            checkIsMyFriend(toUser);
        }
    }

    /**
     * 检查是否已经添加为好友
     * @param toUser
     */
    private void checkIsMyFriend(final User toUser) {
        getMvpModel().findFriendByObjectId(toUser.getObjectId(), new LeanCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (data.size() > 0) {       // 已添加为朋友，显示发送信息和删除朋友按钮
                    getMvpView().showSendMessage();
                    getMvpView().showDeleteFriend();
                } else {                      // 未添加为朋友，判断是否已经发送过添加请求
                    checkIsAlreadySendAddRequest(toUser);
                }
            }

            @Override
            public void onError(int code, AVException e) {
            }
        });
    }

    /**
     * 检查是否已经发送过添加请求
     * @param toUser
     */
    private void checkIsAlreadySendAddRequest(User toUser) {
        getMvpModel().findAddRequestByToUser(toUser, new LeanCallback<List<AddRequest>>() {
            @Override
            public void onSuccess(List<AddRequest> data) {
                if (data.size() > 0){       // 已经发送过添加请求
                    getMvpView().showAlreadSendRequestTips();
                }else{
                    getMvpView().showAddFriend();
                }
            }

            @Override
            public void onError(int code, AVException e) {
            }
        });
    }

    @Override
    public void doEditExtra() {
        getMvpView().editExtra();
    }

    @Override
    public void doAddFriend() {
        getMvpModel().sendFriendAddRequest(getMvpView().getUser(), getMvpView().getExtra(), new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                LT.show(LRUtils.getString(R.string.send_add_request_success));
                getMvpView().hideAddFriend();
                getMvpView().showAlreadSendRequestTips();
            }

            @Override
            public void onError(int code, AVException e) {
                LT.show(LRUtils.getString(R.string.send_add_request_error));
            }
        });
    }

    @Override
    public void doAvatarClick() {
        getMvpView().gotoImage();
    }

    @Override
    public void doSendMessage() {
        getMvpView().gotoChat();
    }

    @Override
    public void doDeleteFriend() {
        getMvpModel().deleteFriend(getMvpView().getUser().getObjectId(), new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                getMvpView().showMessage(LRUtils.getString(R.string.delete_friend_success));
                getMvpView().hideSendMessage();
                getMvpView().hideDeleteFriend();
                getMvpView().showAddFriend();
            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().showMessage(LRUtils.getString(R.string.delete_friend_failure));
            }
        });
    }
}
