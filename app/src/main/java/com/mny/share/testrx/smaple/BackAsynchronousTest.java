package com.mny.share.testrx.smaple;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Crate by E470PD on 2018/8/21
 * 异步订阅的关系  被观察者与观察者不再同一个线程工作
 * 异步订阅情况
 * 被观察者FlowableEmitter.request()的返回值=被观察者线程中Rxjava内部调用
 * 的request(n) 中n 的值{128， 96， 0}(从而每次发送128/96/0 个事件给观察者)
 * 观察者requested()的返回值= 观察者线程中的request(a)中a的值
 */
public class BackAsynchronousTest {
    public static final String TAG = "BackAsynchronousTest";

    /**
     * 证明观察者Subscription.request(150)不影响被观察者中FlowableEmitter.requested();
     * 由于二者处于不同线程，所以被观察者 无法通过 FlowableEmitter.requested()知道观察者自身接收事件能力
     * 即 被观察者不能根据 观察者自身接收事件的能力 控制发送事件的速度。
     */
    public static void unInfluencesTest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                // 调用emitter.requested()获取当前观察者需要接收的事件数量
                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());

            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        s.request(150);
                        // 该设置仅影响观察者线程中的requested，却不会影响的被观察者中的FlowableEmitter.requested()的返回值
                        // 因为FlowableEmitter.requested()的返回值 取决于RxJava内部调用request(n)，而该内部调用会在一开始就调用request(128)
                        // 为什么是调用request(128)下面再讲解
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

    }

    /**
     * 而在异步订阅关系中，反向控制的原理是：
     * 通过RxJava内部固定调用被观察者线程中的request(n) 从而 反向控制被观察者的发送事件速度
     */
    public static Subscription mSubscription;

    /**
     * 控制接收数量
     * @param num
     */
    public static void setRequest(int num){
        mSubscription.request(num);
    }
    public static void influencesTest() {
        // 被观察者：一共需要发送500个事件，但真正开始发送事件的前提 = FlowableEmitter.requested()返回值 ≠ 0
        // 观察者：每次接收事件数量 = 48（点击按钮）
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());
                boolean flag; //设置标记位控制

                // 被观察者一共需要发送500个事件
                for (int i = 0; i < 500; i++) {
                    flag = false;

                    // 若requested() == 0则不发送
                    while (emitter.requested() == 0) {
                        if (!flag) {
                            Log.d(TAG, "不再发送");
                            flag = true;
                        }
                    }
                    // requested() ≠ 0 才发送
                    Log.d(TAG, "发送了事件" + i + "，观察者可接收事件数量 = " + emitter.requested());
                    emitter.onNext(i);


                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                        // 初始状态 = 不接收事件；通过点击按钮接收事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }
}
