package com.rengwuxian.rxjavasamples.module.rxbus_13;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by trity on 17/5/16.
 */
public class RxBus {
    private static final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());//通过SerializedSubject来保持多线程安全

    public static void send(Object o) {
        bus.onNext(o);
    }

    /**
     * 获取实际的Bus对象
     */
    public static Observable<Object> toObserverable() {
        return bus;
    }

    /**
     * 是否含有观察者
     */
    public static boolean hasObservers() {
        return bus.hasObservers();
    }
}
