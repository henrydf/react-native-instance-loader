package com.hongchuangtech.enhancer;

import android.app.Application;

import com.facebook.react.ReactPackage;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by henry on 2018/3/7.
 */

public abstract class HCTReactApplication extends Application {

    abstract public List<ReactPackage> getNativePackages(@Nullable String newRNInstanceMainModuleName);
}
