package com.mny.share.testrx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mny.share.testrx.smaple.BackAsynchronousTest;
import com.mny.share.testrx.smaple.BackpressureFlowable;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "Test";
    private TextView mTvShowMsg;
    private Button mBtnRecive, mBtngGoAsync;
    private Button mBtnGoBackPressure,mBtnGoSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        RetorfitTest.request();
//        Loopers.ConditionalLoop();
//        Loopers.UnConditionalLoop();
//        ConnectionError.connectionErrorConn();
//        Nesting.nestingConnection();
//        BackpressureFlowable.backPressureTest();//背压控制接收数量
//        BackpressureFlowable.testError();//背压控制接收数量测试容量128显示bug
//        BackpressureFlowable.unBakcPressure();//无背压
        BackAsynchronousTest.unInfluencesTest();
    }

    private void initView() {
        mBtnRecive = (Button) findViewById(R.id.btn_receive);
        mBtnRecive.setOnClickListener(listener);
        mBtngGoAsync = (Button) findViewById(R.id.btn_goasync);
        mBtngGoAsync.setOnClickListener(listener);
        mBtnGoBackPressure=findViewById(R.id.btn_gobackress);
        mBtnGoBackPressure.setOnClickListener(listener);
        mBtnGoSearch=findViewById(R.id.btn_gosearch);
        mBtnGoSearch.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_receive:
                    BackpressureFlowable.reciveTest();
                    break;
                case R.id.btn_goasync:
                    Intent intent = new Intent(MainActivity.this, AsynchronousActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_gobackress:
                    Intent intent1 = new Intent(MainActivity.this, BackPressureActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btn_gosearch:
                    Intent intent2=new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    };

}
