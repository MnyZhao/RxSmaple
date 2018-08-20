package com.mny.share.testrx.getretorfit;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Crate by E470PD on 2018/8/16
 */
public class RetorfitInstanceLooper {
    private static RetorfitInstanceLooper instance;

    public RetorfitInstanceLooper() {

    }

    public static RetorfitInstanceLooper getInstance() {
        if (instance == null) {
            instance = new RetorfitInstanceLooper();
        }
        return instance;
    }

    Retrofit retrofit = null;

    public Retrofit getRetorfit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                    .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                    .build();
        }
        return retrofit;
    }
}
