package com.hongchuangtech.enhancer;

import android.os.Bundle;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;

/**
 * Created by henry on 2018/3/7.
 */

public class HCTReactActivity extends ReactActivity {

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new HCTReactActivityDelegate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Thread.UncaughtExceptionHandler defaultHandler;

    @Override
    protected void onResume() {
        super.onResume();
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Log.e("UNCatched Exception", throwable.getMessage());
                throwable.printStackTrace();
                HCTReactActivity activity = HCTReactActivity.this;
                activity.setResult(RESULT_CANCELED);
                activity.finish();
                // restore handler
                Thread.setDefaultUncaughtExceptionHandler(activity.defaultHandler);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Thread.setDefaultUncaughtExceptionHandler(defaultHandler);
    }
}
