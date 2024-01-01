package com.example.touhouapp.Base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class TouHouApplication extends Application {
    public static final String TAG = "TouHouApplication";
    @SuppressLint("StaticFieldLeak")
    public static Context MyContext;
    public static final int LOG_LEVEL = 5;
    public static final int LOG_DEBUG = 2;
    public static final int LOG_ERROR = 5;
    public static final int LOG_INFO = 3;
    public static final int LOG_VERBOSE = 1;
    public static final int LOG_WARN = 4;

    @Override
    public void onCreate() {
        super.onCreate();
        TouHouApplication.d(TAG, "create my application");
        //全局context
        MyContext = getApplicationContext();
    }

    //日志等级划分、输出控制
    public static void v(String TAG, String message){
        if(LOG_LEVEL >= LOG_VERBOSE){
            Log.v(TAG, message);
        }
    }
    public static void d(String TAG, String message){
        if(LOG_LEVEL >= LOG_DEBUG){
            Log.d(TAG, message);
        }
    }
    public static void i(String TAG, String message){
        if(LOG_LEVEL >= LOG_INFO){
            Log.i(TAG, message);
        }
    }
    public static void w(String TAG, String message){
        if(LOG_LEVEL >= LOG_WARN){
            Log.w(TAG, message);
        }
    }
    public static void e(String TAG, String message){
        if(LOG_LEVEL >= LOG_ERROR){
            Log.e(TAG, message);
        }
    }

}
