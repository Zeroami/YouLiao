package com.zeroami.youliao.presenter;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.zeroami.commonlib.mvp.LBasePresenter;
import com.zeroami.commonlib.utils.LAppUtils;
import com.zeroami.commonlib.utils.LRUtils;
import com.zeroami.commonlib.utils.LSPUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.config.Constant;
import com.zeroami.youliao.contract.SplashContract;
import com.zeroami.youliao.model.IUserModel;
import com.zeroami.youliao.model.real.UserModel;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：闪屏页Presenter</p>
 */
public class SplashPresenter extends LBasePresenter<SplashContract.View,IUserModel> implements SplashContract.Presenter{

    public SplashPresenter(SplashContract.View view) {
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
    public void doViewInitialized() {
        if (getMvpModel().getExitAppStatus()){  // 当前为退出状态，显示闪屏页
            String versionName = String.format(LRUtils.getString(R.string.format_version_name), LAppUtils.getVerionName());
            getMvpView().setVersionName(versionName);
            getMvpView().startAnimation();
        }else{
            doGoto();
        }
    }

    @Override
    public void doGoto() {
        // 判断是否登陆
        if (getMvpModel().getLoginStatus()){
            getMvpView().gotoMain();
        }else{
            getMvpView().gotoLogin();
        }
    }
}
