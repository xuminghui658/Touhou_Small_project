package com.example.touhouapp.View.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.touhouapp.Base.BaseActivity;
import com.example.touhouapp.R;

public class SearchActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "SearchActivity";

    private Toolbar searchViewToolbar;
    private SearchView mSearchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView(){
        searchViewToolbar = findViewById(R.id.search_view_toolbar);
        setSupportActionBar(searchViewToolbar);
        mSearchView = findViewById(R.id.search_view_search);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(null);
        }
        searchViewToolbar.setNavigationIcon(R.drawable.arrow_back_icon);
        searchViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //這裡原逻辑是有输入文字，先清空，没有才退出，改为直接退出
                finish();
            }
        });
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG,"now click item = " + view.getId());
        int itemId = view.getId();

    }
}
