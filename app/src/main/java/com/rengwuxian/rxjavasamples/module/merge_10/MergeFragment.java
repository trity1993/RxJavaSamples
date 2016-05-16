package com.rengwuxian.rxjavasamples.module.merge_10;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * merge操作符
 * 可以将多个Observables的输出合并，就好像它们是一个单个的Observable一样
 * <p/>
 * Demo:模拟先读取(1s)本地缓存数据，再读取(3s)网络数据
 *
 * 跟Concat的区别在于
 * merge是交错的，concat则按顺序执行.
 * 但是数据并不好测试
 *
 * 对比zip
 * 发现zip比较灵活，及时得到的容器list不同，但提供方法转换成相同，
 * 而concat和merge做不到，所获得的容器一定相同
 * 来源：https://github.com/cn-ljb/rxjava_for_android/blob/master/app/src/main/java/com/che58/ljb/rxjava/fragment/MergeFragment.java
 * Created by zjh on 2016/3/26.
 */
public class MergeFragment extends BaseFragment {
    private static final String LOCATION = "location:";
    @Bind(R.id.tv_show)
    TextView tvShow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merge, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mergeDemo();
    }

    private void mergeDemo() {
        Observable.merge(
                getDataFromLocation(),
                getDataFromNet()
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Contacter>>() {
                    @Override
                    public void call(List<Contacter> contacters) {
                        initPage(contacters);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private void initPage(List<Contacter> contacters) {
        StringBuffer stringBuffer=new StringBuffer();
        for (Contacter tmp : contacters) {
            stringBuffer.append(tmp.toString()+"\n");
        }
        tvShow.setText(stringBuffer.toString());
    }

    private Observable<List<Contacter>> getDataFromNet() {
        return Observable.create(new Observable.OnSubscribe<List<Contacter>>() {
            @Override
            public void call(Subscriber<? super List<Contacter>> subscriber) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ArrayList<Contacter> contacters = new ArrayList<>();
                contacters.add(new Contacter("net:Zeus"));
                contacters.add(new Contacter("net:Athena"));
                contacters.add(new Contacter("net:Prometheus"));
                subscriber.onNext(contacters);
                subscriber.onCompleted();
            }
        });
    }


    private Observable<List<Contacter>> getDataFromLocation() {
        return Observable.create(new Observable.OnSubscribe<List<Contacter>>() {
            @Override
            public void call(Subscriber<? super List<Contacter>> subscriber) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<Contacter> datas = new ArrayList<>();
                datas.add(new Contacter(LOCATION + "张三"));
                datas.add(new Contacter(LOCATION + "李四"));
                datas.add(new Contacter(LOCATION + "王五"));

                subscriber.onNext(datas);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    protected int getContentRes() {
        return R.string.dialog_merge;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_merge;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
