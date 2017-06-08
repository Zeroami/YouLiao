package com.zeroami.commonlib.http;

import com.zeroami.commonlib.utils.LL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Http请求日志拦截器</p>
 */
public class LHttpLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestBody = "";
        try {
            Buffer buffer = new Buffer();
            RequestBody body = request.body();
            boolean isMultipartBody = body instanceof MultipartBody;
            if (!isMultipartBody){
                body.writeTo(buffer);
            }
            if (!isMultipartBody && isPlaintext(buffer)) {
                requestBody = buffer.clone().readUtf8() + "\n\n" + body.contentLength() + " byte body";
            } else {
                requestBody = "binary " + body.contentLength() + " byte body";
            }
        } catch (Exception e) {
        }
        LL.i(String.format("Sending request %s by %s%n%n%s%n%s",
                request.url(), request.method(), request.headers(), requestBody));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String responseBody = "";
        try {
            ResponseBody body = response.body();
            boolean isProgressBody = body instanceof LProgressResponseBody;
            Buffer buffer = null;
            if (!isProgressBody){
                BufferedSource source = body.source();
                source.request(Long.MAX_VALUE);
                buffer = source.buffer().clone();
            }
            if (!isProgressBody && isPlaintext(buffer)) {
                responseBody = buffer.readString(Charset.forName("UTF-8")) + "\n\n" + body.contentLength() + " byte body";
            } else {
                responseBody = "binary " + body.contentLength() + " byte body";
            }
        } catch (Exception e) {
        }
        LL.i(String.format("Received response for %s in %.1fms%n%n%s %s %s%n%n%s%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.protocol(), response.code(), response.message(), response.headers(), responseBody));
        if (isGoodJson(responseBody)) {
            LL.json(responseBody);
        }
        return response;
    }


    /**
     * 判断返回数据时候为文本类型
     *
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

    /**
     * 判断字符串是否为json格式
     *
     * @param json
     * @return
     */
    private static boolean isGoodJson(String json) {

        try {
            new JSONObject(json.trim());
            return true;
        } catch (JSONException e) {
            System.out.println("bad json: " + json);
            return false;
        }
    }
}
