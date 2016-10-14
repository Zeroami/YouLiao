package com.zeroami.youliao.presenter.activity;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LNetUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.activity.AddFriendContract;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.FriendModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：添加朋友Presenter</p>
 */
public class AddFriendPresenter extends LBasePresenter<AddFriendContract.View,IFriendModel> implements AddFriendContract.Presenter {

    private boolean isSearchByUsername = true;
    private String mNickname;
    private int mCurrentPage;

    public AddFriendPresenter(AddFriendContract.View view) {
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
    public void doChangeSearch() {
        isSearchByUsername = !isSearchByUsername;
        if (isSearchByUsername){
            getMvpView().showMessage(LRUtils.getString(R.string.change_to_username_search));
            getMvpView().showUsernameSearch();
            getMvpView().hideNicknameSearch();
        }else{
            getMvpView().showMessage(LRUtils.getString(R.string.change_to_nickname_search));
            getMvpView().showNicknameSearch();
            getMvpView().hideUsernameSearch();
        }
    }

    @Override
    public void doSearchByUsername() {

        if (!LNetUtils.isNetworkConnected()){
            getMvpView().showMessage(LRUtils.getString(R.string.please_check_network));
            return;
        }

        String username = getMvpView().getUsername();
        if (TextUtils.isEmpty(username)){
            getMvpView().showMessage(LRUtils.getString(R.string.input_empry_error));
            return ;
        }
        getMvpView().hideInputMethod();
        getMvpView().hideList();
        getMvpView().hideEmpty();
        getMvpView().showLoading();
        getMvpModel().findUserByUsername(username, new LeanCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                getMvpView().hideLoading();
                if (data.size() == 0) {
                    getMvpView().showEmpty();
                } else {
                    getMvpView().showList();
                    getMvpView().updateFriendList(data);
                }
            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().showMessage(LRUtils.getString(R.string.load_failure));
            }
        });

    }

    @Override
    public void doSearchByNickname() {

        if (!LNetUtils.isNetworkConnected()){
            getMvpView().showMessage(LRUtils.getString(R.string.please_check_network));
            return;
        }

        mNickname = getMvpView().getNickname();
        if (TextUtils.isEmpty(mNickname)){
            getMvpView().showMessage(LRUtils.getString(R.string.input_empry_error));
            return ;
        }
        mCurrentPage = 0;
        getMvpView().hideInputMethod();
        getMvpView().hideList();
        getMvpView().hideEmpty();
        getMvpView().showLoading();
        getMvpModel().findUsersByNickname(mNickname, 0, Constant.INIT_SIZE, new LeanCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                getMvpView().hideLoading();
                if (data.size() == 0) {
                    getMvpView().showEmpty();
                } else {
                    getMvpView().showList();
                    getMvpView().updateFriendList(data);
                }
            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().showMessage(LRUtils.getString(R.string.load_failure));
            }
        });
    }

    @Override
    public void doSearchByNicknameLoadMore() {
        mCurrentPage ++;
        getMvpModel().findUsersByNickname(mNickname, mCurrentPage * Constant.PAGE_SIZE, Constant.PAGE_SIZE, new LeanCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (data.size() > 0) {
                    getMvpView().appendFriendList(data);
                } else {
                    mCurrentPage--;
                    getMvpView().appendFriendList(data);
                }
            }

            @Override
            public void onError(int code, AVException e) {
                mCurrentPage--;
                getMvpView().appendFriendList(new ArrayList<User>());
                getMvpView().showMessage(LRUtils.getString(R.string.load_failure));
            }
        });
    }

    @Override
    public void doListItemClick(int position) {
        getMvpView().gotoFriendDetail(position);
    }
}
