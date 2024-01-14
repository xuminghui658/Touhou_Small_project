package com.example.touhouapp.View.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.R;

public class HeadImageDialog extends DialogFragment {
    public static final String TAG = "HeadImageFragment";
    public OnHeadImageChangedListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnHeadImageChangedListener){
            mListener = (OnHeadImageChangedListener)context;
            TouHouApplication.d(TAG,"HeadImageFragment onAttach");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        TouHouApplication.d(TAG,"HeadImageFragment onCreate Dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.change_head_image);
        builder.setItems(R.array.change_head_image_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        mListener.onCameraSelected(HeadImageDialog.this);
                        break;
                    case 1:
                        mListener.onAlbumSelected(HeadImageDialog.this);
                        break;
                }
            }
        });
        return builder.create();
    }

    public interface OnHeadImageChangedListener{
        void onCameraSelected(HeadImageDialog m);
        void onAlbumSelected(HeadImageDialog m);
    }
}
