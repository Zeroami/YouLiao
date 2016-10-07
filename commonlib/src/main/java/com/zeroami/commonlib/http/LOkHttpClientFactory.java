package com.zeroami.commonlib.http;

import android.text.TextUtils;
import android.util.SparseArray;

import com.zeroami.commonlib.CommonLib;
import com.zeroami.commonlib.utils.LL;
import com.zeroami.commonlib.utils.LNetUtils;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：OkHttp工厂类</p>
 */
public class LOkHttpClientFactory {

    private static SparseArray<OkHttpClient> sClientPool = new SparseArray<>();
    private static Map<LClientConfig,OkHttpClient> sCustomClientPool = new HashMap<>();
    private static final String CACHE_DIRNAME = "cache_data";
    private static final int CACHE_SIZE = 10485760; // 10M
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;
    private static final int MAX_STALE = 60;    // 有网络时默认缓存的时间


    /**
     * 日志拦截器，打印日志
     */
    private static Interceptor ｍLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            String requestBody = "";
            try {
                Request newRequest = request.newBuilder().build();
                Buffer buffer = new Buffer();
                newRequest.body().writeTo(buffer);
                if (isPlaintext(buffer)) {
                    requestBody = buffer.readUtf8() + "\n\n" + newRequest.body().contentLength() + " byte body";
                }else{
                    requestBody = "binary " + newRequest.body().contentLength() + " byte body";
                }

            } catch (Exception e) {
            }
            LL.i(String.format("Sending request %s by %s%n%n%s%n%s",
                    request.url(), request.method(), request.headers(),requestBody));

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            String resonseBody = "";
            try {
                BufferedSource source = response.body().source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer().clone();
                if (isPlaintext(buffer)){
                    resonseBody = buffer.readString(Charset.forName("UTF-8")) + "\n\n" + response.body().contentLength() + " byte body";
                }else{
                    resonseBody = "binary " + buffer.size() + " byte body";
                }
            } catch (Exception e) {
            }
            LL.i(String.format("Received response for %s in %.1fms%n%n%s %s %s%n%n%s%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.protocol(), response.code(), response.message(), response.headers(), resonseBody));

            return response;
        }
    };

    private LOkHttpClientFactory() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取默认Client，离线可以缓存，在线就获取最新数据
     *
     * @return
     */
    public static OkHttpClient getDefaultClient() {
        return getClient(LClientType.TYPE_DEFAULT);
    }


    /**
     * 根据ClientType返回OkHttpClient
     *
     * @param clientType
     * @return
     */
    public static OkHttpClient getClient(int clientType) {
        OkHttpClient client = sClientPool.get(clientType);
        if (client == null) {
            synchronized (LOkHttpClientFactory.class) {
                if (client == null) {
                    client = createClient(clientType);
                    if (client != null) {
                        sClientPool.put(clientType, client);
                    }
                }
            }
        }
        return client;
    }


    /**
     * 根据ClientConfig返回定制的OkHttpClient
     * @param clientConfig  创建Client使用的配置
     * @return
     */
    public static OkHttpClient getCustomClient(LClientConfig clientConfig) {
        if (clientConfig == null){
            throw new RuntimeException("clientConfig is null");
        }
        OkHttpClient client = sCustomClientPool.get(clientConfig);
        if (client == null) {
            synchronized (LOkHttpClientFactory.class) {
                if (client == null) {
                    client = createCustomClient(clientConfig);
                    if (client != null) {
                        sCustomClientPool.put(clientConfig,client);
                    }
                }
            }
        }
        return client;
    }


    /**
     * 根据ClientType创建OkHttpClient
     *
     * @param clientType
     * @return
     */
    private static OkHttpClient createClient(int clientType) {
        OkHttpClient client = null;
        switch (clientType) {
            case LClientType.TYPE_DEFAULT:
                client = createDefaultClient();
                break;
            case LClientType.TYPE_CACHE_IN_MAX_STALE:
                client = createCacheInMaxStaleClient();
                break;
        }
        return client;
    }

    /**
     * 创建默认Client，离线可以缓存，在线就获取最新数据
     *
     * @return
     */
    private static OkHttpClient createDefaultClient() {
        return getCustomClient(LClientConfig.get());
    }

    /**
     * 创建带缓存Client，离线可以缓存，在线在max-stale时间内读取缓存，过期获取最新数据
     *
     * @return OkHttpClient
     */
    private static OkHttpClient createCacheInMaxStaleClient() {
        return getCustomClient(LClientConfig.get().clientType(LClientType.TYPE_CACHE_IN_MAX_STALE));
    }

