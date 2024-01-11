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

    public void saveBitmap(Context context, String fileName, Bitmap bitmap){
        String fileDirName = Environment.getExternalStorageDirectory() + File.separator + "Pictures"+ File.separator + "TouhouApp/.Picture";
        //保存系统相册的方法
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.TITLE,fileName);
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/png");
        contentValues.put(MediaStore.MediaColumns.DATE_TAKEN,fileName);
        //文件保存路径
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,fileDirName);
        Uri uri = null;
        OutputStream fileOS = null;
        ContentResolver localContentResolver = context.getContentResolver();
        try {
            uri = localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fileOS = localContentResolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
            TouHouApplication.d(TAG,"Image save success");
            fileOS.flush();
            fileOS.close();
        }catch (IOException e){
            TouHouApplication.e(TAG,"create SaveFile failed");
            if(uri != null){
                localContentResolver.delete(uri,null,null);
            }
        }finally {
//            bitmap.recycle();
            if(fileOS != null) {
                try {
                    fileOS.close();
                }catch (IOException e){
                    e.getStackTrace();
                }
            }
        }
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
