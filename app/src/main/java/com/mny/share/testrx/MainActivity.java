package com.mny.share.testrx;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mny.share.testrx.connection_interface.GetRequest_InferfaceTranslate;
import com.mny.share.testrx.getretorfit.RetorfitInstanceLooper;
import com.mny.share.testrx.model.Translation;
import com.mny.share.testrx.smaple.BackpressureFlowable;
import com.mny.share.testrx.smaple.ConnectionError;
import com.mny.share.testrx.smaple.Loopers;
import com.mny.share.testrx.smaple.Nesting;
import com.mny.share.testrx.smaple.RetorfitTest;

import java.util.concurrent.TimeUnit;

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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        RetorfitTest.request();
//        Loopers.ConditionalLoop();
//        Loopers.UnConditionalLoop();
//        ConnectionError.connectionErrorConn();
//        Nesting.nestingConnection();
        BackpressureFlowable.backPressureTest();//背压控制接收数量
//        BackpressureFlowable.unBakcPressure();//无背压
    }
}
