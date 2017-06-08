package com.example.administrator.drawingdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.R.attr.color;
import static android.R.attr.startX;
import static android.R.attr.startY;
import static android.graphics.BitmapFactory.decodeResource;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {
    private ImageView iv;
    private Paint paint;
    private Canvas canvas;
    private Bitmap copyBitmap,srcbitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
        //获取原图
       srcbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        //创建一个副本
       copyBitmap = Bitmap.createBitmap(srcbitmap.getWidth(),srcbitmap.getHeight(),srcbitmap.getConfig());

        paint = new Paint();
        canvas = new Canvas(copyBitmap);

        //开始画画
        canvas.drawBitmap(srcbitmap,new Matrix(),paint);
        iv.setImageBitmap(copyBitmap);

        iv.setOnTouchListener(new View.OnTouchListener() {
            float startX = 0;
            float startY =0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();

                float stopX ;
                float stopY ;
            switch(action){
                case MotionEvent.ACTION_DOWN :
                    //获取开始的位置
                    startX = event.getX();
                    startY = event.getY();

                    break;
                case MotionEvent.ACTION_MOVE :
                    //获取结束的位置
                    stopX = event.getX();
                    stopY = event.getY();
                    canvas.drawLine(startX,startY,stopX,stopY,paint);

                    iv.setImageBitmap(copyBitmap);
                    //更新开始和结束的坐标
                    startX = stopX;
                    startY = stopY;

                    break;
                case MotionEvent.ACTION_UP :

                    break;
                }

                return true;
            }
        });
    }
    //变红色
    public void click1(View view){
        paint.setColor(Color.RED);
    }
    //字体加粗
    public void click2(View view){
        paint.setStrokeWidth(10);
    }
    //保存图片
    public void click3(View view){
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath(),"tuya.png");
            FileOutputStream fos = new FileOutputStream(file);
            copyBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

            //发送广播

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file); //out is your output file
                mediaScanIntent.setData(contentUri);
                MainActivity.this.sendBroadcast(mediaScanIntent);
            } else {
                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
//                sendBroadcast(intent);
//            }else {
//                sendBroadcast(new Intent(
//                        Intent.ACTION_MEDIA_MOUNTED,
//                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

}
