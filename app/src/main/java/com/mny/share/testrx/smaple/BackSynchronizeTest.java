package com.mny.share.testrx.smaple;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Crate by E470PD on 2018/8/21
 * 同步订阅的关系  观察者被观察者都再一个线程里工作
 * 同步订阅情况
 * 同步订阅中 被观察者FlowableEmitter.request =Subscription.request(i)
 * 被观察者通过FlowableEmitter.requested()获得观察者自身接收事件能力，从而根据该信息控制事件发送速度，
 * 从而达到了观察者反向控制被观察者的效果
 */
public class BackSynchronizeTest {
    public static final String TAG = "BackSynchronizeTest";

    //叠加性测试  观察者可连续要求接收事件，被观察者会进行叠加并一起发送
    public static void SuperimposedFlowable() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 调用emitter.requested()获取当前观察者需要接收的事件数量
                Log.d(TAG, "观察者可接收事件" + emitter.requested());
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(10);
                s.request(20);
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext:接收到了" + integer);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    //实时性测试  每次发送事件后，emitter.requested()会实时更新观察者能接受的事件
    //仅计算Next事件，complete & error事件不算
    public static void RealTimeFlowable() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 1. 调用emitter.requested()获取当前观察者需要接收的事件数量
                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());

                // 2. 每次发送事件后，emitter.requested()会实时更新观察者能接受的事件
                // 即一开始观察者要接收10个事件，发送了1个后，会实时更新为9个
                Log.d(TAG, "发送了事件 1");
                emitter.onNext(1);
                Log.d(TAG, "发送了事件1后, 还需要发送事件数量 = " + emitter.requested());

                Log.d(TAG, "发送了事件 2");
                emitter.onNext(2);
                Log.d(TAG, "发送事件2后, 还需要发送事件数量 = " + emitter.requested());

                Log.d(TAG, "发送了事件 3");
                emitter.onNext(3);
                Log.d(TAG, "发送事件3后, 还需要发送事件数量 = " + emitter.requested());

                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(10);
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "onNext:接收到了" + integer);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 异常
     * 当FlowableEmitter.requested()减到0时，则代表观察者已经不可接收事件
     * 此时被观察者若继续发送事件，则会抛出MissingBackpressureException异常
     * 如观察者可接收事件数量 = 1，当被观察者发送第2个事件时，就会抛出异常
     * 若观察者没有设置可接收事件数量，即无调用Subscription.request（）
     * 那么被观察者默认观察者可接收事件数量 = 0，即FlowableEmitter.requested()的返回值 = 0
     */
    public static void ErrorFlowable() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 1. 调用emitter.requested()获取当前观察者需要接收的事件数量
                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());

                // 2. 每次发送事件后，emitter.requested()会实时更新观察者能接受的事件
                // 即一开始观察者要接收10个事件，发送了1个后，会实时更新为9个
                Log.d(TAG, "发送了事件 1");
                emitter.onNext(1);
                Log.d(TAG, "发送了事件1后, 还需要发送事件数量 = " + emitter.requested());

                Log.d(TAG, "发送了事件 2");
                emitter.onNext(2);
                Log.d(TAG, "发送事件2后, 还需要发送事件数量 = " + emitter.requested());

                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
                s.request(1); // 设置观察者每次能接受1个事件
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
