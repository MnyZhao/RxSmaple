package com.mny.share.testrx.smaple;

import android.content.Intent;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Crate by E470PD on 2018/8/20
 * 背压策略  一种 控制事件流速 的策略
 * 背压：
 * 作用：
 * 在 异步订阅关系 中，控制事件发送 & 接收的速度
 * 注：背压的作用域 = 异步订阅关系，即 被观察者 & 观察者处在不同线程中
 * 应用场景：
 * 被观察者发送事件速度 与 观察者接收事件速度 不匹配的场景
 * 具体场景就取决于 该事件的类型，如：网络请求，那么具体场景：有很多网络请求需要执行
 * ，但执行者的执行度没那么快，此时就需要使用背压策略来进行控制。
 * 解决的问题
 * 解决了 因被观察者发送事件速度 与 观察者接收事件速度 不匹配（一般是前者 快于 后者），从而
 * 导致观察者无法及时响应 / 处理所有 被观察者发送事件 的问题、
 * 背压的实现形式
 * Flowable 支持背压模式
 * 实现方式（Flowable<T> create(FlowableOnSubscribe<T> source, BackpressureStrategy mode)
 * BackpressureStrategy.mode类型:
 * BackPressureStrategy.ERROR;  直接抛出异常MissingBackpressureException
 * BackpressureStrategy.MISSING; 友好提示缓存区满了
 * BackpressureStrategy.BUFFER; 将缓存区大小设置成无限大 被观察者可无限发送事件 观察者可无限接收事件
 * BackpressureStrategy.DROP;     超过缓存区（128）的事件将被丢弃
 * BackpressureStrategy.LATEST;  只保留最新的事件超过缓存区（128）的将被丢弃
 */
public class BackpressureFlowable {
    private static final String TAG = "BackpressureFlowable";
    static Subscription subscription = null;

    //网上的教程说背压等于异步 这我特么不知道还得去探索一下
    public static void backPressureTest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Log.e(TAG, "发送成功: 第1个事件");
                e.onNext(2);
                Log.e(TAG, "发送成功: 第2个事件");
                e.onNext(3);
                Log.e(TAG, "发送成功: 第3个事件");
                e.onNext(4);
                Log.e(TAG, "发送成功: 第4个事件");
                e.onNext(5);
                Log.e(TAG, "发送成功: 第5个事件");
                e.onNext(6);
                Log.e(TAG, "发送成功: 第6个事件");
                Log.e(TAG, "发送完成");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        // 对比Observer传入的Disposable参数，Subscriber此处传入的参数 = Subscription
                        // 相同点：Subscription参数具备Disposable参数的作用，即Disposable.dispose()切断连接, 同样的调用Subscription.cancel()切断连接
                        // 不同点：Subscription增加了void request(long n)
                        s.request(3);
                        // 作用：决定观察者能够接收多少个事件
                        // 如设置了s.request(3)，这就说明观察者能够接收3个事件（多出的事件存放在缓存区）
                        // 官方默认推荐使用Long.MAX_VALUE，即s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext:接收成功 " + "第" + integer + "个事件");
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

    public static void cancel() {
        if (!(null == subscription)) {
            subscription.cancel();
        }
    }

    public static void testFlowable() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
                e.onNext(6);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
//                Log.d(TAG, "onNext: "+integer);
                System.out.println("BackpressureFlowable.onNext" + integer);
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
     * 没有背压处理 发送事件快过接收事件 会引起内存溢出
     */
    public static void unBakcPressure() {
        //发送事件与接收事件不匹配 引入了背压策略
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    Log.e(TAG, "发送了事件" + i);
                    Thread.sleep(10);
                    // 发送事件速度：10ms / 个
                    e.onNext(i);
                }
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "Test.onNext" + integer + "---time:" + System.currentTimeMillis());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.toString());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    //背压溢出测试
    public static Subscription reciveSub;

    public static void reciveTest() {
        reciveSub.request(Long.MAX_VALUE);
    }

    public static void testError() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 150; i++) {
                    Log.e(TAG, "发送了事件" + i);
                    e.onNext(i);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
                reciveSub = s;
                // 默认不设置可接收事件大小
            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG, "接收到了事件" + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        });
    }

    /*背压模式smaple*/
    public static void modeEorrorSmaple() {
        // 创建被观察者Flowable
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                // 发送 129个事件
                for (int i = 0; i < 129; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR) // 设置背压模式 = BackpressureStrategy.ERROR
                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
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
    public static void modeMissingSmaple() {
        // 创建被观察者Flowable
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                // 发送 129个事件
                for (int i = 0; i < 129; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.MISSING) // 设置背压模式 = BackpressureStrategy.MISSING
                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
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
    public static void modeBufferSmaple() {
        // 创建被观察者Flowable
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                // 发送 129个事件
                for (int i = 1; i < 130; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER) // 设置背压模式 = BackpressureStrategy.BUFFER
                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
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
    public static Subscription mSubscriptionDrop;
    public static void modeDropSmaple() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 发送150个事件
                for (int i = 0; i < 150; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.DROP)      // 设置背压模式 = BackpressureStrategy.DROP
                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscriptionDrop = s;
                        // 通过按钮进行接收事件
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

    public static Subscription mSubscriptionLatest;
    public static void modeLatestSmaple() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 150; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST) // // 设置背压模式 = BackpressureStrategy.LATEST
                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscriptionLatest = s;
                        // 通过按钮进行接收事件
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
