
package com.hongchuangtech;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hongchuangtech.enhancer.HCTReactActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class HCTInstanceloaderModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private static ReactContext mainReactContext;

  public static void setMainReactContext(ReactContext ctx) {
    Assertions.assertCondition(mainReactContext == null, "Should Only Set MainReactContext Once!");
    mainReactContext = ctx;
  }

  public static ReactContext getMainReactContext() {
    return mainReactContext;
  }

  public HCTInstanceloaderModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "HCTInstanceloader";
  }

  @ReactMethod
  public void returnMainProcess(ReadableMap data) {
    WritableMap wData = Arguments.createMap();
    wData.merge(data);
    mainReactContext.getJSModule(
      DeviceEventManagerModule.RCTDeviceEventEmitter.class
    ).emit("RNInstanceFinished", wData);
    getCurrentActivity().finish();
  }

  private boolean isLoadingBundle = false;
  @ReactMethod
  public void startNewInstance(final ReadableMap bundleInfo) {
    if (isLoadingBundle) return;
    isLoadingBundle = true;
    reactContext.runUIBackgroundRunnable(new Runnable() {
      @Override
      public void run() {
        String name = bundleInfo.getString("moduleName");
        String dest = reactContext.getCacheDir() + "/" + name + "/";
        String bundleFile = dest + "main.jsbundle";
        String url = bundleInfo.getString("androidUrl");
        try {
          File destDir = new File(dest);
          if (!destDir.isDirectory()) {
            destDir.mkdirs();
          }
          URL zipURL = new URL(url);
          HttpURLConnection connection = (HttpURLConnection) zipURL.openConnection();
          ZipInputStream zis = new ZipInputStream(connection.getInputStream());
          ZipEntry zipEntry;
          byte[] buffer = new byte[10240];
          while ((zipEntry = zis.getNextEntry()) != null) {
            String abPath = dest + zipEntry.getName();
            if (zipEntry.isDirectory()) {
              File dir = new File(abPath);
              dir.mkdirs();
            } else {
              FileOutputStream fos = new FileOutputStream(abPath);
              int count;
              while((count = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
              }
              fos.flush();
              fos.close();
            }
            zis.closeEntry();
          }
          final Bundle args = new Bundle();
          args.putString(Constance.PARAMS_OUT_BUNDLE_PATH, bundleFile);
          args.putString(Constance.PARAMS_OUT_MODULE_NAME, name);
          reactContext.runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
              Intent intent = new Intent(getCurrentActivity(), HCTReactActivity.class);
              intent.putExtras(args);
              reactContext.getCurrentActivity().startActivity(intent);
              HCTInstanceloaderModule.this.isLoadingBundle = false;
            }
          });
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}