package com.zeroami.commonlib.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：定制ResponseBody，发送下载进度信息的事件</p>
 */
public class LProgressResponseBody extends ResponseBody {

    private Response mOriginalResponse;
    //进度回调接口
    private final LProgressListener mProgressListener;

    public LProgressResponseBody(Response originalResponse, LProgressListener progressListener) {
        this.mOriginalResponse = originalResponse;
        this.mProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mOriginalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return mOriginalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(mOriginalResponse.body().source()) {
            long bytesReadedCount = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReadedCount += bytesRead == -1 ? 0 : bytesRead;
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                mProgressListener.onProgress(bytesReadedCount,totalBytesCount);
                return bytesRead;
            }
        });
    }
}