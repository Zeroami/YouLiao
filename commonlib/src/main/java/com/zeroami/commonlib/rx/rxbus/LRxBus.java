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

    public static final LNull NULL = new LNull();
    public static final Class<LNull> NULL_TYPE = LNull.class;

    private static volatile LRxBus sInstance;
    private final Subject<Object, Object> mSubject;

    private final Map<Class<?>, Object> mStickyEventMap;
    private final Map<String, Object> mStickyActionEventMap;

    public LRxBus() {
        // PublishSubject: 只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
        mSubject = new SerializedSubject<>(PublishSubject.create());
        mStickyEventMap = new ConcurrentHashMap<>();
        mStickyActionEventMap = new ConcurrentHashMap<>();
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
     * 发送一个带action的事件
     *
     * @param event
     * @param action
     */
    public void post(Object event, String action) {
        mSubject.onNext(new LRxBusEvent(action, event));
    }

    /**
     * 发送一个带action的空事件
     *
     * @param action
     */
    public void postAction(String action) {
        mSubject.onNext(new LRxBusEvent(action, NULL));
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
     * 关注事件类型且满足action标志
     *
     * @param eventType
     * @param action
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(final Class<T> eventType, final String action) {
        return mSubject.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                if (!(o instanceof LRxBusEvent)) return false;
                LRxBusEvent event = (LRxBusEvent) o;
                return eventType.isInstance(event.getData()) && action != null && action.equals(event.getAction());
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
     * 仅关注action对应的事件
     *
     * @param action
     * @return
     */
    public Observable<LNull> toActionObservable(String action) {
        return toObservable(NULL_TYPE, action);
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
     * 发送一个带action的Sticky事件
     * @param event
     * @param action
     */
    public void postSticky(Object event, String action) {
        synchronized (mStickyActionEventMap) {
            mStickyActionEventMap.put(event.getClass().toString() + action, event);
        }
        post(event, action);
    }

    /**
     * 发送一个带action的Sticky空事件
     * @param action
     */
    public void postActionSticky(String action) {
        synchronized (mStickyActionEventMap) {
            mStickyActionEventMap.put(NULL.getClass().toString() + action, NULL);
        }
        post(NULL, action);
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
     * 关注事件类型且满足action标志
     *
     * @param eventType
     * @param action
     * @param <T>
     * @return
     */
    public <T> Observable<T> toStickyObservable(final Class<T> eventType, final String action) {
        synchronized (mStickyActionEventMap) {
            Observable<T> observable = toObservable(eventType, action);
            final Object event = mStickyActionEventMap.get(eventType.toString() + action);

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
     * 仅关注action对应的事件
     *
     * @param action
     * @return
     */
    public Observable<LNull> toActionStickyObservable(String action) {
        return toStickyObservable(NULL_TYPE, action);
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
     * 根据eventType和action获取Sticky事件
     *
     * @param eventType
     * @param action
     * @param <T>
     * @return
     */
    public <T> T getStickyEvent(Class<T> eventType, String action) {
        synchronized (mStickyActionEventMap) {
            return eventType.cast(mStickyActionEventMap.get(eventType.toString() + action));
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
     * @param action
     * @param <T>
     * @return
     */
    public <T> T removeStickyEvent(Class<T> eventType, String action) {
        synchronized (mStickyActionEventMap) {
            return eventType.cast(mStickyActionEventMap.remove(eventType.toString() + action));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
        synchronized (mStickyActionEventMap) {
            mStickyActionEventMap.clear();
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
