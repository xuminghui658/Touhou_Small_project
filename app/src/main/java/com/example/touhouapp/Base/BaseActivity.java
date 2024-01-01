package com.example.touhouapp.Base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private static List<String> activityStacks = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String activityName = getClass().getSimpleName();
        TouHouApplication.d(TAG,"current is creating activity: " + activityName + ", current created activities: " + activityStacks);
        activityStacks.add(activityName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TouHouApplication.d(TAG,"onDestroy");
        String activityName = getClass().getSimpleName();
        TouHouApplication.d(TAG, "onDestroy, removeActivity: " + activityName + ", current created activities: " + activityStacks);
        activityStacks.remove(activityName);
    }
}
