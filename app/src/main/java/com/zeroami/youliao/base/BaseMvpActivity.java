package com.zeroami.youliao.base;

import com.zeroami.commonlib.mvp.LBaseMvpActivity;
import com.zeroami.commonlib.mvp.LMvpPresenter;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：BaseMvpActivity</p>
 */
public abstract class BaseMvpActivity<P extends LMvpPresenter> extends LBaseMvpActivity<P>{

    @Override
    public void finish() {
        super.finish();
        setupExitAnimation();
    }

    /**
     * 设置退出默认动画
     */
    protected void setupExitAnimation(){
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
