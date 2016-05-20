package com.rengwuxian.rxjavasamples.module.rxbus_13;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 来源：http://www.jianshu.com/p/ca090f6e2fe2
 * Created by trity on 17/5/16.
 */
public class RxBus {

    private static volatile RxBus INSTANCE;
    private final Subject<Object, Object> bus;

    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());//通过SerializedSubject来保持多线程安全
    }

    public static RxBus getInstance() {
        if (INSTANCE == null) {
            synchronized (RxBus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxBus();
                }
            }
        }
        return INSTANCE;
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * ofType=filter+cast
     * filter操作符可以使你提供一个指定的测试数据项，只有通过测试的数据才会被“发射”。
     * cast操作符可以将一个Observable转换成指定类型的Observable。
     */
    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 是否含有观察者
     */
    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
