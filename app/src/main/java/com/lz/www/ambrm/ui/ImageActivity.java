package com.lz.www.ambrm.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lz.www.ambrm.R;
import com.lz.www.ambrm.net.HttpService;
import com.lz.www.ambrm.util.Config;
import com.lz.www.ambrm.util.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Administrator on 2016-07-25.
 */
public class ImageActivity extends AppCompatActivity {

    Button btnUpload,btnDownload,btnSelect;
    ImageView imgView;
    String downURL="http://album.sina.com.cn/pic/001uAJWPzy6ROEllGWJ89";
    String uploadURL= Config.PhotoAPI+"/UploadImage";
    String picPath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        btnDownload=(Button)findViewById(R.id.btnDownload);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        btnSelect=(Button)findViewById(R.id.btnSelect);
        imgView=(ImageView)findViewById(R.id.imgView);

        imgView.setImageResource(R.drawable.mn1);


        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        if(msg.obj.toString()!="200") {
                            Toast.makeText(ImageActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ImageActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if(msg.obj!=null){
                            imgView.setImageBitmap((Bitmap)msg.obj);
                        }else{
                            Toast.makeText(ImageActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }

            }
        };

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(picPath);
                        Message msg = new Message();
                        msg.what = 0;
                        if(file!=null) {
                            try {
                                ImageUtils.uploadForm(file, uploadURL);
                                msg.obj = 1;
                            } catch (IOException ex) {
                                msg.obj = 0;
                                Toast.makeText(ImageActivity.this, "file fail", Toast.LENGTH_SHORT).show();
                            }
                            handler.sendMessage(msg);
                        }else {
                            Toast.makeText(ImageActivity.this, "file is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });



        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap img = ImageUtils.downloadImage(downURL);
                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=img;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK){
            Uri uri=data.getData();
            if(uri!=null){
                try {
                picPath=ImageUtils.getRealFilePath(ImageActivity.this,uri);
                Bitmap img =  BitmapFactory.decodeFile(picPath);
                imgView.setImageBitmap(img);
                }catch (Exception ex){
                    Toast.makeText(ImageActivity.this,"出现错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}
