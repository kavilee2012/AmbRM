package com.lz.www.ambrm.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016-07-25.
 */
public class ImageUtils {

    //下载图片
    public static Bitmap downloadImage(final String imgUrl){
        Bitmap img=null;
        try {
            URL url = new URL(imgUrl);
            byte[] d=null;

            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5*1000);
            connection.setUseCaches(false);
            connection.connect();
            InputStream stream=connection.getInputStream();
            img= BitmapFactory.decodeStream(stream);
            stream.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return img;
    }

    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";

    /**
     *
     * @param uploadFile
     *            需要上传的文件
     * @param serverUrl
     *            上传的服务器的路径
     * @throws IOException
     */
    public static int uploadForm(File uploadFile,  String serverUrl)
            throws IOException {
        int re=0;

        String fileName = uploadFile.getName();
        StringBuilder sb = new StringBuilder();
        sb.append("--" + BOUNDARY + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + fileName
                + "\"; filename=\"" + fileName + "\"" + "\r\n");
        sb.append("Content-Type: image/jpeg" + "\r\n");
        sb.append("\r\n");

        byte[] headerInfo = sb.toString().getBytes("UTF-8");
        byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
        System.out.println(sb.toString());
        URL url = new URL(serverUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty("Content-Length", String
                .valueOf(headerInfo.length + uploadFile.length()
                        + endInfo.length));
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        InputStream in = new FileInputStream(uploadFile);
        out.write(headerInfo);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1)
            out.write(buf, 0, len);

        out.write(endInfo);
        in.close();
        out.close();
        if (conn.getResponseCode() == 200) {
           // System.out.println("上传成功");
            re=1;
        }
        return re;
    }


    /**
     * 根据URI获取绝对路径
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
