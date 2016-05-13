package com.rengwuxian.rxjavasamples.module.not_more_click_7;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;

/**
 * 参考：http://www.jianshu.com/p/dc63a4b636fa
 * Created by trity on 13/5/16.
 */
public class NotMoreClickFragment extends BaseFragment {

    @Bind(R.id.btn_click)
    Button btnClick;
    @Bind(R.id.btn_click_utils)
    Button btnClickUtils;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_throttle_first, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_click)
    public void onclick(View view) {
        subscription = Observable.create(new ViewClickOnSubscribe(view))
                .throttleFirst(10, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.d("click", "click rxbinding");
                    }
                });
    }
    @OnClick(R.id.btn_click_utils)
    public void onclickUtils(View view) {
       if(!NoMutilClickUtils.isMutiClick(-1)){
           Log.d("click", "click utils");
       }
    }

    @Override
    protected int getDialogRes() {
        return 0;
    }

    @Override
    protected int getTitleRes() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
