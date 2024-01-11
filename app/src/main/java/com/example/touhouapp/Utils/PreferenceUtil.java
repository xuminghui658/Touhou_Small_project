package com.example.touhouapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Preferences保存工具类
 */
public class PreferenceUtil {
    //保存裁剪图片
    public static final String CROPPED_IMAGE= "croppedHeadImage";
    public static final String SAVED_IMAGE= "savedHeadImage";

    public static void setCroppedBitmap(Context ct, Bitmap bitmap){
        SharedPreferences pt = ct.getSharedPreferences("croppedImage",Context.MODE_PRIVATE);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        String base = new String(android.util.Base64.encodeToString(bos.toByteArray(), android.util.Base64.DEFAULT));
        pt.edit().putString(PreferenceUtil.CROPPED_IMAGE,base).apply();
    }

    public static Bitmap getCroppedBitmap(Context ct){
        Bitmap result;
        SharedPreferences pt = ct.getSharedPreferences("croppedImage",Context.MODE_PRIVATE);
        String bit = pt.getString(PreferenceUtil.CROPPED_IMAGE,"");
        if(bit.equals("")){
            return null;
        }
        byte[] bis = android.util.Base64.decode(bit.getBytes(), android.util.Base64.DEFAULT);
        result = BitmapFactory.decodeByteArray(bis,0,bis.length);
        return result;
    }

    //因为相机返回data为空，直接在这里存储拍照头像保存路径
    public static void setCameraSavedPath(Context ct, String str){
        SharedPreferences pt = ct.getSharedPreferences("savedImage",Context.MODE_PRIVATE);
        pt.edit().putString(PreferenceUtil.SAVED_IMAGE,str).apply();
    }

    public static String getSavedImage(Context ct){
        SharedPreferences pt = ct.getSharedPreferences("savedImage",Context.MODE_PRIVATE);
        String str = pt.getString(PreferenceUtil.SAVED_IMAGE,"");
        return str;
    }
}
