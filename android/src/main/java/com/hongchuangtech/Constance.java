package com.hongchuangtech;

import android.app.Activity;

/**
 * Created by henry on 2018/3/7.
 */

public interface Constance {
    public static final String PARAMS_MODULE_NAME = "PARAMS_MODULE_NAME";
    public static final String PARAMS_BUNDLE_PATH = "PARAMS_BUNDLE_PATH";
    public static final String PARAMS_NAMESPACE   = "PARAMS_NAMESPACE";

    public static final String INIT_PARAMS = "INIT_PARAMS";

    public static int ALT_RN_ACTIVITY_REQUEST_CODE = 123321;
    public static int ALT_RN_ACTIVITY_RESULT_FAILED = Activity.RESULT_FIRST_USER + 1;
}
