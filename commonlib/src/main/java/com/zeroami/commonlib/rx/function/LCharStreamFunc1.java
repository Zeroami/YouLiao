package com.zeroami.commonlib.rx.function;

import java.io.Reader;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：将ResponseBody转换为Reader的Func1</p>
 */
public class LCharStreamFunc1 implements Func1<ResponseBody,Reader> {
    @Override
    public Reader call(ResponseBody responseBody) {
        return responseBody.charStream();
    }
}
