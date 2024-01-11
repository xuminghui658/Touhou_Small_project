package com.example.touhouapp.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Bean.MainDisplay;
import com.example.touhouapp.Model.Dao.VideoData;
import com.example.touhouapp.Model.Services.MainFragmentService;
import com.example.touhouapp.Presenter.MainFragmentManager;
import com.example.touhouapp.R;
import com.example.touhouapp.View.Adapters.MainRecycleAdapter;

import java.util.List;

public class MainFragment extends Fragment implements VideoData {
    private static final String TAG = "MainFragment";
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<MainDisplay> mDisplayList;
    private RecyclerView mMainRecycle;
    private MainRecycleAdapter mMainAdapter;
    private MainFragmentManager mMainFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        TouHouApplication.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        bindService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        return view;
    }

    private void bindService(){
        mMainFragmentManager = MainFragmentManager.getInstance(getContext());
        mMainFragmentManager.startMainFragmentService();
        MainFragmentService.setVideoDataCallBack(this);
    }

    private void initView(){
        mMainRecycle = view.findViewById(R.id.recyclerView_main);
    }

    private void addVideoTag(){
        TouHouApplication.d(TAG,"mDisplayList = " + mDisplayList);
        if(mDisplayList != null){
            mMainAdapter = new MainRecycleAdapter(mDisplayList);
            mMainRecycle.setAdapter(mMainAdapter);
            LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
            mMainRecycle.setLayoutManager(linearLayoutManager);
        }
    }

    private void unbindService(){
        if(mMainFragmentManager != null){
            mMainFragmentManager.stopMainFragmentService();
        }
    }

    @Override
    public void setVideoData(List<MainDisplay> list) {
        TouHouApplication.d(TAG,"loading video over");
        mDisplayList = list;
        addVideoTag();
    }

    @Override
    public void onDestroy() {
        TouHouApplication.d(TAG,"onDestroy");
        super.onDestroy();
        unbindService();
    }


}
