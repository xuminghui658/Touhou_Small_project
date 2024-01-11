package com.example.touhouapp.View.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Bean.PersonalPageListBean;
import com.example.touhouapp.R;

import java.util.ArrayList;
import java.util.List;

public class PersonalListViewAdapter extends ArrayAdapter {
    public static final String TAG = "PersonalListViewAdapter";
    private final int resourceId;
    private final Context context;
    private final ArrayList<PersonalPageListBean> dataList;

    PersonalListViewAdapter.ViewHolder holder;

    public PersonalListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        resourceId = resource;
        dataList = (ArrayList<PersonalPageListBean>) objects;
    }

    public static class ViewHolder {
        TextView messageTitle;
        TextView messageContent;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        holder = new ViewHolder();
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            holder.messageTitle = convertView.findViewById(R.id.personal_page_message_title);
            holder.messageContent = convertView.findViewById(R.id.personal_page_message_detail);
            convertView.setTag(holder);
            PersonalPageListBean bean = dataList.get(position);
            TouHouApplication.d(TAG, "bean value = " + bean.toString());
            if (bean != null) {
                holder.messageTitle.setText(bean.getListTitle());
                holder.messageContent.setHint(bean.getListHintContent());
                holder.messageContent.setText(bean.getListContent());
            }
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
