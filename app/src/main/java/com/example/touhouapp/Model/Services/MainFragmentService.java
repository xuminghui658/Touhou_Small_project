package com.example.touhouapp.Model.Services;
/**
 * 2023/07/29
 * xu ming hui
 * 该服务日后会涉及视频流读取，播放等操作
 * 目前采用bindService绑定，因为需要MainFragment的对象
 * 日后注意后台、深浅色切换等会导致页面重建的操作
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Bean.MainDisplay;
import com.example.touhouapp.Model.Interfaces.VideoData;
import com.example.touhouapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentService extends Service {
    private static final String TAG = "MainFragmentService";
    private final IBinder mBinder = new LocalBinder();
    private Context context;
    //把HomeDisplay类型的list作为接口数据返回
    private static VideoData mVideoDataImpl;
    private List<MainDisplay> VideoList= new ArrayList<>();
    @Override
    public void onCreate() {
        TouHouApplication.d(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TouHouApplication.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocalBinder extends Binder {

        public MainFragmentService getService(){
            return MainFragmentService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    public void getContext(Context context){
        this.context = context;
    }

    public void initVideoData(){
        //这里接受视频数据，放入list中并传递
        //后面可能请求数据库，暂定数量一定
        VideoList.clear();
        for(int i = 0; i < 12; i++){
            MainDisplay mainDisplay = new MainDisplay();
            mainDisplay.ImageId = R.drawable.login_morisa;
            mainDisplay.ImageText = "当前是第" + (i + 1) + "个视频";
            VideoList.add(mainDisplay);
        }
        mVideoDataImpl.setVideoData(VideoList);
    }

    @Override
    public void onDestroy() {
        TouHouApplication.d(TAG,"onDestroy");
        super.onDestroy();
    }

    public static void setVideoDataCallBack(VideoData data){
        mVideoDataImpl = data;
    }
}