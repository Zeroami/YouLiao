package com.zeroami.youliao.presenter.activity;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LNetUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.activity.NewFriendContract;
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
public class NewFriendPresenter extends LBasePresenter<NewFriendContract.View, IFriendModel> implements NewFriendContract.Presenter {

    private int mCurrentPage = 0;

    public NewFriendPresenter(NewFriendContract.View view) {
        super(view);
    }

    @Override
    protected IFriendModel createRealModel() {
        return new FriendModel();
    }

    @Override
    protected IFriendModel createTestModel() {
        return null;
    }

    @Override
    public void doViewInitialized() {

        getMvpModel().markAddRequestsRead(new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                initAddRequestList();
            }

            @Override
            public void onError(int code, AVException e) {
                initAddRequestList();
            }
        });
    }


    @Override
    public void doLoadMore() {
        mCurrentPage++;
        getMvpModel().loadAddRequests(mCurrentPage * Constant.PAGE_SIZE, Constant.PAGE_SIZE, new LeanCallback<List<AddRequest>>() {
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
                getMvpView().showMessage(LRUtils.getString(R.string.load_failure));
            }
        });
    }

    @Override
    public void doAgreeAddRequest() {

        if (!LNetUtils.isNetworkConnected()) {
            getMvpView().showMessage(LRUtils.getString(R.string.please_check_network));
            return;
        }

        getMvpModel().agreeAddRequest(getMvpView().getAddRequest(), new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                getMvpView().notifyDataSetChanged();
                getMvpView().gotoChat();
            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().showMessage(LRUtils.getString(R.string.operate_failure));
            }
        });
    }


    /**
     * 初始化添加请求列表
     */
    private void initAddRequestList() {
        getMvpModel().loadAddRequests(0, Constant.INIT_SIZE, new LeanCallback<List<AddRequest>>() {
            @Override
            public void onSuccess(List<AddRequest> data) {
                getMvpView().updateAddRequestList(data);
            }

            @Override
            public void onError(int code, AVException e) {
            }
        });
    }
}
