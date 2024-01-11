package com.example.touhouapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Constants.Constants4Main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CameraUtil {
    public static final String TAG = "CameraUtil";
    private File ImageFile;
    private Uri ImageUri;
    private String FileDir;
    private String FilePath;
    private String fullFilePath;
    private int userId;
    private String rootPath;


    public String getImagePath(){
        return fullFilePath;
    }

    public Uri getImageUri(){
        return ImageUri;
    }

    //start Camera
    public void startCamera(Activity activity){
        initCameraFile();
        //设置拍摄后的文件保存路径
        PreferenceUtil.setCameraSavedPath(TouHouApplication.MyContext,fullFilePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,ImageUri);
        activity.startActivityForResult(intent, Constants4Main.CAMERA_SAVE_REQUEST_CODE);
    }

    public void initCameraFile(){
        //完整保存路径,根据根目录+自定义文件夹+.隐藏文件进行保存,反射写法
        StorageManager storageManager = (StorageManager) TouHouApplication.MyContext.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method volumeMethod = storageManager.getClass().getDeclaredMethod("getVolumes");
            try {
                List<Object> volumeList = (List<Object>)volumeMethod.invoke(storageManager);
                userId = getUserId();
                if(userId == -1){
                    return;
                }
                rootPath = getRootPath(volumeList);
            }catch (IllegalAccessException | InvocationTargetException e){
                TouHouApplication.e(TAG,"volumeList error " + e);
            }

        }catch (NoSuchMethodException e){
            TouHouApplication.e(TAG,"NoSuchMethodException, " + e);
        }
        FileDir = rootPath + File.separator + "Pictures" + File.separator + "TouhouApp/.Picture";
        File file = new File(FileDir);
        TouHouApplication.d(TAG,"FileDir = " + FileDir + ", actually file path = " + Environment.getExternalStorageDirectory());
        if(!file.exists()){
            if(!file.mkdirs()){
                TouHouApplication.e(TAG,"mkdirs picture file path failed");
                try {
                    file.createNewFile();
                }catch (IOException e){
                    TouHouApplication.e(TAG,"create picture file path failed");
                }
            }
        }
        FilePath = System.currentTimeMillis() + ".jpg";
        fullFilePath = FileDir + File.separator + FilePath;
        TouHouApplication.d(TAG,"fullFilePath =" + fullFilePath);
        ImageFile = new File(FileDir,FilePath);
        if(!ImageFile.exists()){
            try {
                ImageFile.createNewFile();
            }catch (IOException e){
                TouHouApplication.e(TAG,"create ImageFile Failed");
            }
        }
        //将图片路径转化为Uri绝对路径
        ImageUri = FileProvider.getUriForFile(TouHouApplication.MyContext,"com.example.myapplication.FileProvider",ImageFile);
    }

    private int getUserId(){
        int id = -1;
        try {
            Class User = Class.forName("android.os.UserHandle");
            try{
                Method userMethod = User.getDeclaredMethod("myUserId", new Class[0]);
                id = ((Integer)userMethod.invoke(null,new Object[0])).intValue();
            }catch (NoSuchMethodException e){
                TouHouApplication.e(TAG,"myUserId method not found");
            } catch (InvocationTargetException | IllegalAccessException e) {
                TouHouApplication.e(TAG,"myUserId method invoke error");
            }
        }catch (ClassNotFoundException e){
            TouHouApplication.e(TAG,"UserHandle not found");
        }
        return id;
    }

    private String getRootPath(List<Object> lists){
        String path = "";
        for (Object list : lists){
            if(list != null && list.getClass().getName().equals("android.os.storage.VolumeInfo")){
                Class<? extends Object> c = list.getClass();
                try {
                    //获取存储盘是sd卡或usb
                    Method diskMethod = c.getDeclaredMethod("getDisk");

                    Method pathMethod = c.getDeclaredMethod("getPathForUser", int.class);
                    Object currentPath = pathMethod.invoke(list, userId);
                    if(currentPath != null) {
                        path = currentPath.toString();
                    }
                } catch (NoSuchMethodException e) {
                    TouHouApplication.e(TAG,"getDisk not found");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    TouHouApplication.e(TAG,"getPathForUser not found");
                }
            }
        }
        return path;
    }
}
