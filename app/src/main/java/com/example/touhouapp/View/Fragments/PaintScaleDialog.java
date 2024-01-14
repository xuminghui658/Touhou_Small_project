package com.example.touhouapp.View.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.R;
import com.example.touhouapp.View.SelfView.PaintScale;

import java.io.IOException;

/**
 * 此页面为绘画弹窗，目前准备用作个人签名功能
 */
public class PaintScaleDialog extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "PaintScaleDialog";
    private PaintScale paintScale;
    private Spinner colorSpinner;
    private Spinner widthSpinner;
    private RadioButton clearPaint;
    private RadioButton saveCanvas;
    public OnSaveClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        TouHouApplication.d(TAG,"onAttach");
        if(context instanceof OnSaveClickListener){
            listener = (PaintScaleDialog.OnSaveClickListener)context;
            TouHouApplication.d(TAG,"PaintScaleFragment onAttach");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TouHouApplication.d(TAG,"onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_dialog_sign_paint, null);
        initView(view);
        initSpinnerListener();
        builder.setTitle("请签字")
                .setView(view);
        /**
         * 修改签字弹窗为底部弹出
         */
        AlertDialog dialog = builder.create();
        Window signDialogWindow = dialog.getWindow();
        signDialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = signDialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        view.measure(0, 0);
        lp.height = view.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        signDialogWindow.setAttributes(lp);
        return dialog;
    }

    private void initView(View rootView){
        paintScale = (PaintScale)rootView.findViewById(R.id.paintScale1);
        colorSpinner = (Spinner)rootView.findViewById(R.id.paint_color);
        widthSpinner = (Spinner)rootView.findViewById(R.id.paint_width);
        clearPaint = (RadioButton) rootView.findViewById(R.id.paint_clear);
        clearPaint.setOnClickListener(this);
        saveCanvas = rootView.findViewById(R.id.canvas_save);
        saveCanvas.setOnClickListener(this);
    }

    public void initSpinnerListener(){
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                paintScale.paint.setXfermode(null);
                switch (selected){
                    case "黑色":
                        TouHouApplication.d(TAG,"current color = 黑色");
                        paintScale.setColor(selected);
                        paintScale.paint.setColor(Color.BLACK);
                        break;
                    case "红色":
                        TouHouApplication.d(TAG,"current color = 红色");
                        paintScale.setColor(selected);
                        paintScale.paint.setColor(Color.RED);
                        break;
                    case "绿色":
                        TouHouApplication.d(TAG,"current color = 绿色");
                        paintScale.setColor(selected);
                        paintScale.paint.setColor(Color.GREEN);
                        break;
                    case "蓝色":
                        TouHouApplication.d(TAG,"current color = 蓝色");
                        paintScale.setColor(selected);
                        paintScale.paint.setColor(Color.BLUE);
                        break;
                }
                colorSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        widthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                paintScale.paint.setXfermode(null);
                switch (selected){
                    case "细线":
                        TouHouApplication.d(TAG,"current color = 细线");
                        paintScale.setWidth(selected);
                        paintScale.paint.setStrokeWidth(4);
                        break;
                    case "中线":
                        TouHouApplication.d(TAG,"current color = 中线");
                        paintScale.setWidth(selected);
                        paintScale.paint.setStrokeWidth(8);
                        break;
                    case "粗线":
                        TouHouApplication.d(TAG,"current color = 粗线");
                        paintScale.setWidth(selected);
                        paintScale.paint.setStrokeWidth(12);
                        break;
                }
                colorSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.paint_clear:
                paintScale.clear();
                break;
            case R.id.canvas_save:
                try {
                    String fileName = "sign_result_" + System.currentTimeMillis() + ".jpg";
                    paintScale.save(fileName);
                } catch (IOException e) {
                    TouHouApplication.e(TAG,"save Pictures failed," + e);
                }finally {
                    listener.onSaved(this);
                }
                break;
        }
    }

    public interface OnSaveClickListener{
        void onSaved(PaintScaleDialog p);
    }
}
