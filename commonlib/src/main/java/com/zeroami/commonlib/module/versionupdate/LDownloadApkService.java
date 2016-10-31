package com.zeroami.commonlib.module.versionupdate;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.zeroami.commonlib.http.LHttpLoggingInterceptor;
import com.zeroami.commonlib.http.LOkHttpClientFactory;
import com.zeroami.commonlib.http.LProgressCallback;
import com.zeroami.commonlib.http.LProgressResponseBody;
import com.zeroami.commonlib.rx.rxbus.LRxBus;
import com.zeroami.commonlib.utils.LAppUtils;
import com.zeroami.commonlib.utils.LL;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Apk下载服务</p>
 */
public class LDownloadApkService extends Service {

    /**
     * 下载时发生的事件
     */
    public static final String EVENT_APK_DOWNLOAD_PROGRESS = "event_apk_download_progress";
    public static final String EVENT_APK_DOWNLOAD_SUCCESS = "event_apk_download_success";
    public static final String EVENT_APK_DOWNLOAD_LOADING = "event_apk_download_loading";
    public static final String EVENT_APK_DOWNLOAD_FAILURE = "event_apk_download_failure";

    /**
     * 下载参数key
     */
    public static final String EXTRA_REQUEST_URL = "extra_request_url";
    public static final String EXTRA_DEST_FILE_NAME = "extra_dest_file_name";
    public static final String EXTRA_DEST_FILE_DIR = "extra_dest_file_dir";
    public static final String EXTRA_NOTIFICATION_SMALL_ICON = "extra_notification_small_icon";
    public static final String EXTRA_NOTIFICATION_CONTENT_TITLE = "extra_notification_content_title";

    /**
     * 下载参数value
     */
    private String mRequestUrl;
    private String mDestFileName;
    private String mDestFileDir;
    private int mNotificationSmallIcon;
    private String mNotificationContentTitle;
    private String mRequestUrlDirPart;
    private String mRequestUrlFilePart;

    private Context mContext;
    private int mPreProgress = 0;
    private static final int NOTIFY_ID = 1000;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager notificationManager;
    private Retrofit.Builder mRetrofitBuilder;

    /**
     * 开启服务
     *
     * @param context                  Context
     * @param requestUrl               请求地址
     * @param destFileDir              Apk保存目录
     * @param destFileName             Apk保存名称
     * @param notificationSmallIcon    通知小图标
     * @param notificationContentTitle 通知标题
     */
    public static void start(Context context, String requestUrl, String destFileDir, String destFileName, int notificationSmallIcon, String notificationContentTitle) {
        Intent intent = new Intent(context, LDownloadApkService.class);
        intent.putExtra(LDownloadApkService.EXTRA_REQUEST_URL, requestUrl);
        intent.putExtra(LDownloadApkService.EXTRA_DEST_FILE_DIR, destFileDir);
        intent.putExtra(LDownloadApkService.EXTRA_DEST_FILE_NAME, destFileName);
        intent.putExtra(LDownloadApkService.EXTRA_NOTIFICATION_SMALL_ICON, notificationSmallIcon);
        intent.putExtra(LDownloadApkService.EXTRA_NOTIFICATION_CONTENT_TITLE, notificationContentTitle);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        if (intent != null) {
            initData(intent);
            downloadFile();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initData(Intent intent) {
        mRequestUrl = intent.getStringExtra(EXTRA_REQUEST_URL);
        mDestFileName = intent.getStringExtra(EXTRA_DEST_FILE_NAME);
        mDestFileDir = intent.getStringExtra(EXTRA_DEST_FILE_DIR);
        mNotificationSmallIcon = intent.getIntExtra(EXTRA_NOTIFICATION_SMALL_ICON, 0);
        mNotificationContentTitle = intent.getStringExtra(EXTRA_NOTIFICATION_CONTENT_TITLE);

        mRequestUrlDirPart = mRequestUrl.substring(0, mRequestUrl.lastIndexOf("/") + 1);
        mRequestUrlFilePart = mRequestUrl.substring(mRequestUrl.lastIndexOf("/") + 1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private interface LDownloadService {
        @GET("{requestUrlFilePart}")
        Call<ResponseBody> downloadFile(@Path("requestUrlFilePart") String requestUrlFilePart);
    }

    /**
     * 下载文件
     */
    private void downloadFile() {
        initNotification();
        if (mRetrofitBuilder == null) {
            mRetrofitBuilder = new Retrofit.Builder();
        }
        LL.i("Start download apk , request url : " + mRequestUrl);
        mRetrofitBuilder.baseUrl(mRequestUrlDirPart)
                .client(initOkHttpClient())
                .build()
                .create(LDownloadService.class)
                .downloadFile(mRequestUrlFilePart)
                .enqueue(new LProgressCallback(mDestFileDir, mDestFileName, EVENT_APK_DOWNLOAD_PROGRESS) {

                    @Override
                    public void onSuccess(File file) {
                        LL.i("Download apk success");
                        LRxBus.getDefault().post(file, EVENT_APK_DOWNLOAD_SUCCESS);
                        cancelNotification();
                        LAppUtils.installApk(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        updateNotification(progress * 100 / total);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LL.i("Download apk failure");
                        LRxBus.getDefault().post(t, EVENT_APK_DOWNLOAD_FAILURE);
                        cancelNotification();
                    }
                });
    }


    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new LProgressResponseBody(originalResponse, EVENT_APK_DOWNLOAD_PROGRESS))
                        .build();
            }
        });
        builder.addInterceptor(new LHttpLoggingInterceptor());
        return builder.build();
    }

    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        mNotificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(mNotificationSmallIcon)
                .setContentText("0%")
                .setContentTitle(mNotificationContentTitle)
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, mNotificationBuilder.build());
    }

    /**
     * 更新通知
     *
     * @param progress
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (mPreProgress < currProgress) {
            mNotificationBuilder.setContentText(progress + "%");
            mNotificationBuilder.setProgress(100, (int) progress, false);
            notificationManager.notify(NOTIFY_ID, mNotificationBuilder.build());
        }
        mPreProgress = (int) progress;
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }
}