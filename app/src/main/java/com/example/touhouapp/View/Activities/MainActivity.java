package com.example.touhouapp.View.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.touhouapp.Base.BaseActivity;
import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Presenter.MainActivityManager;
import com.example.touhouapp.R;
import com.example.touhouapp.View.Adapters.BusFragmentAdapter;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static String TAG = "MainActivity";
    /**
     *different fragment
     */
    public static final int TAB_MAIN = 0;
    public static final int TAB_HOME = 3;
    public static final int TAB_NEWS = 1;
    public static final int TAB_SHOP = 2;

    private RadioButton mainBtn,newsBtn,shopBtn,homeBtn;

    private BusFragmentAdapter mBusFragmentAdapter;

    /**
     * ViewPager2
     */
    private ViewPager2 mainPageSelection;

    /**
     * actionBar/ToolBar
     */
    private Toolbar toolbar;
    private SearchView searchView;
    private MainActivityManager mMainActivityManager;

    //滑动窗口相关
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CircleImageView mIconImage;

    /**
     * dialog
     */
    DialogFragment headChangeFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouHouApplication.d(TAG,"onCreate");
        setContentView(R.layout.activity_main);
        initView();
        initPresenter();
    }

    /**
     * layout component related
     */
    private void initView(){
        mainPageSelection = findViewById(R.id.ViewPager_main);
        mainBtn = findViewById(R.id.main_tab);
        mainBtn.setOnClickListener(this);
        newsBtn = findViewById(R.id.news_tab);
        newsBtn.setOnClickListener(this);
        shopBtn = findViewById(R.id.shop_tab);
        shopBtn.setOnClickListener(this);
        homeBtn = findViewById(R.id.home_tab);
        homeBtn.setOnClickListener(this);
//        mIconImage.setOnClickListener(this);
        mBusFragmentAdapter = BusFragmentAdapter.getInstance(getApplicationContext(),getSupportFragmentManager(),getLifecycle());
        mainPageSelection.setAdapter(mBusFragmentAdapter);
        //forbid slide
        mainPageSelection.setUserInputEnabled(false);
        mainPageSelection.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TouHouApplication.d(TAG,"PageSelected, position = " + position);
                switch (position){
                    case TAB_MAIN:
                        mainBtn.setChecked(true);
                        break;
                    case TAB_NEWS:
                        newsBtn.setChecked(true);
                        break;
                    case TAB_SHOP:
                        shopBtn.setChecked(true);
                        break;
                    case TAB_HOME:
                        homeBtn.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        toolbar = findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navView_main);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TouHouApplication.d(TAG, "open navigation View");
                drawerLayout = findViewById(R.id.drawerLayout);
                mIconImage = findViewById(R.id.circle_icon_image);
                mIconImage.setImageResource(R.drawable.login_morisa);
                drawerLayout.openDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.navCall);
            }
        });
        searchView = findViewById(R.id.search_toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }
        searchView.setFocusable(false);
        searchView.setFocusableInTouchMode(true);
        //暂定以焦点改变为跳转条件
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if(focused){
                    searchView.clearFocus();
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(searchIntent);
                }
            }
        });
    }

    /**
     * Presenter related
     */
    private void initPresenter(){

    }

    /**
     * override related
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_scan:
                Toast.makeText(this,R.string.open_scan,Toast.LENGTH_LONG).show();
                return true;
            case R.id.item_msg:
                Toast.makeText(this,R.string.open_msg,Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_tab:
                mainPageSelection.setCurrentItem(TAB_MAIN,false);
                break;
            case R.id.news_tab:
                mainPageSelection.setCurrentItem(TAB_NEWS,false);
                break;
            case R.id.shop_tab:
                mainPageSelection.setCurrentItem(TAB_SHOP,false);
                break;
            case R.id.home_tab:
                mainPageSelection.setCurrentItem(TAB_HOME,false);
                break;
            case R.id.layout_toolbar: {
                TouHouApplication.d(TAG, "open navigation View");
                navigationView = findViewById(R.id.navView_main);
                mIconImage = findViewById(R.id.circle_icon_image);
                drawerLayout.openDrawer(GravityCompat.START);
                navigationView.setCheckedItem(R.id.navCall);
                break;
            }
            case R.id.circle_icon_image:
                //更换头像的逻辑
            default:
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        TouHouApplication.d(TAG,"onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TouHouApplication.d(TAG,"onDestroy");
    }
}
