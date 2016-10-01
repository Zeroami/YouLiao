package com.zeroami.youliao.app;

import android.os.Looper;

import com.zeroami.commonlib.app.LBaseUncaughtExceptionHandler;
import com.zeroami.commonlib.utils.LT;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：自定义系统异常处理类</p>
 */
public class CrashHandler extends LBaseUncaughtExceptionHandler{


    @Override
    public boolean handleException(final Throwable ex) {
        new Thread() {
            public void run() {
                Looper.prepare();
                LT.show("出现异常：" + ex.getMessage());
                ex.printStackTrace();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
