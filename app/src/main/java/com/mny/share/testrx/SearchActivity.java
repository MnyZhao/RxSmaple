package com.mny.share.testrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * 联想搜索smaple
 */
public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "SEARCH";
    private EditText mEtSearch;
    private TextView mTvShowMsg, mTvShowRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        RxTextView.textChanges(mEtSearch).skip(1).flatMap(new Function<CharSequence, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(final CharSequence charSequence) throws Exception {
                return Observable.create(new ObservableOnSubscribe<CharSequence>() {
                    @Override
                    public void subscribe(ObservableEmitter<CharSequence> e) throws Exception {
                        e.onNext(charSequence.toString());
                    }
                });
            }
        });

    }

    private void initView() {
        mEtSearch = findViewById(R.id.et_search);
        mTvShowMsg = findViewById(R.id.tv_showsmsg);
        mTvShowRx = findViewById(R.id.tv_showrxsmsg);
        setWatch();
        rxAddWatecher();
    }

    /**
     * 原生添加观察事件
     */
    private void setWatch() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Toast.makeText(SearchActivity.this, charSequence.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTvShowMsg.setText(editable.toString());
            }
        });
    }

    private void rxAddWatecher() {
        /*
         * 说明
         * 1. 此处采用了RxBinding：RxTextView.textChanges(name) = 对对控件数据变更进行监听（功能类似TextWatcher），需要引入依赖：compile 'com.jakewharton.rxbinding2:rxbinding:2.0.0'
         * 2. 传入EditText控件，输入字符时都会发送数据事件（此处不会马上发送，因为使用了debounce（））
         *      debounce延迟发送
         * 3. 采用skip(1)原因：跳过 第1次请求 = 初始输入框的空字符状态
         **/
        RxTextView.textChanges(mEtSearch).debounce(1, TimeUnit.SECONDS).skip(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<CharSequence>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CharSequence charSequence) {
                mTvShowRx.setText(charSequence.toString());
                Log.e(TAG, "onNext: "+"搜索的文字 可以发送给服务器" );
                Toast.makeText(SearchActivity.this, charSequence.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        });
    }
}
