package com.zeroami.commonlib.rx.function;

import java.io.IOException;

import rx.functions.Func1;
import okhttp3.ResponseBody;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：将ResponseBody转换为String的Func1</p>
 */
public class LStringFunc1 implements Func1<ResponseBody, String> {
    @Override
    public String call(ResponseBody responseBody) {
        try {
            return responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
