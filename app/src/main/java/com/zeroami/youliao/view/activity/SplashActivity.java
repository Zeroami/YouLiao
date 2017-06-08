package com.zeroami.youliao.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeroami.commonlib.utils.LPageUtils;
import com.zeroami.youliao.R;
import com.zeroami.youliao.base.BaseMvpActivity;
import com.zeroami.youliao.contract.activity.SplashContract;
import com.zeroami.youliao.presenter.activity.SplashPresenter;

import butterknife.Bind;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：闪屏页</p>
 */
public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements SplashContract.View {

    @Bind(R.id.iv_splash)
    ImageView ivSplash;

    @Bind(R.id.tv_version_name)
    TextView tvVersionName;

    private static final int ANIMATION_DURATION = 3000;
    private static final float SCALE_END = 1.3f;


    @Override
    protected SplashContract.Presenter createPresenter() {
        return new SplashPresenter(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

    }

    @Override
    public void startAnimation() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(ivSplash, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(ivSplash, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                getMvpPresenter().doGoto();
            }
        });
    }

    @Override
    public void setVersionName(String versionName) {
        tvVersionName.setText(versionName);
    }

    @Override
    public void gotoLogin() {
        LPageUtils.startActivity(this,LoginActivity.class,true);
    }

    @Override
    public void gotoMain() {
        LPageUtils.startActivity(this,MainActivity.class,true);
    }
}
