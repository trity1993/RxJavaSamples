package com.rengwuxian.rxjavasamples.module.buffer_9;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;
import com.rengwuxian.rxjavasamples.module.not_more_click_7.ViewClickOnSubscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * buffer操作符
 * 统计规定时间内，相应的数量。如：时间内，点击的次数，摇一摇的次数等
 * 分发：对数组的数据进行切割并分发，适当跳过一些指定的数据
 *
 * 注意Window跟Buffer相似
 * 区别在于Window为Observable缓存，而Buffer为list
 * Created by trity on 16/5/16.
 */
public class BufferFragment extends BaseFragment {
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.btn_buffer_count)
    Button btnBufferCount;
    @Bind(R.id.btn_buffer_time)
    Button btnBufferTime;
    @Bind(R.id.tv_buffer_skip)
    TextView tvBufferSkip;
    @Bind(R.id.editText)
    EditText editText;
    @Bind(R.id.btn_buffer_skip)
    Button btnBufferSkip;
    @Bind(R.id.tv_buffer_skip_show)
    TextView tvBufferSkipShow;

    private int click=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buffer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_buffer_count)
    public void bufferCount(View v){
        subscription=Observable.create(new ViewClickOnSubscribe(v))
                .buffer(3)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Void>>() {
                    @Override
                    public void call(List<Void> voids) {
                        Toast.makeText(getActivity(),"每统计到3次点击事件，发送一次数据",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 不是rxbinding的方式，很难达到效果
     * @param v
     */
    @OnClick(R.id.btn_buffer_time)
    public void bufferTime(View v){
        subscription=Observable.create(new ViewClickOnSubscribe(v))
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> voids) {
                        Log.d(TAG, "call: "+voids.size());
                    }
                });
    }
    @OnClick(R.id.btn_buffer_skip)
    public void bufferSkip(View v){
        char[] cs = editText.getText().toString().trim().toCharArray();
        Character[] chs = new Character[cs.length];
        for (int i = 0; i < chs.length; i++) {
            chs[i] = cs[i];
        }

        subscription=Observable
                .from(chs)
                .buffer(2,3)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Character>>() {
                    @Override
                    public void call(List<Character> characters) {
                        tvBufferSkipShow.setText(characters.toString());
                    }
                });
    }

    @Override
    protected int getContentRes() {
        return R.string.dialog_buffer;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_buffer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
