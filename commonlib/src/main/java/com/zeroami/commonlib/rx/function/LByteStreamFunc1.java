package com.zeroami.commonlib.rx.function;

import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：将ResponseBody转换为InputStream的Func1</p>
 */
public class LByteStreamFunc1 implements Func1<ResponseBody,InputStream> {

    @Override
    public InputStream call(ResponseBody responseBody) {
        return responseBody.byteStream();
    }
}
