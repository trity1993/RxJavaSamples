package com.rengwuxian.rxjavasamples;

import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 封装CompositeSubscription来进行管理Subscription的生命周期
 * 参考：
 * http://blog.csdn.net/lzyzsd/article/details/45033611
 * http://www.jianshu.com/p/47e72693a302#
 * Created by trity on 18/5/16.
 */
public class BaseActivity extends AppCompatActivity {
    private static final CompositeSubscription compositeSubscription=new CompositeSubscription();

    /**
     * 加入hashset容器
     * @param subscription
     */
    public static void addSubscription(Subscription subscription){
        compositeSubscription.add(subscription);
    }

    /**
     * 取消所有的订阅
     */
    public static void finishAllSubscription(){
        compositeSubscription.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAllSubscription();
    }
}
