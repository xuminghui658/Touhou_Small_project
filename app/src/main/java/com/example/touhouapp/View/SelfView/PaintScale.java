package com.example.touhouapp.View.SelfView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Presenter.MainActivityManager;
import com.example.touhouapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PaintScale extends View {
    public static String TAG = "paintScale";
    private int screenWidth = 0;
    private int screenHeight = 0;
    private float preX;
    private float preY;
    private Path path;
    public Paint paint;
    private Bitmap cacheBitmap;
    public Canvas cacheCanvas;

    public PaintScale(Context context) {
        super(context);
        initCanvas(context);
    }

    public PaintScale(Context context, @Nullable AttributeSet attrs){
        super(context,attrs);
        initCanvas(context);
    }

    private void initCanvas(Context context) {
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDimensionPixelSize(R.dimen.drawView_height);
        //创建空白位图
        cacheBitmap = Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap);
        cacheCanvas.drawColor(getResources().getColor(R.color.ThemeColor_Gray));
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);
        //设置部分画笔属性
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(8);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.ThemeColor_Gray));
        Paint bitPaint = new Paint();
        //画板背景
        canvas.drawBitmap(cacheBitmap,0,0,bitPaint);
        canvas.drawPath(path,paint);
        canvas.save();
        canvas.restore();
    }

    //和触发频率有关
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        TouHouApplication.d(TAG,"current point(x,y) = " + x + "  " + y);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //画线，采用贝塞尔曲线
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if(dx >= 5 || dy >= 5){
                    path.quadTo(preX,preY,(x + preX)/2,(y + preY)/2);
                    preX = x;
                    preY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path,paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    //橡皮擦
    public void clear(){
        cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    //保存
    public void save(String fileName) throws IOException {
        String fileDirName = Environment.getExternalStorageDirectory() + File.separator + "Pictures"+ File.separator + "TouhouApp/.Picture";
        //保存系统相册的方法
        OutputStream fileOS = null;
        File saveFile = new File(fileDirName,fileName);
        try {
            if(!saveFile.exists()){
                if(!saveFile.createNewFile()){
                    TouHouApplication.d(TAG,"saveFile.createNewFile failed");
                }
            }
            fileOS = new FileOutputStream(saveFile);
            cacheBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOS);
            TouHouApplication.d(TAG,"Image save success");
            fileOS.flush();
            fileOS.close();
        }catch (IOException e){
            TouHouApplication.e(TAG,"create SaveFile failed :" + e);
        }finally {
            if(fileOS != null) {
                try {
                    fileOS.close();
                }catch (IOException e){
                    e.getStackTrace();
                }
            }
        }
    }

    public void setColor(String str){
        paint.setXfermode(null);
        switch(str){
            case "黑色":
                paint.setColor(Color.BLACK);
                break;
            case "红色":
                paint.setColor(Color.RED);
                break;
            case "蓝色":
                paint.setColor(Color.BLUE);
                break;
            case "绿色":
                paint.setColor(Color.GREEN);
                break;
        }
    }

    public void setWidth(String str){
        paint.setXfermode(null);
        switch(str){
            case "细线":
                paint.setStrokeWidth(4);
                break;
            case "中线":
                paint.setStrokeWidth(8);
                break;
            case "粗线":
                paint.setStrokeWidth(12);
                break;
        }
    }
}
