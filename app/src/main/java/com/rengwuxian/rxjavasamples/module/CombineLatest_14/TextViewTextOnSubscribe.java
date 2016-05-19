package com.rengwuxian.rxjavasamples.module.CombineLatest_14;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

/**
 * Created by trity on 18/5/16.
 */
public class TextViewTextOnSubscribe implements Observable.OnSubscribe<CharSequence> {
    private final TextView view;

    public TextViewTextOnSubscribe(TextView view) {
        this.view = view;
    }

    @Override public void call(final Subscriber<? super CharSequence> subscriber) {
        checkUiThread();

        final TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(s);
                }
            }

            @Override public void afterTextChanged(Editable s) {
            }
        };
        view.addTextChangedListener(watcher);

        subscriber.add(new MainThreadSubscription() {
            @Override protected void onUnsubscribe() {
                view.removeTextChangedListener(watcher);
            }
        });

        // Emit initial value.
        subscriber.onNext(view.getText());
    }
}
