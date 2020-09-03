package com.firebase.test.utils;

import android.util.Log;

/**
 * Created by darshakparmar on 10/05/17.
 */

public class AndroidLogger {

    static boolean isLogEnable = false;

    //    private interface LogLevel {
//        String VERBOSE = "verbose";
//        String DEBUG = "debug";
//        String INFO = "info";
//        String WARN = "warn";
//        String ERROR = "error";
//    }
    private static String TAG = "AndroidLogger";


    public AndroidLogger() {
    }

    public AndroidLogger(boolean isLogEnable) {
        this.isLogEnable = isLogEnable;
    }

    public static void verbose(String log) {
        if (isLogEnable) {
            Log.v(TAG, log);
        }

    }

    public static void verbose(String tag, String log) {
        if (isLogEnable)
            Log.v(tag, log);
    }

    public static void debug(String log) {
        if (isLogEnable)
            Log.d(TAG, log);
    }

    public static void debug(String tag, String log) {
        if (isLogEnable)
            Log.d(tag, log);
    }

    public static void info(String log) {
        if (isLogEnable)
            Log.i(TAG, log);
    }

    public static void info(String tag, String log) {
        if (isLogEnable)
            Log.i(tag, log);
    }

    public static void warn(String log) {
        if (isLogEnable)
            Log.w(TAG, log);
    }

    public static void warn(String tag, String log) {
        if (isLogEnable)
            Log.w(tag, log);
    }

    public static void error(String log) {
        if (isLogEnable)
            Log.e(TAG, log);
    }

    public static void error(String tag, String log) {
        if (isLogEnable)
            Log.e(tag, log);
    }


}
