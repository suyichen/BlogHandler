package com.example.suyichen.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 *
 * @author suyichen
 * @date 2019/3/17 0017
 */

public abstract class CountDownTimerUtil {

    private long mMillisInFuture;
    private long mCountdownInterval;
    private long mStopTimeInFuture;

    private boolean mCancelled = false;

    private static final int MSG = 1;
    private static final int MSG_DEFAULT = 0;

    private Message mMsg;
    private boolean mIsRunning = false;

    public CountDownTimerUtil(long millisInFuture,long countDownInterval){
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    public synchronized final void setCountdownInterval(long countdownInterval){
        mCountdownInterval = countdownInterval;
    }

    public synchronized final void setMillisInFuture(long millisInFuture){
        mMillisInFuture = millisInFuture;
    }

    public synchronized final void cancel(){
        mHandler.removeMessages(MSG);
        mCancelled = true;
        if(mMsg != null){
            mMsg.what = MSG_DEFAULT;
        }
        mIsRunning = false;
    }

    public synchronized final CountDownTimerUtil start(){
        if(mMillisInFuture <= 0){
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mMsg = null;
        mMsg = mHandler.obtainMessage(MSG);
        mHandler.sendMessage(mMsg);
        mCancelled = false;
        mIsRunning = true;
        return this;
    }

    public synchronized boolean isRunning(){
        return mIsRunning;
    }

    /**
     * Fixed interval execution
     *
     * @param millisUntilFinished interval
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Timer is Finish
     */
    public abstract void onFinish();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            synchronized (CountDownTimerUtil.this){
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                if (millisLeft <= 0){
                    onFinish();
                    msg.what = MSG_DEFAULT;
                    if (mMsg != null){
                        mMsg.what = MSG_DEFAULT;
                    }
                    mIsRunning = false;
                }else if(millisLeft < mCountdownInterval){
                    mMsg = null;
                    mMsg = obtainMessage(MSG);
                    sendMessageDelayed(mMsg,millisLeft);
                }else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();
                    while(delay < 0) {
                        delay += mCountdownInterval;
                    }
                    if (!mCancelled){
                        mMsg = null;
                        mMsg = obtainMessage(MSG);
                        sendMessageDelayed(mMsg,delay);
                    }

                }
            }
        }
    };
}
