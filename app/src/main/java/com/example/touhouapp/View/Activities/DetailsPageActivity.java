package com.example.touhouapp.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.touhouapp.Base.TouHouApplication;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.example.touhouapp.R;

public class DetailsPageActivity extends AppCompatActivity {
    private static String TAG = "DetailsPageActivity";
    public static final String IMAGE_NAME = "Image_name";
    public static final String IMAGE_ID = "Image_Id";
    private String text;
    private int imageId;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView textView;
    private ImageView imageView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouHouApplication.d(TAG,"intent = " + getIntent().getComponent().getClassName() + "----action = " + getIntent().getAction() + "----type = " + getIntent().getType()
                + "---getPackage = " + getIntent().getPackage() + "---getData = " + getIntent().getData());
        initView();
        initToolBarView(toolbar);
    }

    private void initView(){
        setContentView(R.layout.activity_details_page);
        text = getIntent().getStringExtra(IMAGE_NAME);
        imageId = getIntent().getIntExtra(IMAGE_ID,0);
        toolbar = findViewById(R.id.toolbar_detailPage);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_detailPage);
        textView = findViewById(R.id.text_detailPage);
        imageView = findViewById(R.id.image_detailPage);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        collapsingToolbarLayout.setTitle(text);
        imageView.setImageResource(imageId);
    }
    //返回键
    private void initToolBarView(Toolbar toolbar){
        toolbar.setNavigationIcon(R.drawable.arrow_back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}