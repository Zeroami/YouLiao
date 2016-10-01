package com.zeroami.commonlib.rx.function;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：将ResponseBody转换为byte[]的Func1</p>
 */
public class LBytesFunc1 implements Func1<ResponseBody,byte[]> {
    @Override
    public byte[] call(ResponseBody responseBody) {
        try {
            return responseBody.bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
