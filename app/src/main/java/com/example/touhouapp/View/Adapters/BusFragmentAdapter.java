package com.example.touhouapp.View.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.touhouapp.Constants.Constants4Main;
import com.example.touhouapp.View.Activities.MainActivity;
import com.example.touhouapp.View.Fragments.HomeFragment;
import com.example.touhouapp.View.Fragments.MainFragment;
import com.example.touhouapp.View.Fragments.NewsFragment;
import com.example.touhouapp.View.Fragments.ShopFragment;

import java.io.PipedOutputStream;

public class BusFragmentAdapter extends FragmentStateAdapter {
    private static final String TAG = "BusFragmentAdapter";
    @SuppressLint("StaticFieldLeak")
    private static BusFragmentAdapter mBusFragmentAdapter;
    private static Context mContext;
    private BusFragmentAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager,lifecycle);
    }

    public static BusFragmentAdapter getInstance(Context context, @NonNull FragmentManager fragmentManager, Lifecycle lifecycle){
        if(mBusFragmentAdapter == null){
            mBusFragmentAdapter = new BusFragmentAdapter(fragmentManager, lifecycle);
        }
        mContext = context;
        return mBusFragmentAdapter;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(TAG,"current click position: " + position);
        switch (position){
            case MainActivity.TAB_MAIN:
                return new MainFragment();
            case MainActivity.TAB_NEWS:
                return new NewsFragment();
            case MainActivity.TAB_SHOP:
                return new ShopFragment();
            case MainActivity.TAB_HOME:
                return new HomeFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return Constants4Main.TOTAL_FRAGMENTS_NUM;
    }
}
