package com.mny.share.testrx.smaple;

import android.util.Log;

import com.mny.share.testrx.connection_interface.GetRequest_InferfaceTranslate;
import com.mny.share.testrx.getretorfit.RetorfitInstanceLooper;
import com.mny.share.testrx.model.Translation;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Crate by E470PD on 2018/8/17
 * 网络请求循环  1无条件2 有条件
 */
public class Loopers {
    public static String TAG="RxJavaLooper";
    //无条件循环
    public static void UnConditionalLoop(){
        /**
         * 第一次开始前等待的时间
         * 每次等待的时间
         */
        Log.d(TAG,""+System.currentTimeMillis());
        Observable.interval(2, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "第 " + aLong + " 次轮询"+System.currentTimeMillis());
                        GetRequest_InferfaceTranslate requestInferface = RetorfitInstanceLooper.getInstance().getRetorfit().create(GetRequest_InferfaceTranslate.class);
                        Observable<Translation> observable = requestInferface.getCall();
                        observable.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Translation>() {
                                    @Override
                                    public void onSubscribe(Disposable disposable) {

                                    }

                                    @Override
                                    public void onNext(Translation translation) {
                                        Log.d(TAG, "onNext: "+translation.show());

                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Log.d(TAG, "onError: "+throwable.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Long aLong) {
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    public static void ConditionalLoop(){
        /**
         * 如果不想设置固定次数 那直接用interval
         * start 开始位置 比如5  就是从5 开始  5.6.7.8.9
         * count 重复次数
         * 第一次开始前等待的时间
         * 每次等待的时间
         */
        Log.d(TAG,""+System.currentTimeMillis());
        Observable.intervalRange(5,5,2, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "第 " + aLong + " 次轮询"+System.currentTimeMillis());
                        GetRequest_InferfaceTranslate requestInferface = RetorfitInstanceLooper.getInstance().getRetorfit().create(GetRequest_InferfaceTranslate.class);
                        Observable<Translation> observable = requestInferface.getCall();
                        observable.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Translation>() {
                                    @Override
                                    public void onSubscribe(Disposable disposable) {

                                    }

                                    @Override
                                    public void onNext(Translation translation) {
                                        Log.d(TAG, "onNext: "+translation.show());

                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Log.d(TAG, "onError: "+throwable.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Long aLong) {
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
