package com.mny.share.testrx.smaple;

import android.util.Log;

import com.mny.share.testrx.getretorfit.RetorfitInstanceLooper;
import com.mny.share.testrx.model.Translation;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;


/**
 * Crate by E470PD on 2018/8/17
 * 网络请求嵌套回调
 */
public class Nesting {
    public static String TAG="RxJavaNesting";
    interface Request_Register {
        @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20register")
        Observable<Translation> getCall();
    }

    interface Request_Login {
        @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20login")
        Observable<Translation> getCall();
    }

    public static void nestingConnection() {
        Request_Login request_login = RetorfitInstanceLooper.getInstance()
                .getRetorfit().create(Request_Login.class);
        Request_Register request_register = RetorfitInstanceLooper.getInstance()
                .getRetorfit().create(Request_Register.class);
        Observable observable_register=request_register.getCall();
        final Observable observable_login=request_login.getCall();
        observable_register.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Translation>() {
            @Override
            public void accept(Translation o) throws Exception {
                Log.d(TAG, "accept: "+o.show());
            }
        }).observeOn(Schedulers.io()).flatMap(new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                return observable_login;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Translation>() {
            @Override
            public void accept(Translation o) throws Exception {
                Log.d(TAG, "accept: "+o.show());
            }
        });
    }
}
