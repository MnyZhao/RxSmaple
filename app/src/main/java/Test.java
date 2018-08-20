import android.util.Log;


import com.mny.share.testrx.smaple.BackpressureFlowable;

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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Crate by E470PD on 2018/8/20
 */
public class Test {
    public static String TAG = "Test";

    public static void main(String[] args) {
        BackpressureFlowable.backPressureTest();
        BackpressureFlowable.testFlowable();

    }
}
