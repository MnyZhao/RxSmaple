package com.mny.share.testrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mny.share.testrx.smaple.BackpressureFlowable;

public class BackPressureActivity extends AppCompatActivity {
    private Button mBtnError, mBtnMissing, mBtnBuffer, mBtnDrop, mBtnLatest;
    private Button mBtnDropReceve, mBtnLatestReceve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure);
        initView();
    }

    private void initView() {
        mBtnError = findViewById(R.id.btn_error);
        mBtnMissing = findViewById(R.id.btn_missing);
        mBtnBuffer = findViewById(R.id.btn_buffer);
        mBtnDrop = findViewById(R.id.btn_drop);
        mBtnLatest = findViewById(R.id.btn_latest);
        mBtnDropReceve = findViewById(R.id.btn_drop_receve);
        mBtnLatestReceve = findViewById(R.id.btn_latest_receve);

        mBtnError.setOnClickListener(listener);
        mBtnMissing.setOnClickListener(listener);
        mBtnBuffer.setOnClickListener(listener);
        mBtnDrop.setOnClickListener(listener);
        mBtnLatest.setOnClickListener(listener);
        mBtnDropReceve.setOnClickListener(listener);
        mBtnLatestReceve.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_error:
                    BackpressureFlowable.modeEorrorSmaple();
                    break;
                case R.id.btn_missing:
                    BackpressureFlowable.modeMissingSmaple();
                    break;
                case R.id.btn_buffer:
                    BackpressureFlowable.modeBufferSmaple();
                    break;
                case R.id.btn_drop:
                    BackpressureFlowable.modeDropSmaple();
                    break;
                case R.id.btn_latest:
                    BackpressureFlowable.modeLatestSmaple();
                    break;
                case R.id.btn_drop_receve:
                    BackpressureFlowable.mSubscriptionDrop.request(128);
                    break;
                case R.id.btn_latest_receve:
                    BackpressureFlowable.mSubscriptionLatest.request(128);
                    break;
            }
        }
    };
}
