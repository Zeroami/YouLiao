package com.zeroami.commonlib.http;

import com.zeroami.commonlib.rx.rxbus.LRxBus;

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
    private String mEventTag;
    private LProgressInfo mDownloadProgressInfo = new LProgressInfo();

    public LProgressResponseBody(Response originalResponse,String eventTag) {
        this.mOriginalResponse = originalResponse;
        this.mEventTag = eventTag;
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
            long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                mDownloadProgressInfo.setProgress(bytesReaded);
                mDownloadProgressInfo.setTotal(contentLength());
                LRxBus.getDefault().post(mDownloadProgressInfo,mEventTag);
                return bytesRead;
            }
        });
    }
}