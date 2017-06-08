package com.zeroami.commonlib.mvp;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：Api异常</p>
 */
public class LApiException extends RuntimeException {

    private int code;

    public LApiException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "LApiException{" +
                "code=" + code +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
