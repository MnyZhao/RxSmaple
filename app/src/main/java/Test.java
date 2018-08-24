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
//        Test();
        TestFlowable();
    }

    private static void Test() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                System.out.println("Test.subscribe" + 1);
                e.onNext(2);
                System.out.println("Test.subscribe" + 2);
                e.onNext(3);
                System.out.println("Test.subscribe" + 3);
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("Test.onNext" + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Test.onError");
            }

            @Override
            public void onComplete() {
                System.out.println("Test.onComplete");
            }
        };
        observable.subscribe(observer);
    }
    public static void TestFlowable(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                System.out.println("发送了事件1");
                emitter.onNext(1);
                System.out.println("发送了事件2");
                emitter.onNext(2);
                System.out.println("发送了事件3");
                emitter.onNext(3);
                emitter.onComplete();
            }
        },BackpressureStrategy.ERROR).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("Test.onNext");
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
