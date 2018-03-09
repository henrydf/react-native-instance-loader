package com.hongchuangtech.enhancer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.modules.dialog.DialogModule;
import com.hongchuangtech.Constance;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by henry on 2018/3/7.
 */

public class HCTReactActivityDelegate extends ReactActivityDelegate {

    private final Activity mContext;
    private String mModuleName;
    private String mBundlePath;
    private Bundle mLaunchBundle;
    private ReactNativeHost mNativeHost;

    public HCTReactActivityDelegate(Activity activity) {
        super(activity, null);
        this.mContext = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mNativeHost = new ReactNativeHost(mContext.getApplication()) {
            @Override
            public boolean getUseDeveloperSupport() {
                return false;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                Application application = getApplication();
                Assertions.assertCondition(application instanceof HCTReactApplication, "MainApplication should be an instance of HCTReactApplication!");
                if (application instanceof HCTReactApplication) {
                    HCTReactApplication enhancedApp = (HCTReactApplication)application;
                    return enhancedApp.getNativePackages(HCTReactActivityDelegate.this.mModuleName);
                }
                return Collections.emptyList();
            }

            @Nullable
            @Override
            protected String getJSBundleFile() {
                return HCTReactActivityDelegate.this.mBundlePath;
            }
        };

        Bundle data = mContext.getIntent().getExtras();
        mModuleName = data.getString(Constance.PARAMS_MODULE_NAME);
        mBundlePath = data.getString(Constance.PARAMS_BUNDLE_PATH);
        mLaunchBundle = data.getBundle(Constance.INIT_PARAMS);
        // not work for js exception
//        getReactInstanceManager().addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
//            @Override
//            public void onReactContextInitialized(ReactContext context) {
//                context.setNativeModuleCallExceptionHandler(new NativeModuleCallExceptionHandler() {
//                    @Override
//                    public void handleException(Exception e) {
//                        e.printStackTrace();
//                        mContext.setResult(Activity.RESULT_CANCELED);
//                        mContext.finish();
//                    }
//                });
//            }
//        });
        loadApp(mModuleName);
    }

    @Nullable
    @Override
    protected Bundle getLaunchOptions() {
        return mLaunchBundle;
    }

    @Override
    protected ReactNativeHost getReactNativeHost() {
        return mNativeHost;
    }
}
