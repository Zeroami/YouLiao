package com.zeroami.youliao.presenter;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.NewFriendContract;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.FriendModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：新的朋友Presenter</p>
 */
public class NewFriendPresenter extends LBasePresenter<NewFriendContract.View,IFriendModel> implements NewFriendContract.Presenter {

    private int mCurrentPage = 0;

    public NewFriendPresenter(NewFriendContract.View view) {
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
        if (getMvpModel().hasUnreadAddRequests()){
            getMvpModel().markAddRequestsRead();
        }
        getMvpModel().findAddRequests(0, Constant.INIT_SIZE, new LeanCallback<List<AddRequest>>() {
            @Override
            public void onSuccess(List<AddRequest> data) {
                getMvpView().updateAddRequestList(data);
            }

            @Override
            public void onError(int code, AVException e) {
            }
        });
    }

    @Override
    public void doLoadMore() {
        mCurrentPage ++;
        getMvpModel().findAddRequests(mCurrentPage * Constant.PAGE_SIZE, Constant.PAGE_SIZE, new LeanCallback<List<AddRequest>>() {
            @Override
            public void onSuccess(List<AddRequest> data) {
                if (data.size() > 0) {
                    getMvpView().appendAddRequestList(data);
                } else {
                    mCurrentPage--;
                    getMvpView().appendAddRequestList(data);
                }
            }

            @Override
            public void onError(int code, AVException e) {
                mCurrentPage--;
                getMvpView().appendAddRequestList(new ArrayList<AddRequest>());
                getMvpView().showToast(LRUtils.getString(R.string.load_failure));
            }
        });
    }

    @Override
    public void doAgreeAddRequest() {
        getMvpModel().agreeAddRequest(getMvpView().getAddRequest(), new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                getMvpView().notifyDataSetChanged();
                getMvpView().gotoChat();
            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().showToast(LRUtils.getString(R.string.operate_failure));
            }
        });
    }
}
