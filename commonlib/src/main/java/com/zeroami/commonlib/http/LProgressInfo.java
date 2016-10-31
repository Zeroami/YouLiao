package com.zeroami.commonlib.http;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：上传下载进度信息</p>
 */
public class LProgressInfo {
    /**
     * 文件大小
     */
    long total;
    /**
     * 已上传下载大小
     */
    long progress;


    public LProgressInfo(){}

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

    public LProgressInfo(long total, long progress) {
        this.total = total;
        this.progress = progress;
    }
}