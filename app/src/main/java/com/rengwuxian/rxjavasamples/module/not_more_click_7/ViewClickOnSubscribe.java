package com.rengwuxian.rxjavasamples.module.not_more_click_7;

/**
 * Created by trity on 13/5/16.
 */

import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

/**
 * 仿造rxbinding写的防止btn在短时间的多次点击
 * 通过setOnClickListener是否为null来达到目的
 * bugs:自己写的，会在第一次点击的时候进行监听，所以点击次数要跟rxbinding多一次
 * Created by trity on 13/5/16.
 */
public final class ViewClickOnSubscribe<T> implements Observable.OnSubscribe<T> {
    final View view;

    public ViewClickOnSubscribe(View view) {
        this.view = view;
    }

    @Override public void call(final Subscriber<? super T> subscriber) {
        checkUiThread();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        view.setOnClickListener(listener);

        subscriber.add(new MainThreadSubscription() {
            @Override protected void onUnsubscribe() {
                view.setOnClickListener(null);
            }
        });
    }
}