package com.zeroami.commonlib.rx.rxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * <p>作者：Zeroami</p>
 * <p>邮箱：826589183@qq.com</p>
 * <p>描述：基于Rxjava实现的EventBus</p>
 */
public class LRxBus {

    public static final Object EMPTY = new Object();
    public static final Class<Object> EMPTY_TYPE = Object.class;

    private static volatile LRxBus sInstance;
    private final Subject<Object, Object> mSubject;

    private final Map<Class<?>, Object> mStickyEventMap;
    private final Map<String, Object> mStickyTagEventMap;

    public LRxBus() {
        // PublishSubject: 只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
        mSubject = new SerializedSubject<>(PublishSubject.create());
        mStickyEventMap = new ConcurrentHashMap<>();
        mStickyTagEventMap = new ConcurrentHashMap<>();
    }

    public static LRxBus getDefault() {
        if (sInstance == null) {
            synchronized (LRxBus.class) {
                if (sInstance == null) {
                    sInstance = new LRxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 发送一个事件
     *
     * @param event
     */
    public void post(Object event) {
        mSubject.onNext(event);
    }

    /**
     * 发送一个带tag的事件
     *
     * @param event
     * @param tag
     */
    public void post(Object event, String tag) {
        mSubject.onNext(new LRxBusEvent(tag, event));
    }

    /**
     * 发送一个带tag的空事件
     *
     * @param tag
     */
    public void postTag(String tag) {
        mSubject.onNext(new LRxBusEvent(tag, EMPTY));
    }

    /**
     * 仅关注事件的类型
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mSubject.ofType(eventType);
    }

    /**
     * 关注事件类型且满足tag标志
     *
     * @param eventType
     * @param tag
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final Class<T> eventType, final String tag) {
        return mSubject.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                if (!(o instanceof LRxBusEvent)) return false;
                LRxBusEvent event = (LRxBusEvent) o;
                return eventType.isInstance(event.getData()) && tag != null && tag.equals(event.getTag());
            }
        }).map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                LRxBusEvent event = (LRxBusEvent) o;
                return (T) event.getData();
            }
        });
    }

    /**
     * 仅关注tag对应的事件
     *
     * @param tag
     * @return
     */
    public Observable<Object> toTagObservable(String tag) {
        return toObservable(EMPTY_TYPE, tag);
    }


    /**
     * 发送一个Sticky事件
     *
     * @param event
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 发送一个带tag的Sticky事件
     * @param event
     * @param tag
     */
    public void postSticky(Object event, String tag) {
        synchronized (mStickyTagEventMap) {
            mStickyTagEventMap.put(event.getClass().toString() + tag, event);
        }
        post(event, tag);
    }

    /**
     * 发送一个带tag的Sticky空事件
     * @param tag
     */
    public void postTagSticky(String tag) {
        synchronized (mStickyTagEventMap) {
            mStickyTagEventMap.put(EMPTY.getClass().toString() + tag, EMPTY);
        }
        post(EMPTY, tag);
    }

    /**
     * 仅关注事件的类型
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toStickyObservable(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = toObservable(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 关注事件类型且满足tag标志
     *
     * @param eventType
     * @param tag
     * @param <T>
     * @return
     */
    public <T> Observable<T> toStickyObservable(final Class<T> eventType, final String tag) {
        synchronized (mStickyTagEventMap) {
            Observable<T> observable = toObservable(eventType, tag);
            final Object event = mStickyTagEventMap.get(eventType.toString() + tag);

            if (event != null) {
                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 仅关注tag对应的事件
     *
     * @param tag
     * @return
     */
    public Observable<Object> toTagStickyObservable(String tag) {
        return toStickyObservable(EMPTY_TYPE, tag);
    }

    /**
     * 根据eventType获取Sticky事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 根据eventType和tag获取Sticky事件
     *
     * @param eventType
     * @param tag
     * @param <T>
     * @return
     */
    public <T> T getStickyEvent(Class<T> eventType, String tag) {
        synchronized (mStickyTagEventMap) {
            return eventType.cast(mStickyTagEventMap.get(eventType.toString() + tag));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     *
     * @param eventType
     * @param tag
     * @param <T>
     * @return
     */
    public <T> T removeStickyEvent(Class<T> eventType, String tag) {
        synchronized (mStickyTagEventMap) {
            return eventType.cast(mStickyTagEventMap.remove(eventType.toString() + tag));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
        synchronized (mStickyTagEventMap) {
            mStickyTagEventMap.clear();
        }
    }

    /**
     * 判断是否有订阅者
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

    /**
     * 解除单例对象引用
     */
    public void reset() {
        sInstance = null;
    }


}
