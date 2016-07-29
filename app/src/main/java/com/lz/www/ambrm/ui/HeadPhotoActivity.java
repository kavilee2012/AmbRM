package com.lz.www.ambrm.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lz.www.ambrm.R;
import com.lz.www.ambrm.util.Config;
import com.lz.www.ambrm.util.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016-07-18.
 */
public class HeadPhotoActivity extends Activity {

    ImageView imgHead;
    Button btnHeadCamera,btnHeadCancel,btnHeadSelect;

    final String SAVE_PATH=Environment.getExternalStorageDirectory()+"/my_head.jpg"; //拍照后保存路径
    String SERVER_URL = Config.PhotoAPI+"/UploadImage";//上传的服务端API地址
    String downURL="http://album.sina.com.cn/pic/001uAJWPzy6ROEllGWJ89";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        imgHead=(ImageView)findViewById(R.id.imgHead);
        btnHeadCamera=(Button)findViewById(R.id.btnHeadCamera);
        btnHeadSelect=(Button)findViewById(R.id.btnHeadSelect);
        btnHeadCancel=(Button)findViewById(R.id.btnHeadUpload);

        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if ((int)msg.obj == 1) {
                            Toast.makeText(HeadPhotoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HeadPhotoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        };

        //点击拍照
        btnHeadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                itCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMG_NAME)));
                startActivityForResult(itCamera,0);
            }
        });

        //从相册中选择
        btnHeadSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itSelect=new Intent(Intent.ACTION_PICK,null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                itSelect.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(itSelect,1);

            }
        });

        //上传
        btnHeadCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(SAVE_PATH);
                        Message msg = new Message();
                        msg.what = 0;
                        if(file!=null) {
                            try {
                              int re = ImageUtils.uploadForm(file, SERVER_URL);
                                msg.obj = re;
                            } catch (IOException ex) {
                                msg.obj = 0;
                                Toast.makeText(HeadPhotoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                            handler.sendMessage(msg);
                        }else {
                            Toast.makeText(HeadPhotoActivity.this, "找不到上传图片", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0://拍照
                //File temp=new File(SAVE_PATH);
               // startPhotoZoom(data.getData());
                savePhoto(data);
                break;
            case 1://裁剪
                try {
                    startPhotoZoom(data.getData());
                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                break;
            case 2://保存本地
                if(data!=null){
                    savePhoto(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //剪裁图片
    public void startPhotoZoom(Uri uri){
        Intent it=new Intent("com.android.camera.action.CROP");
        it.setDataAndType(uri,"image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        it.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        it.putExtra("aspectX", 1);
        it.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        it.putExtra("outputX", 300);
        it.putExtra("outputY", 300);
        it.putExtra("return-data", true);
        startActivityForResult(it, 2);
    }

    //保存显示剪裁后的图片
    public void savePhoto(Intent it){
        Bundle bundle=it.getExtras();
        if(bundle!=null){
            // 取得SDCard图片路径做显示
            Bitmap photo = bundle.getParcelable("data");
            imgHead.setImageBitmap(photo);
            File fileHead=new File(SAVE_PATH);
            try {
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    if(!fileHead.getParentFile().exists()){
                        fileHead.getParentFile().mkdir();
                    }
                    BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(fileHead));
                    photo.compress(Bitmap.CompressFormat.JPEG,80,bos);
                    bos.flush();
                    bos.close();
                }else {
                    Toast toast = Toast.makeText(HeadPhotoActivity.this, "保存失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
