package com.y.compress_libjpeg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
    }

    public native String test();
    public native void nativeCompress(Bitmap bitmap, int q, String path);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE"};
        requestPermissions(permissions,100);
        setContentView(R.layout.activity_main);

//        Log.e("TAG",test());

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e("222222",root);
        String path0 = root + "/aa0.jpg";
        File file0 = new File(path0);
        Log.e("TAG","原图file大小="+ file0.length() / 1024);
        Bitmap bitmap0 = BitmapFactory.decodeFile(file0.getAbsolutePath());
        Log.e("TAG","原图bitmap大小="+ bitmap0.getByteCount() / 1024);

        //系统的压缩
        compress(bitmap0, Bitmap.CompressFormat.JPEG,5,root+"/aa1_compress.jpeg");
        File file1 = new File(root + "/aa1_compress.jpeg");
        Log.e("TAG","系统的压缩file大小="+ file1.length() / 1024);
        Bitmap bitmap1 = BitmapFactory.decodeFile(file1.getAbsolutePath());
        Log.e("TAG","系统的压缩bitmap大小="+ bitmap1.getByteCount() / 1024);

        //libjpeg的压缩
        nativeCompress(bitmap0, 5, root+"/aa2_compress_libjpeg.jpg");
        File file2 = new File(root + "/aa2_compress_libjpeg.jpg");
        Log.e("TAG","libjpeg的压缩file大小="+ file2.length() / 1024);
        Bitmap bitmap2 = BitmapFactory.decodeFile(file2.getAbsolutePath());
        Log.e("TAG","libjpeg的压缩bitmap大小="+ bitmap2.getByteCount() / 1024);

//        2019-05-15 18:08:24.727 16640-16640/com.y.compress_libjpeg E/222222: /storage/emulated/0
//        2019-05-15 18:08:24.728 16640-16640/com.y.compress_libjpeg E/TAG: 原图file大小=21
//        2019-05-15 18:08:24.740 16640-16640/com.y.compress_libjpeg E/TAG: 原图bitmap大小=611
//        2019-05-15 18:08:24.747 16640-16640/com.y.compress_libjpeg E/TAG: 系统的压缩file大小=13
//        2019-05-15 18:08:24.752 16640-16640/com.y.compress_libjpeg E/TAG: 系统的压缩bitmap大小=611
//        2019-05-15 18:08:24.761 16640-16640/com.y.compress_libjpeg E/TAG: libjpeg的压缩file大小=13
//        2019-05-15 18:08:24.765 16640-16640/com.y.compress_libjpeg E/TAG: libjpeg的压缩bitmap大小=611
//        质量压缩减少了磁盘中大小，像素数量未变所以bitmap大小不变
    }

    /**
     * 压缩图片到制定文件
     *
     * @param bitmap 待压缩图片
     * @param format 压缩的格式
     * @param q      质量1-100
     * @param path   文件地址
     */
    private void compress(Bitmap bitmap, Bitmap.CompressFormat format, int q, String path) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(format, q, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}