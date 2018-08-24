package com.mny.share.testrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mny.share.testrx.smaple.BackAsynchronousTest;

public class AsynchronousActivity extends AppCompatActivity {
    private Button mBtnSend, mBtnRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous);
        init();
        BackAsynchronousTest.influencesTest();
    }

    private void init() {
        mBtnRec = findViewById(R.id.btn_rec);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnRec.setOnClickListener(listener);
        mBtnSend.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_send:
                    BackAsynchronousTest.influencesTest();
                    break;
                case R.id.btn_rec:
                    BackAsynchronousTest.setRequest(48);
                    break;
            }
        }
    };
}
