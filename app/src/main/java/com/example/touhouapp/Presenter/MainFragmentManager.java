package com.example.touhouapp.Presenter;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.touhouapp.Bean.MainDisplay;
import com.example.touhouapp.Model.Services.MainFragmentService;

import java.util.List;

public class MainFragmentManager {
    private static final String TAG = "MainFragmentManager";
    private Context context;
    private MainFragmentService mMainFragmentService;
    private ServiceConnection mMainFragmentServiceConnection;
    @SuppressLint("StaticFieldLeak")
    public static MainFragmentManager instance;
    private List<MainDisplay> mDisplayList;

    private MainFragmentManager(Context context){
        this.context = context;
    }

    public static synchronized MainFragmentManager getInstance(Context context){
        Log.d(TAG,"getInstance, instance = " + instance);
        if(instance == null){
            instance = new MainFragmentManager(context);
        }
        return instance;
    }

    public void startMainFragmentService(){
        Log.d(TAG,"MainFragment bind Service..., context = " + context);
        if(context != null && mMainFragmentService == null){
            if(mMainFragmentServiceConnection == null){
                initMainFragmentServiceConnection();
            }
            Intent serviceIntent = new Intent(context, MainFragmentService.class);
            context.bindService(serviceIntent, mMainFragmentServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void initMainFragmentServiceConnection(){
        mMainFragmentServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //绑定service和上下文
                mMainFragmentService = ((MainFragmentService.LocalBinder)iBinder).getService();
                mMainFragmentService.getContext(context);
                mMainFragmentService.initVideoData();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG,"unexpected disconnected");
                mMainFragmentService = null;
            }
        };
    }

    public void stopMainFragmentService(){
        if(context != null && mMainFragmentService != null) {
            context.unbindService(mMainFragmentServiceConnection);
            mMainFragmentService = null;
        }
    }
}
