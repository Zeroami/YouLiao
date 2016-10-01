package com.zeroami.commonlib.module.versionupdate;

import com.zeroami.commonlib.rx.rxbus.LRxBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：下载文件的回调，使用RxBus订阅下载过程进度信息</p>
 */
public abstract class LDownloadFileCallback implements Callback<ResponseBody> {
    /**
     * 订阅下载进度
     */
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    /**
     * 目标文件存储的文件夹路径
     */
    private String mDestFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String mDestFileName;

    public LDownloadFileCallback(String destFileDir, String destFileName) {
        this.mDestFileDir = destFileDir;
        this.mDestFileName = destFileName;
        subscribeLoadProgress();
    }
    /**
     * 成功后回调
     */
    public abstract void onSuccess(File file);

    /**
     * 下载过程回调
     */
    public abstract void onLoading(long progress, long total);

    /**
     * 请求成功后保存文件
     */
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 通过IO流写入文件
     */
    public File saveFile(Response<ResponseBody> response) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048];
        int len;
        try {
            File dir = new File(mDestFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            in = response.body().byteStream();
            File file = new File(dir, mDestFileName);
            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1){
                out.write(buf,0,len);
            }

            onSuccess(file);
            unSubscribe();
            return file;
        }finally {
            in.close();
            out.close();
        }
    }
    /**
     * 订阅文件下载进度
     */
    private void subscribeLoadProgress() {
        mCompositeSubscription.add(LRxBus.getDefault()
                .toObservable(LDownloadProgressInfo.class)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LDownloadProgressInfo>() {
                    @Override
                    public void call(LDownloadProgressInfo progressInfo) {
                        onLoading(progressInfo.getProgress(), progressInfo.getTotal());
                    }
                }));
    }
    /**
     * 取消订阅，防止内存泄漏
     */
    private void unSubscribe() {
        if (!mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}