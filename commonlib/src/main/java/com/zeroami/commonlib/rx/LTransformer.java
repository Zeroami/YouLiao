package com.zeroami.commonlib.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：RxJava的转换器</p>
 */
public class LTransformer {

    private static final Observable.Transformer schedulersTransformer = new  Observable.Transformer() {
        @Override public Object call(Object observable) {
            return ((Observable)  observable).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * 应用线程调度，io线程处理数据，主线程处理结果
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }
}
