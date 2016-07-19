package com.lz.www.ambrm.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lz.www.ambrm.R;

import java.io.File;
import java.net.URL;

/**
 * Created by Administrator on 2016-07-18.
 */
public class HeadPhotoActivity extends Activity {

    final String IMG_NAME="my_head";
    ImageView imgHead;
    Button btnHeadCamera,btnHeadCancel,btnHeadSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        imgHead=(ImageView)findViewById(R.id.imgHead);
        btnHeadCamera=(Button)findViewById(R.id.btnHeadCamera);
        btnHeadSelect=(Button)findViewById(R.id.btnHeadSelect);
        btnHeadCancel=(Button)findViewById(R.id.btnHeadCancel);

        //拍照
        btnHeadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                itCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMG_NAME)));
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

        //取消
        btnHeadCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                File temp=new File(Environment.getExternalStorageDirectory()+"/"+IMG_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case 1:
                try {
                    startPhotoZoom(data.getData());
                }catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                break;
            case 2:
                if(data!=null){
                    saveCutPhoto(data);
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
    public void saveCutPhoto(Intent it){
        Bundle bundle=it.getExtras();
        if(bundle!=null){
            // 取得SDCard图片路径做显示
            Bitmap photo = bundle.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);

            imgHead.setImageDrawable(drawable);
        }
    }
}
