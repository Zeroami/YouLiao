package com.zeroami.commonlib.module.versionupdate;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：下载进度信息</p>
 */
public class LDownloadProgressInfo {
    /**
     * 文件大小
     */
    long total;
    /**
     * 已下载大小
     */
    long progress;


    public LDownloadProgressInfo(){}

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public long getTotal() {
        return total;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public LDownloadProgressInfo(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}