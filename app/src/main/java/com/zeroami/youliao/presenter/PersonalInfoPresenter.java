package com.zeroami.youliao.presenter;

import com.avos.avoscloud.AVException;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.bean.User;
import com.zeroami.youliao.contract.PersonalInfoContract;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.callback.LeanCallback;
import com.zeroami.youliao.model.real.UserModel;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：个人信息Presenter</p>
 */
public class PersonalInfoPresenter extends LBasePresenter<PersonalInfoContract.View,IUserModel> implements PersonalInfoContract.Presenter {

    private boolean isEdit = false;

    public PersonalInfoPresenter(PersonalInfoContract.View view) {
        super(view);
    }

    @Override
    protected IUserModel getRealModel() {
        return new UserModel();
    }

    @Override
    protected IUserModel getTestModel() {
        return null;
    }

    @Override
    public void doEdit() {
        isEdit = !isEdit;
        if (isEdit){
            getMvpView().hideTextView();
            getMvpView().showEditView();
        }else{
            if (getMvpView().isContentChanged()){
                getMvpView().showAbandonChangeTips();
            }else {
                getMvpView().showTextView();
                getMvpView().hideEditView();
            }
        }
    }

    @Override
    public void doAbandonChange() {
        getMvpView().restoreView();
        getMvpView().showTextView();
        getMvpView().hideEditView();
    }

    @Override
    public void doSaveChange(User user) {
        if (!getMvpView().isContentChanged()){
            getMvpView().showToast(LRUtils.getString(R.string.personal_info_not_need_change));
            return;
        }
        if (user.getNickname().length() == 0 || user.getNickname().length() > 16){
            getMvpView().showToast(LRUtils.getString(R.string.nickname_length_error));
            return;
        }
        if (user.getSignature().length() > 120){
            getMvpView().showToast(LRUtils.getString(R.string.signature_length_error));
            return;
        }
        isEdit = !isEdit;
        getMvpView().showTextView();
        getMvpView().hideEditView();
        getMvpModel().updateUserInfo(user, new LeanCallback() {
            @Override
            public void onSuccess(Object data) {
                getMvpView().showToast(LRUtils.getString(R.string.persontal_info_change_success));
                getMvpView().updateView();
            }

            @Override
            public void onError(int code, AVException e) {
                getMvpView().showToast(LRUtils.getString(R.string.persontal_info_change_error));
                getMvpView().restoreView();
            }
        });
    }
}
