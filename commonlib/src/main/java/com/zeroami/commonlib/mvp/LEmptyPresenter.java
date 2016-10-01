package com.zeroami.commonlib.mvp;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：空的Presenter</p>
 */
public class LEmptyPresenter extends LBasePresenter<LEmptyContract.View,LMvpModel> implements LEmptyContract.Presenter {
    public LEmptyPresenter(LEmptyContract.View view) {
        super(view);
    }

    @Override
    protected LMvpModel getRealModel() {
        return null;
    }

    @Override
    protected LMvpModel getTestModel() {
        return null;
    }
}
