package com.example.jeremychen.bookreader;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {

    public static final int TIME_CODE = 001;
    public static final int TOTLA_TIME = 3000;
    public static final int INTERVAL_TIME = 1000;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();

        final Myhandler handler = new Myhandler(this);
        Message message =  Message.obtain();
        message.what = TIME_CODE;
        message.arg1 = TOTLA_TIME;
        handler.sendMessage(message);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListActivity.start(SplashActivity.this);
                finish();
                handler.removeMessages(TIME_CODE);
            }
        });
    }

    private void initUI() {
        textView = (TextView) findViewById(R.id.text_view_skip);
    }
    private static class Myhandler extends Handler{
        //weak reference!

        public final WeakReference<SplashActivity> mWeakReference;

        private Myhandler(SplashActivity activity) {
            this.mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity activity = mWeakReference.get();
            if(msg.what == TIME_CODE)
            {
                int time = msg.arg1;
                if(activity != null)
                {
                    activity.textView.setText(time / INTERVAL_TIME + "秒  点击跳过");
                    Message message = Message.obtain();
                    message.what = TIME_CODE;
                    message.arg1 = time - 1000;
                    if(time > 0)
                    {
                        sendMessageDelayed(message, INTERVAL_TIME);
                    }
                    else
                    {
                        BookListActivity.start(activity);
                        activity.finish();
                        // TODO: 2018/7/5 进入主界面
                    }
                }
            }
        }
    }
}
