package com.rengwuxian.rxjavasamples.module.subject_12;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rengwuxian.rxjavasamples.R;
import com.rengwuxian.rxjavasamples.module.rxbus_13.RxBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subjects.PublishSubject;

/**
 * Created by trity on 17/5/16.
 */
public class PublishLeftFragment extends Fragment {
    public PublishSubject<String> publishSubject;
    @Bind(R.id.button)
    Button button;
    boolean isRxbus;

    public static PublishLeftFragment newInstance(PublishSubject<String> publishSubject,boolean isRxbus) {

        Bundle args = new Bundle();
        args.putBoolean(SubjectDemoFragment.RX_BUS,isRxbus);

        PublishLeftFragment fragment = new PublishLeftFragment();
        fragment.publishSubject = publishSubject;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        isRxbus=bundle.getBoolean(SubjectDemoFragment.RX_BUS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_btn, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.button)
    void onClick(View view) {
        if(isRxbus){
            if(RxBus.hasObservers()){
                RxBus.send(new SubjectDemoFragment.TapEvent());
            }
        }else{
            publishSubject.onNext(button.getText().toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
