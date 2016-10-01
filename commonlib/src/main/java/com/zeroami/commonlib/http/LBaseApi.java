package com.zeroami.commonlib.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：通用Api操作</p>
 */
public class LBaseApi {


    private static Map<String,Retrofit> sRetrofitMap = new HashMap<>(); // 子类共用

    protected LBaseApi(String baseUrl){
        Retrofit retrofit = sRetrofitMap.get(baseUrl);
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(createOkHttpClient())
                    .build();
            sRetrofitMap.put(baseUrl, retrofit);
        }
    }

    /**
     * 创建Retrofit使用的OkHttpClient
     * @return
     */
    protected OkHttpClient createOkHttpClient() {
        return LOkHttpClientFactory.getDefaultClient();
    }


    /**
     * 获取Retrofit实例
     * @param baseUrl
     * @return
     */
    protected Retrofit getRetrofit(String baseUrl) {
        return sRetrofitMap.get(baseUrl);
    }


}
