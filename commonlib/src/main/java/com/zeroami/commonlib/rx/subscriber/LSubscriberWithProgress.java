package com.zeroami.commonlib.rx.subscriber;

import android.content.Context;

import com.zeroami.commonlib.rx.LProgressDialogHandler;

import rx.Subscriber;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：带ProgressDialog的Subscriber</p>
 */
public abstract class LSubscriberWithProgress<T> extends Subscriber<T> implements LProgressDialogHandler.LOnProgressDialogCancelListener {

    private LProgressDialogHandler mProgressDialogHandler;

    private Context mContext;

    /**
     * 构造方法
     * @param context       context
     * @param message       ProgressDialog显示信息
     * @param cancelable    ProgressDialog是否可取消
     */
    public LSubscriberWithProgress(Context context ,CharSequence message,boolean cancelable) {
        this.mContext = context;
        mProgressDialogHandler = new LProgressDialogHandler(context, message, cancelable);
        mProgressDialogHandler.setProgressDialogCancelListener(this);
    }

    /**
     * 显示ProgressDialog
     */
    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(LProgressDialogHandler.MSG_SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    /**
     * 销毁ProgressDialog
     */
    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(LProgressDialogHandler.MSG_DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
    }

    @Override
    public void onProgressDialogCancel() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
