package com.rengwuxian.rxjavasamples.module.interval_11;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by trity on 17/5/16.
 */
public class IntervalFragment extends BaseFragment {
    @Bind(R.id.tv_show)
    TextView tvShow;
    @Bind(R.id.btn_rx_interval)
    Button btnRxInterval;
    @Bind(R.id.btn_rx_timer)
    Button btnRxTimer;
    @Bind(R.id.btn_timer)
    Button btnTimer;
    @Bind(R.id.btn_ScheduledThreadPoolExecutor)
    Button btnScheduledThreadPoolExecutor;
    @Bind(R.id.btn_close)
    Button btnClose;

    private int count;
    private Timer timer;
    private ScheduledThreadPoolExecutor exec;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interval, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void autoIntervalChanage() {
        unsubscribe();
        subscription = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        tvShow.setText(""+(count += aLong));
                    }
                });
    }

    public void autoRxTimerChanage() {
        unsubscribe();
        subscription = Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        tvShow.setText(""+(count += aLong));

                    }
                });
    }

    /**
     * 所创建的定时器是互补影响的，这跟java的timer很大区别。
     */
    public void autoScheduledChanage() {
        if (exec == null || exec.isShutdown()) {
            exec = new ScheduledThreadPoolExecutor(1);
            exec.scheduleAtFixedRate(new Runnable() {//每隔一段时间就触发异常
                @Override
                public void run() {
                    Log.d(TAG, "run: " + (count++));
                }
            }, 1, 5, TimeUnit.SECONDS);//延迟1s,间隔5s，触发一次
        /*exec.scheduleAtFixedRate(new Runnable(){//每隔一段时间打印系统时间，证明两者是互不影响的
            @Override
            public void run() {
                Log.d(TAG, "run: "+(SystemClock.currentThreadTimeMillis()));
            }}, 1000, 2000, TimeUnit.MILLISECONDS);*/
        }

    }

    public void autoTimerChanage() {
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "timer run: "+ count++);
            }
        }, 3 * 1000);//默认只能milliseconds
    }

    private void closeAllTimer(){
        if(timer!=null)
            timer.cancel();
        if(exec!=null&&!exec.isShutdown())
            exec.shutdownNow();
        unsubscribe();
    }

    @OnClick({R.id.btn_rx_interval, R.id.btn_rx_timer, R.id.btn_timer, R.id.btn_ScheduledThreadPoolExecutor,R.id.btn_close})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rx_interval:
                autoIntervalChanage();
                break;
            case R.id.btn_rx_timer:
                autoRxTimerChanage();
                break;
            case R.id.btn_timer:
                autoTimerChanage();
                break;
            case R.id.btn_ScheduledThreadPoolExecutor:
                autoScheduledChanage();
                break;
            case R.id.btn_close:
                closeAllTimer();
                break;
        }
    }

    @Override
    protected int getContentRes() {
        return R.string.title_interval;
    }

    @Override
    protected int getTitleRes() {
        return R.string.dialog_interval;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
