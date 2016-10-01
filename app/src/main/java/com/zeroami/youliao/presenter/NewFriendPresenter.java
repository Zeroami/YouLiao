package com.zeroami.youliao.presenter;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.youliao.bean.AddRequest;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.NewFriendContract;
import com.zeroami.youliao.model.IFriendModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.FriendModel;

import java.util.List;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：新的朋友Presenter</p>
 */
public class NewFriendPresenter extends LBasePresenter<NewFriendContract.View,IFriendModel> implements NewFriendContract.Presenter {
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
        getMvpModel().findAddRequests(0, Constant.PAGE_SIZE, new LeanCallback<List<AddRequest>>() {
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
