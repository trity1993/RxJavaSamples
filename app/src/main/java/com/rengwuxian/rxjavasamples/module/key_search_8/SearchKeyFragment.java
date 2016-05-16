package com.rengwuxian.rxjavasamples.module.key_search_8;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;
import com.rengwuxian.rxjavasamples.adapter.ZhuangbiListAdapter;
import com.rengwuxian.rxjavasamples.model.ZhuangbiImage;
import com.rengwuxian.rxjavasamples.network.Network;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 使用debounce来控制edittext上关键字搜索的时间间隔
 * <p/>
 * 详细看http://www.jianshu.com/p/33c548bce571 这个的重试机制无法实现
 * <p/>
 * 使用http://www.jianshu.com/p/023a5f60e6d0# 这种方式的重试机制
 * <p/>
 * 扩展：http://www.jianshu.com/p/b24e69da7bbb 本地搜索的实现，
 * 使用handlerThread 取消上一个搜索，缓存搜索结果，时间内无新的，进行输出队列
 * <p/>
 * Created by trity on 16/5/16.
 */
public class SearchKeyFragment extends BaseFragment {

    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.btn_intelligence_search)
    Button btnIntelligenceSearch;

    private boolean isIntelliGen;

    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    Observer<List<ZhuangbiImage>> observer = new Observer<List<ZhuangbiImage>>() {
        @Override
        public void onCompleted() {
            Log.e(TAG, "onError: ");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ZhuangbiImage> images) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(images);
        }
    };

    @OnTextChanged(R.id.et_search)
    void onTextChanage(CharSequence s, int start, int before, int count) {
        if (isIntelliGen)
            intelligenceSearch(s.toString());
        else
            search(s.toString());
    }

    /**
     * 对传入key进行频率过滤后，switchMap取消上一个的请求
     *
     * @param key
     */
    private void search(final String key) {
        subscription = Observable.create(new Observable.OnSubscribe<String>() {//自定义创建Observable

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(key);
            }
        })
                .debounce(3000, TimeUnit.MILLISECONDS)//过滤搜索的频率
                .filter(new Func1<String, Boolean>() {//过滤不符合的字体
                    @Override
                    public Boolean call(String s) {

                        //当 EditText 中文字大于0的时候
                        return s.length() > 0;
                    }
                })
                .switchMap(new Func1<String, Observable<List<ZhuangbiImage>>>() {//执行异步搜索
                    @Override
                    public Observable<List<ZhuangbiImage>> call(String s) {
                        return Network.getZhuangbiApi().search(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        adapter.setImages(null);
                        swipeRefreshLayout.setRefreshing(true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 结合retryWhen加入重试的机制
     * 关于时间间隔使用的是二进制指数退避算法，记性延长时间
     *
     * @param key
     */
    private void intelligenceSearch(final String key) {
        subscription = Observable.create(new Observable.OnSubscribe<String>() {//自定义创建Observable

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(key);
            }
        })
                .debounce(600, TimeUnit.MILLISECONDS)//过滤搜索的频率
                .filter(new Func1<String, Boolean>() {//过滤不符合的字体
                    @Override
                    public Boolean call(String s) {

                        //当 EditText 中文字大于0的时候
                        return s.length() > 0;
                    }
                })
                .switchMap(new Func1<String, Observable<List<ZhuangbiImage>>>() {//执行异步搜索
                    @Override
                    public Observable<List<ZhuangbiImage>> call(String s) {
                        return Network.getZhuangbiApi().search(s);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {//使用这种方式，发生错误的时候，总是执行3次重试，第4次默认调用onCompleted结束
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> errors) {

                        return errors.zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Integer>() {
                            @Override
                            public Integer call(Throwable throwable, Integer i) {
                                Log.d(TAG, "call: " + i + ",Throwable:" + throwable);
                                return i;
                            }
                        }).flatMap(new Func1<Integer, Observable<? extends Long>>() {
                            @Override
                            public Observable<? extends Long> call(Integer retryCount) {

                                return Observable.timer((long) Math.pow(5, retryCount), TimeUnit.SECONDS);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        adapter.setImages(null);
                        swipeRefreshLayout.setRefreshing(true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 本地搜索，使用HandlerThread进行实现
     */
    private void localSearch() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debounce, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);

        return view;
    }

    @OnClick({R.id.btn_search,R.id.btn_intelligence_search})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_search:
                isIntelliGen=false;
                break;
            case R.id.btn_intelligence_search:
                isIntelliGen=true;
                break;
        }
    }

    @Override
    protected int getContentRes() {
        return R.string.dialog_debounce;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_debounce;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
