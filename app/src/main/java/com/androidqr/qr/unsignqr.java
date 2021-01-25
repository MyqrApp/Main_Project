package com.androidqr.qr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class unsignqr extends AppCompatActivity {
    ImageView scanImage;
    RelativeLayout shareRl1;
    private static final int FILE_PERMISSION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsignqr);
        scanImage = findViewById(R.id.scanImage);
        shareRl1 = findViewById(R.id.shareRl1);

        //for back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        String details = getIntent().getStringExtra("key");


        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            BitMatrix bitMatrix=multiFormatWriter.encode(details, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
            scanImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        shareRl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fileOutputStream1=null;
                File file1=getdisc();
                if (!file1.exists() && !file1.mkdirs())
                {
                    Toast.makeText(getApplicationContext(),"sorry can not make dir",Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmsshhmmss");
                String date=simpleDateFormat.format(new Date());
                String name="img"+date+".jpeg";
                String file_name=file1.getAbsolutePath()+"/"+name;
                File new_file=new File(file_name);
                try {
                    fileOutputStream1 =new FileOutputStream(new_file);
                    Bitmap bitmap=viewToBitmap(scanImage,scanImage.getWidth(),scanImage.getHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream1);
                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_LONG).show();
                    fileOutputStream1.flush();
                    fileOutputStream1.close();
                }
                catch
                (FileNotFoundException e) {

                } catch (IOException e) {

                } refreshGallary(file1);
                Bitmap bitmap = getBitmapFromView(scanImage);

                File file = new File(G.context.getExternalCacheDir(),"forshare.jpg");
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                try {
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file.setReadable(true,false);

                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                Uri Photourl = FileProvider.getUriForFile(G.context,G.context.getApplicationContext().getPackageName()+".provider",file);
                shareintent.putExtra(Intent.EXTRA_STREAM,Photourl);
                shareintent.setType("image/jpeg");
                shareintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(shareintent,null));
            }
            private void refreshGallary(File file)
            { Intent i=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                i.setData(Uri.fromFile(file)); sendBroadcast(i);
            }
            private File getdisc(){
                File file= getExternalFilesDir("QR");
                return new File(file,"My Image");
            }
        });

    }

    public static Bitmap getBitmapFromView(View view){
        Bitmap returnBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgdrawble = view.getBackground();
        if (bgdrawble!=null){
            bgdrawble.draw(canvas);
        }
        else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnBitmap;
    }

    private void shareQrcode() {
        scanImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = scanImage.getDrawingCache();
        File file = new File(Environment.getExternalStorageDirectory(),"bar_code.jpg");
        try{

            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.close();

            Intent intent = new Intent(Intent.ACTION_SEND);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.putExtra(Intent.EXTRA_STREAM,FileProvider.getUriForFile(MainActivity.this,"com.example.qr_generator_and_scanner",file));
//
//            }else{
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            Toast.makeText(unsignqr.this," share",Toast.LENGTH_SHORT).show();
            //  }
            intent.setType("image/*");
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission(String permission){

        int result = ContextCompat.checkSelfPermission(unsignqr.this,permission);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }

    }


    private void  requestPermission(String permission,int code){
        if(ActivityCompat.shouldShowRequestPermissionRationale(unsignqr.this,permission)){

        }
        else{
            ActivityCompat.requestPermissions(unsignqr.this,new String[]{permission},code);
        }
    }
    private static Bitmap viewToBitmap(View view, int widh, int hight)
    {
        Bitmap bitmap=Bitmap.createBitmap(widh,hight, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap); view.draw(canvas);
        return bitmap;
    }
}