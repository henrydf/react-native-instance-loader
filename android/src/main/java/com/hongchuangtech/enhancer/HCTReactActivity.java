package com.hongchuangtech.enhancer;

import android.content.Intent;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hongchuangtech.Constance;
import com.hongchuangtech.HCTInstanceloaderModule;

/**
 * Created by henry on 2018/3/7.
 */

public class HCTReactActivity extends ReactActivity {

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new HCTReactActivityDelegate(this);
    }

    private Thread.UncaughtExceptionHandler defaultHandler;

    @Override
    protected void onResume() {
        super.onResume();
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                // print log
                Log.e("UNCaught Exception", throwable.getMessage());
                throwable.printStackTrace();
                setResult(Constance.ALT_RN_ACTIVITY_RESULT_FAILED, getIntent());
                finish();
                // restore handler
                Thread.setDefaultUncaughtExceptionHandler(defaultHandler);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Thread.setDefaultUncaughtExceptionHandler(defaultHandler);
    }
}