    /**
     * 创建自定义Client
     * @param clientConfig
     * @return
     */
    private static OkHttpClient createCustomClient(final LClientConfig clientConfig) {
        File cacheDirectory = new File(CommonLib.getContext().getCacheDir().getAbsolutePath(), clientConfig.getCacheDirName());
        final Cache cache = new Cache(cacheDirectory, clientConfig.getCacheSize());
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String cacheControl = request.cacheControl().toString();
                if (!LNetUtils.isNetworkConnected()) {  // 没有网络，强制从缓存中读取
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                } else {
                    if (TextUtils.isEmpty(cacheControl)) {  // 如果用户没有设置
                        if (clientConfig.getClientType() == LClientType.TYPE_DEFAULT){  // 强制从网络获取
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_NETWORK)
                                    .build();
                        } else if(clientConfig.getClientType() == LClientType.TYPE_CACHE_IN_MAX_STALE){ // 在max-stale时间内读取缓存
                            request = request.newBuilder()
                                    .header("Cache-Control", "public, max-stale=" + clientConfig.getMaxStale()).removeHeader("Pragma")
                                    .build();
                        }
                    }
                }

                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(clientConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(clientConfig.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(clientConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(interceptor) // Network -> NetworkInterceptor -> Cache -> Interceptor , NetworkInterceptor更快执行，可用于修改定制Response的头信息
                .addInterceptor(ｍLoggingInterceptor)
                .cache(cache)
                .build();


        return client;
    }

    /**
     * 判断返回数据时候为文本类型
     * @param buffer
     * @return
     */
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    public static Interceptor getLoggingInterceptor() {
        return ｍLoggingInterceptor;
    }

    /**
     * 常量类，表示OkHttpClient的类型
     */
    public static class LClientType {
        public static final int TYPE_DEFAULT = 1;       // 默认Client，离线可以缓存，在线就获取最新数据
        public static final int TYPE_CACHE_IN_MAX_STALE = 2;  // 带缓存Client，离线可以缓存，在线在max-stale时间内读取缓存，过期获取最新数据
        // others ...
    }


    /**
     * <p>作者：Zeroami</p>
     * <p>邮箱：826589183@qq.com</p>
     * <p>描述：OkHttpClient配置</p>
     */
    public static class LClientConfig{
        private String cacheDirName;
        private int cacheSize;
        private int connectTimeout;
        private int readTimeout;
        private int writeTimeout;
        private int clientType;
        private int maxStale;

        private LClientConfig(){}

        public static LClientConfig get(){
            return new LClientConfig()
                    .cacheDirName(CACHE_DIRNAME)
                    .cacheSize(CACHE_SIZE)
                    .connectTimeout(CONNECT_TIMEOUT)
                    .readTimeout(READ_TIMEOUT)
                    .writeTimeout(WRITE_TIMEOUT)
                    .clientType(LClientType.TYPE_DEFAULT)
                    .maxStale(MAX_STALE);
        }

        public LClientConfig cacheDirName(String cacheDirName){
            if (cacheDirName != null){
                this.cacheDirName = cacheDirName;
            }
            return this;
        }

        public LClientConfig cacheSize(int cacheSize){
            this.cacheSize = cacheSize;
            return this;
        }

        public LClientConfig connectTimeout(int connectTimeout){
            this.connectTimeout = connectTimeout;
            return this;
        }

        public LClientConfig readTimeout(int readTimeout){
            this.readTimeout = readTimeout;
            return this;
        }

        public LClientConfig writeTimeout(int writeTimeout){
            this.writeTimeout = writeTimeout;
            return this;
        }

        public LClientConfig clientType(int clientType){
            this.clientType = clientType;
            return this;
        }

        public LClientConfig maxStale(int maxStale){
            this.maxStale = maxStale;
            return this;
        }

        public String getCacheDirName() {
            return cacheDirName;
        }

        public int getCacheSize() {
            return cacheSize;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public int getWriteTimeout() {
            return writeTimeout;
        }

        public int getClientType(){
            return clientType;
        }

        public int getMaxStale(){
            return maxStale;
        }


        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof LClientConfig){
                LClientConfig clientConfig = (LClientConfig) o;
                return this.cacheDirName.equals(clientConfig.cacheDirName)
                        && this.cacheSize == clientConfig.cacheSize
                        && this.connectTimeout == clientConfig.connectTimeout
                        && this.readTimeout == clientConfig.readTimeout
                        && this.writeTimeout == clientConfig.writeTimeout
                        && this.clientType == clientConfig.clientType
                        && this.maxStale == clientConfig.maxStale;
            }else{
                return false;
            }
        }
    }

}
