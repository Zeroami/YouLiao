package com.zeroami.commonlib.http;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：上传下载进度监听器</p>
 */
public interface LProgressListener {
    /**
     * 上传下载进度
     * @param currentBytesCount
     * @param totalBytesCount
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}