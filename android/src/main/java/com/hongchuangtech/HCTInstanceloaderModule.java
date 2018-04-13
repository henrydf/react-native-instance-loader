
package com.hongchuangtech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
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
//    Assertions.assertCondition(mainReactContext == null, "Should Only Set MainReactContext Once!");
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
    Activity activity = getCurrentActivity();
    activity.setResult(Activity.RESULT_OK);
    activity.finish();
  }

  private boolean isLoadingBundle = false;
  @ReactMethod
  public void startNewInstance(final ReadableMap rnInfo) {
    if (isLoadingBundle) return;
    isLoadingBundle = true;
    reactContext.runOnNativeModulesQueueThread(new Runnable() {
      @Override
      public void run() {
        String name = rnInfo.getString("moduleName");
        String namespace = rnInfo.hasKey("namespace")
                ? rnInfo.getString("namespace")
                : name;
        String cacheDir = rnInfo.hasKey("cacheDir")
                ? rnInfo.getString("cacheDir")
                : reactContext.getCacheDir().getAbsolutePath();
        String dest = cacheDir + "/" + namespace + "/";
        String url = rnInfo.getString("androidUrl");
        double timeout = rnInfo.hasKey("timeout")
                ? rnInfo.getDouble("timeout")
                : 5;
        String bundleFile = dest + "main.jsbundle";
        boolean isDownloaded = false;
        try {
          isDownloaded = downloadZipFile(dest, url, timeout);
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (isDownloaded) {
          final Bundle args = new Bundle();
          args.putString(Constance.PARAMS_BUNDLE_PATH, bundleFile);
          args.putString(Constance.PARAMS_MODULE_NAME, name);
          args.putBundle(Constance.INIT_PARAMS, Arguments.toBundle(rnInfo.getMap("initProps")));
          reactContext.runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
              Intent intent = new Intent(getCurrentActivity(), HCTReactActivity.class);
              intent.putExtras(args);
              reactContext.getCurrentActivity().startActivityForResult(intent, Constance.ALT_RN_ACTIVITY_REQUEST_CODE);
            }
          });
        }
        reactContext.runOnUiQueueThread(new Runnable() {
          @Override
          public void run() {
            HCTInstanceloaderModule.this.isLoadingBundle = false;
          }
        });
      }
    });
  }

  private boolean downloadZipFile(String dest, String url, double timeout) throws IOException {
    File destDir = new File(dest);
    if (!destDir.isDirectory()) {
      destDir.mkdirs();
    }
    URL zipURL = new URL(url);
    HttpURLConnection connection = (HttpURLConnection) zipURL.openConnection();
    connection.setReadTimeout((int)(timeout * 1000));
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
    return true;
  }
}