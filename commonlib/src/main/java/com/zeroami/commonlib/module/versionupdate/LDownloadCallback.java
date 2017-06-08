package com.zeroami.commonlib.module.versionupdate;

import com.zeroami.commonlib.http.LProgressInfo;
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
 * <p>描述：下载文件的回调</p>
 */
public abstract class LDownloadCallback implements Callback<ResponseBody> {
    /**
     * 目标文件存储的文件夹路径
     */
    private String mDestFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String mDestFileName;

    public LDownloadCallback(String destFileDir, String destFileName) {
        this.mDestFileDir = destFileDir;
        this.mDestFileName = destFileName;
    }
    /**
     * 成功后回调
     */
    public abstract void onSuccess(File file);

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
            return file;
        }finally {
            in.close();
            out.close();
        }
    }
}