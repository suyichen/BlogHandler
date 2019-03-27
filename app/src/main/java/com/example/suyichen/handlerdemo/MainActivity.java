package com.example.suyichen.handlerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suyichen.utils.CountDownTimerUtil;

/**
 * @author suyichen
 */
public class MainActivity extends AppCompatActivity {


    private final static long OLD_TOTAL_TIME = 1000 * 20;
    private final static long NEW_TOTAL_TIME = 1000 * 30;

    CountDownTimerUtil timerUtil = null;
    private TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.timeView);

        initTimer();
    }

    private void initTimer() {
        timerUtil = new CountDownTimerUtil(OLD_TOTAL_TIME,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeView.setText("" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"time is done",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void startTimer(View view) {
        timerUtil.start();
    }

    public void restartTimer(View view) {
        if (timerUtil.isRunning()) {
            timerUtil.cancel();
        }
        timerUtil.setMillisInFuture(NEW_TOTAL_TIME);
        timerUtil.start();

    }
}
