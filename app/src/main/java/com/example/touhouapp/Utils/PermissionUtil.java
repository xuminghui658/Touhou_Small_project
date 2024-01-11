package com.example.touhouapp.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.touhouapp.Base.TouHouApplication;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    public static String TAG = "PermissionUtil";
    public static final int REQUEST_CODE_ALL = 10;
    //检查是否有权限
    public static boolean checkPermissions(final Object fragment, final Activity activity, int code, String...requests){
        TouHouApplication.d(TAG,"request permissions...");
        if(requests == null || requests.length == 0){
            return false;
        }
        final List<String> permissionsList = new ArrayList<>();
        for(String request : requests){
            checkAndAddPermissions(fragment, activity, permissionsList, request);
        }
        if(checkAndRequestPermissions(fragment, activity, permissionsList, code)) {
            return true;
        }
        return  false;
    }

    public static boolean checkAndAddPermissions(Object fragment, Activity activity, final List<String> permissionsList, String str){
        if(fragment != null){
            if(((Fragment)fragment).getActivity().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(str);
            }
        }else if(activity != null){
            if(activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED){
                permissionsList.add(str);
            }
        }
        return true;
    }

    public static boolean checkAndRequestPermissions(Object fragment, Activity activity, final List<String> permissionsList, int code){
        if(permissionsList.size() == 0){
            return false;
        }
        TouHouApplication.d(TAG,"permissions list = " + permissionsList);
        if(activity != null){
            activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),code);
        }else{
            ((Fragment)fragment).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),code);
        }
        return true;
    }

    public static boolean hasPermissions(Object object, String...permissions){
        Activity activity = null;
        if(object instanceof Activity){
            activity = (Activity) object;
        }else if(object instanceof Fragment){
            activity = ((Fragment) object).getActivity();
        }
        if(activity == null){
            return false;
        }
        for(String str : permissions){
            if(activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
