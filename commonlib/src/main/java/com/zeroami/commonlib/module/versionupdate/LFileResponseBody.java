package com.zeroami.commonlib.module.versionupdate;

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
 * <p>描述：定制ResponseBody，发送下载进度信息</p>
 */
public class LFileResponseBody extends ResponseBody {

    private Response originalResponse;
    private LDownloadProgressInfo mDownloadProgressInfo = new LDownloadProgressInfo();

    public LFileResponseBody(Response originalResponse) {
        this.originalResponse = originalResponse;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return originalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                mDownloadProgressInfo.setProgress(bytesReaded);
                mDownloadProgressInfo.setTotal(contentLength());
                LRxBus.getDefault().post(mDownloadProgressInfo);
                return bytesRead;
            }
        });
    }
}