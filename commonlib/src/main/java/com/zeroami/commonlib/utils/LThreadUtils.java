package com.zeroami.commonlib.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：线程工具类</p>
 */
public class LThreadUtils {


    private final static Handler MAIN_LOOPER_HANDLER = new Handler(Looper.getMainLooper());

    private final static ExecutorService EXECUTOR = Executors.newCachedThreadPool(new LInnerThreadFactory());

    public static void doInBackground(final Runnable r) {
        doInBackground(r, null);
    }

    public static void doInUiThread(Runnable r) {
        doInUiThread(r, null);
    }

    public static void doInUiThread(Runnable r, long delay) {
        doInUiThread(r, delay, null);
    }

    public static void doInBackground(final Runnable r, long delay) {
        doInBackground(r, delay, null);
    }

    public static void doInBackground(final Runnable r, LDebugger debugger) {
        EXECUTOR.execute(new LDebuggerRunnable(r, new LDebuggerWrapper(debugger)));
    }

    public static void doInUiThread(Runnable r, LDebugger debugger) {
        MAIN_LOOPER_HANDLER.post(new LDebuggerRunnable(r, new LDebuggerWrapper(debugger)));
    }

    public static void doInUiThread(Runnable r, long delay, LDebugger debugger) {
        MAIN_LOOPER_HANDLER.postDelayed(new LDebuggerRunnable(r, new LDebuggerWrapper(debugger)), delay);
    }

    public static void doInBackground(final Runnable r, long delay, final LDebugger debugger) {
        MAIN_LOOPER_HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                doInBackground(new LDebuggerRunnable(r, new LDebuggerWrapper(debugger)));
            }
        }, delay);
    }

    public static String getName() {
        return Thread.currentThread().getName();
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：线程工厂类</p>
     */
    private static class LInnerThreadFactory implements ThreadFactory {

        private long mThreadId = 0;

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "LThreadUtils#<" + (mThreadId++) + ">");
        }
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：带调试的任务</p>
     */
    private static class LDebuggerRunnable implements Runnable {

        Runnable mRunnable = null;

        private LDebugger mDebugger;

        private LDebuggerRunnable(Runnable r, LDebugger debugger) {
            if (r == null) {
                throw new NullPointerException("LDebuggerRunnable | Runnable must not be null");
            }

            mRunnable = r;
            mDebugger = debugger;
        }

        @Override
        public void run() {
            if (mDebugger != null)
                mDebugger.onPreExecute();

            mRunnable.run();

            if (mDebugger != null)
                mDebugger.onPostExecute();
        }

    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：线程任务调试类</p>
     */
    public static abstract class LDebugger {
        /**
         * execute in execute thread
         */
        protected void onPreExecute() {

        }

        /**
         * execute in execute thread
         */
        protected void onPostExecute() {

        }

        /**
         * execute in execute thread
         */
        protected abstract String getMessage();
    }

    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：线程任务调试的装饰类</p>
     */
    private static class LDebuggerWrapper extends LDebugger {

        private LDebugger mDebugger;

        private long mThreadStartTime;


        private LDebuggerWrapper(LDebugger debugger) {
            this.mDebugger = debugger;
        }

        @Override
        protected void onPreExecute() {
            mThreadStartTime = System.currentTimeMillis();

            if (mDebugger != null) {
                mDebugger.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute() {
            if (mDebugger != null) {
                mDebugger.onPostExecute();
            }

            LL.i(getMessage() + " useTime=" + (System.currentTimeMillis() - mThreadStartTime)
                    + "ms " + " currentTime=" + System.currentTimeMillis());
        }

        @Override
        protected String getMessage() {
            String msg = "ThreadName=" + getName();

            if (mDebugger == null) {
                return msg;
            }

            String debuggerMsg = "";

            try {
                debuggerMsg = mDebugger.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return msg + " | " + debuggerMsg;
        }
    }
}
