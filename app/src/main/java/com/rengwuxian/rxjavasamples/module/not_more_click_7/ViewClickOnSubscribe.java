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
 * Created by trity on 13/5/16.
 */
public final class ViewClickOnSubscribe implements Observable.OnSubscribe<Void> {
    final View view;

    public ViewClickOnSubscribe(View view) {
        this.view = view;
    }

    @Override public void call(final Subscriber<? super Void> subscriber) {
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