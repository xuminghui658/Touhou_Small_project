package com.example.touhouapp.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touhouapp.Bean.MainDisplay;
import com.example.touhouapp.R;
import com.example.touhouapp.View.Activities.DetailsPageActivity;

import java.util.List;

public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.HomeViewHolder>{
    private static final String TAG = "HomeAdapter";

    private final List<MainDisplay> mMainDisplayList;
    public class HomeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private TextView textView;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_image_display);
            textView = itemView.findViewById(R.id.home_text_display);
        }

    }

    public MainRecycleAdapter(List<MainDisplay> list){
        Log.d(TAG,"init HomeRecycleAdapter");
        mMainDisplayList = list;
    }
    @NonNull
    @Override
    public MainRecycleAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup group, int position) {
        Log.d(TAG,"onCreateViewHolder, position = " + position);
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.fragment_main_adapter, group, false);
        HomeViewHolder viewHolder = new HomeViewHolder(view);
        //对应位置的点击事件
        viewHolder.imageView.setOnClickListener(mView -> {
            MainDisplay mainDisplay = getCurrentClickedPosition(viewHolder);
            //视频名称这里后面做一个整体的跳转，暂时停在这
        });
        viewHolder.imageView.setOnClickListener(mView -> {
            MainDisplay mainDisplay = getCurrentClickedPosition(viewHolder);
            actionStart(group.getContext(), mainDisplay, group);
        });
        return viewHolder;
    }

    private MainDisplay getCurrentClickedPosition(HomeViewHolder holder){
        int pos = holder.getAdapterPosition();
        return mMainDisplayList.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecycleAdapter.HomeViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder, position = " + position);
        MainDisplay mainDisplay = mMainDisplayList.get(position);
        holder.imageView.setImageResource(mainDisplay.ImageId);
        holder.textView.setText(mainDisplay.ImageText);
    }

    @Override
    public int getItemCount() {
        return mMainDisplayList.size();
    }

    public static void actionStart(Context context, MainDisplay mainDisplay, ViewGroup parent){
        Intent intent = new Intent();
        intent.putExtra(DetailsPageActivity.IMAGE_NAME, mainDisplay.ImageText);
        intent.putExtra(DetailsPageActivity.IMAGE_ID, mainDisplay.ImageId);
        intent.setClass(parent.getContext(), DetailsPageActivity.class);
        parent.getContext().startActivity(intent);
    }
}
