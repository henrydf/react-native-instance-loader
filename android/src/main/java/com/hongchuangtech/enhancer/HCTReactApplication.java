package com.hongchuangtech.enhancer;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hongchuangtech.Constance;
import com.hongchuangtech.HCTInstanceloaderModule;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by henry on 2018/3/7.
 */

public abstract class HCTReactApplication extends Application {

    abstract public List<ReactPackage> getNativePackages(@Nullable String newRNInstanceMainModuleName);

    private ReactActivity firstReactActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifeCycleAdapter() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                if (firstReactActivity == null && activity instanceof ReactActivity) {
                    firstReactActivity = (ReactActivity)activity;
                    setup();
                }
            }
        });
    }

    private void setup() {
        try {
            Method method = ReactActivity.class.getDeclaredMethod("getReactInstanceManager");
            method.setAccessible(true);
            final ReactInstanceManager instanceManager = (ReactInstanceManager)method.invoke(firstReactActivity);
            ReactContext ctx = instanceManager.getCurrentReactContext();
            if (ctx != null) {
                HCTInstanceloaderModule.setMainReactContext(ctx);
                listenForRNInstanceFailed(instanceManager, ctx);
            } else {
                instanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                    @Override
                    public void onReactContextInitialized(ReactContext context) {
                        HCTInstanceloaderModule.setMainReactContext(context);
                        listenForRNInstanceFailed(instanceManager, context);
                        instanceManager.removeReactInstanceEventListener(this);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForRNInstanceFailed(ReactInstanceManager instanceManager, ReactContext ctx) {
        RNActivityEventHandler adapter = new RNActivityEventHandler(instanceManager, ctx);
        ctx.addActivityEventListener(adapter);
        ctx.addLifecycleEventListener(adapter);
    }



}

class ActivityLifeCycleAdapter implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}

class RNActivityEventHandler implements ActivityEventListener, LifecycleEventListener {

    private ReactInstanceManager mInstanceManager;
    private ReactContext ctx;
    private Bundle previousFailedResult;

    public RNActivityEventHandler(ReactInstanceManager mInstanceManager, ReactContext ctx) {
        this.mInstanceManager = mInstanceManager;
        this.ctx = ctx;
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == Constance.ALT_RN_ACTIVITY_REQUEST_CODE && resultCode == Constance.ALT_RN_ACTIVITY_RESULT_FAILED) {
            previousFailedResult = data.getExtras();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onHostResume() {
        if (previousFailedResult != null) {
            ctx.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("RNInstanceFailed", Arguments.fromBundle(previousFailedResult));
            previousFailedResult = null;
        }
    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }
}