package com.rengwuxian.rxjavasamples.module.subject_12;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;

import rx.subjects.PublishSubject;

/**
 * 具体操作：http://www.jianshu.com/p/1257c8ba7c0c#
 * Created by trity on 17/5/16.
 */
public class SubjectDemoFragment extends BaseFragment {
    public static final String RX_BUS="Rx_BUS";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subject,container,false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        publishSubjectDemo();
    }
    private void publishSubjectDemo(){
        PublishSubject<String> publishSubject=PublishSubject.create();
//        publishSubject.onCompleted();//如果执行这个的话，将接受不到任何订阅消息
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fl_publish_left,PublishLeftFragment.newInstance(publishSubject,false))
                .replace(R.id.fl_publish_right,PublishRightFragment.newInstance(publishSubject,false))
                .commit();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fl_rxbus_left,PublishLeftFragment.newInstance(publishSubject,true))
                .replace(R.id.fl_rxbus_right,PublishRightFragment.newInstance(publishSubject,true))
                .commit();
    }

    @Override
    protected int getContentRes() {
        return R.string.dialog_subject;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_subject;
    }

    public static class TapEvent {
    }
}
