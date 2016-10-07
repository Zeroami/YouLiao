package com.zeroami.commonlib.rx;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：创建和销毁ProgressDialog的Handler处理类</p>
 */
public class LProgressDialogHandler extends Handler {

    public static final int MSG_SHOW_PROGRESS_DIALOG = 1;
    public static final int MSG_DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog mProgressDialog;

    private Context mContext;
    private CharSequence mMessage;
    private boolean mCancelable;

    private LOnProgressDialogCancelListener mOnProgressDialogCancelListener;

    public LProgressDialogHandler(Context context, CharSequence message,
                                 boolean cancelable) {
        super();
        this.mContext = context;
        this.mMessage = message;
        this.mCancelable = cancelable;
    }


    /**
     * 设置ProgressDialog取消监听
     * @param progressDialogCancelListener
     */
    public void setProgressDialogCancelListener(LOnProgressDialogCancelListener progressDialogCancelListener) {
        this.mOnProgressDialogCancelListener = progressDialogCancelListener;
    }

    /**
     * 初始化ProgressDialog并显示
     */
    private void initProgressDialog(){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);

            mProgressDialog.setMessage(mMessage);
            mProgressDialog.setCancelable(mCancelable);

            if (mCancelable && mOnProgressDialogCancelListener != null) {
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mOnProgressDialogCancelListener.onProgressDialogCancel();
                    }
                });
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    /**
     * 销毁ProgressDialog
     */
    private void dismissProgressDialog(){
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case MSG_DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：ProgressDialog取消监听</p>
     */
    public interface LOnProgressDialogCancelListener {
        void onProgressDialogCancel();
    }

}
