package com.example.touhouapp.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Constants.Constants4Main;

public class MainActivityService extends Service {
    private static final String TAG = "MainActivityService";

    @Override
    public void onCreate() {
        super.onCreate();
        TouHouApplication.d(TAG,"onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        TouHouApplication.d(TAG,"onStartCommand, action = " + action + ", startId = " + startId);
        switch (action){

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
