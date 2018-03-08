package com.hongchuangtech.enhancer;

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
                finish();
                // restore handler
                Thread.setDefaultUncaughtExceptionHandler(defaultHandler);
                // emit message
                WritableMap msg = Arguments.createMap();
                msg.putString("ModuleName", getIntent().getStringExtra(Constance.PARAMS_OUT_MODULE_NAME));
                HCTInstanceloaderModule
                        .getMainReactContext()
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("RNInstanceFailed", msg);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Thread.setDefaultUncaughtExceptionHandler(defaultHandler);
    }
}
