package com.yazilimmuhendisim.qrcodescannerandgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ResultActivity extends AppCompatActivity {

    LinearLayout banner_ads;
    private AdView mAdView;
    TextView textView_result;
    FloatingActionButton button_web,button_copy,button_share,button_new,button_save,button_phone,button_sms,button_mail;
    CardView card_result;
    String result;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        result = intent.getStringExtra("result");

        if(result == null || result.isEmpty()){
            finish();
        }

        banner_ads = findViewById(R.id.linear_banner_ads_result);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_result);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                banner_ads.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                banner_ads.setVisibility(View.GONE);
            }
        });

        textView_result = findViewById(R.id.textView_result);
        textView_result.setText(result);

        card_result = findViewById(R.id.card_result);

        button_web=findViewById(R.id.button1);
        button_copy=findViewById(R.id.button2);
        button_share=findViewById(R.id.button3);
        button_new=findViewById(R.id.button4);
        button_save=findViewById(R.id.button8);
        button_phone =findViewById(R.id.fab_phone);
        button_sms=findViewById(R.id.fab_sms);
        button_mail=findViewById(R.id.fab_mail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button_web.setTooltipText(getResources().getString(R.string.fab_web));
            button_copy.setTooltipText(getResources().getString(R.string.fab_copy));
            button_share.setTooltipText(getResources().getString(R.string.fab_share));
            button_new.setTooltipText(getResources().getString(R.string.fab_add));
            button_save.setTooltipText(getResources().getString(R.string.fab_save_result));
            button_phone.setTooltipText(getResources().getString(R.string.fab_phone));
            button_sms.setTooltipText(getResources().getString(R.string.fab_sms));
            button_mail.setTooltipText(getResources().getString(R.string.fab_mail));
        }

        if(result.startsWith("tel:")){
            button_phone.setVisibility(View.VISIBLE);
        }else if(result.startsWith("mailto:")){
            button_mail.setVisibility(View.VISIBLE);
        }else if(result.startsWith("SMSTO:") || result.startsWith("smsto:")){
            button_sms.setVisibility(View.VISIBLE);
        }

        button_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent_tel = new Intent(Intent.ACTION_DIAL);
                    intent_tel.setData(Uri.parse(result));
                    startActivity(intent_tel);
                }catch (Exception e){

                }

            }
        });

        button_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse(result));
                    startActivity(emailIntent);
                } catch (Exception e) {

                }
            }
        });

        button_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String[] smsTo = result.split(":");
                    Uri uri = Uri.parse("smsto:"+smsTo[1]);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body", smsTo[2]);
                    startActivity(intent);
                }catch (Exception e){

                }

            }
        });

        button_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                    startActivity(browserIntent);
                }catch (Exception e){
                    String url = "https://www.google.com/search?q="+result;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

            }
        });

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text",result);
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ResultActivity.this,getResources().getString(R.string.toast_copy),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }

            }
        });

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, result);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);

                }catch (Exception e){

                }

            }
        });

        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent_scanner = new Intent(ResultActivity.this,ScannerActivity.class);
                startActivity(intent_scanner);
            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWriteExternalPermission();
            }
        });

        card_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text",result);
                    if (clipboard != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ResultActivity.this,getResources().getString(R.string.toast_copy),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
            }
        });
    }

    private void requestWriteExternalPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            save(result);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2323);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==2323){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                save(result);
            }else{

            }

        }
    }

    private void save(String name) {
        try {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = null;
            if (manager != null) {
                display = manager.getDefaultDisplay();
            }
            Point point = new Point();
            if (display != null) {
                display.getSize(point);
            }
            int width = point.x;
            int height = point.y;
            int smallerdimension = width<height ? width:height;
            smallerdimension=smallerdimension*3/4;
            QRGEncoder qrgEncoder = new QRGEncoder(name,null, QRGContents.Type.TEXT,smallerdimension);

            Bitmap bitmap = qrgEncoder.encodeAsBitmap();

            String file_name="";
            if(name.length()>=30){
                file_name = name.substring(0,30);
            }else{
                file_name=name;
            }

            if (file_name.contains(".")){
                file_name=file_name.replaceAll("\\.","");
            }
            if (file_name.contains("/")){
                file_name=file_name.replaceAll("/","");
            }

            File root = Environment.getExternalStorageDirectory();
            File dir = new File (root.getAbsolutePath()+"/"+getResources().getString(R.string.file_yol));

            if(!dir.exists())
            {
                dir.mkdir();
            }

            File photo = new File(dir+"/"+file_name+".png");
            int i=1;
            while (photo.exists()){
                photo = new File(dir+"/"+file_name+" ("+i+").png");
                i++;
            }


            FileOutputStream fos = new FileOutputStream(photo.getPath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Toast.makeText(ResultActivity.this,getResources().getString(R.string.save_basarili),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ResultActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
        }
    }
}
