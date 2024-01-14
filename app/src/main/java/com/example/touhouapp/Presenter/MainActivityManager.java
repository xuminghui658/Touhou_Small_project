package com.example.touhouapp.Presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Constants.Constants4Main;
import com.example.touhouapp.Model.Services.MainActivityService;
import com.example.touhouapp.Utils.CameraUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivityManager {
    public static final String TAG = "MainActivityManager";
    private static MainActivityManager instance = null;
    private static Activity mActivity;
    private CameraUtil cameraUtil;
    public static MainActivityManager getInstance(Activity activity){
        if(instance == null){
            instance = new MainActivityManager();
        }
        if(mActivity == null){
            mActivity = activity;
        }
        return instance;
    }

    /**
     * camera部分原想用Service处理，但目前的写法需要startActivityForResult，直接写Manager了
     */
    public void startCamera(){
        if(cameraUtil == null){
            cameraUtil = new CameraUtil();
        }
        cameraUtil.startCamera(mActivity);
    }

    public void startAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        mActivity.startActivityForResult(intent, Constants4Main.ALBUM_SAVE_REQUEST_CODE);
    }

    private void startService(Intent intent, String action){
        intent.setClass(TouHouApplication.MyContext, MainActivityService.class);
        intent.setAction(action);
        TouHouApplication.MyContext.startService(intent);
    }

    public void doDestroy(){
        instance = null;
    }
}
