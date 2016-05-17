package com.rengwuxian.rxjavasamples.module.subject_12;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rengwuxian.rxjavasamples.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by trity on 17/5/16.
 */
public class PublishRightFragment extends Fragment {
    @Bind(R.id.textView2)
    TextView textView2;
    public PublishSubject<String> publishSubject;


    public static PublishRightFragment newInstance(PublishSubject<String> publishSubject) {

        Bundle args = new Bundle();

        PublishRightFragment fragment = new PublishRightFragment();
        fragment.publishSubject=publishSubject;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_text, container, false);
        ButterKnife.bind(this, view);

        publishSubject.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                textView2.setText(s);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
