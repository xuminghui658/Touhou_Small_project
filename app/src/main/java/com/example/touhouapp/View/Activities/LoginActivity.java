package com.example.touhouapp.View.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.touhouapp.Base.BaseActivity;
import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static String TAG = "LoginActivity";

    private Button confirmBtn;
    private Button registerBtn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouHouApplication.d(TAG,"onCreate");
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        confirmBtn = (Button) findViewById(R.id.login_btn_confirm);
        confirmBtn.setOnClickListener(this);
        registerBtn = (Button) findViewById(R.id.login_btn_register);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int currentId = view.getId();
        if(currentId == R.id.login_btn_confirm){
            TouHouApplication.d(TAG,"confirm login");
            Intent loginIntent = new Intent();
            loginIntent.setClass(this,MainActivity.class);
            startActivity(loginIntent);
            finish();
        }
        if(currentId == R.id.login_btn_register){
            TouHouApplication.d(TAG,"register login");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
