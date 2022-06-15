package com.yazilimmuhendisim.qrcodescannerandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrGeneratorActivity extends AppCompatActivity {

    EditText editText,editText_phone,editText_sms_body;
    ImageView imageView;
    Button button;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String inputValue;
    LinearLayout button_bar;
    FloatingActionButton button_save,button_share,button_ref;
    RadioButton radio_metin,radio_url,radio_tel,radio_sms;
    RadioGroup radioGroup;
    LinearLayout banner_ads,sms_bar;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        editText = findViewById(R.id.edit_text_qr_generator);
        editText_phone = findViewById(R.id.edit_text_phone_number);
        editText_sms_body = findViewById(R.id.edit_text_sms);
        imageView = findViewById(R.id.imgView_qrCode);
        button = findViewById(R.id.button_qrCode);
        button_bar = findViewById(R.id.lineer_button_bar);
        button_save = findViewById(R.id.button5);
        button_share = findViewById(R.id.button6);
        button_ref = findViewById(R.id.button7);
        radio_metin =findViewById(R.id.radioButton3);
        radio_url =findViewById(R.id.radioButton4);
        radio_tel =findViewById(R.id.radioButton5);
        radio_sms=findViewById(R.id.radioButton6);
        sms_bar=findViewById(R.id.linear_sms_bar);
        banner_ads = findViewById(R.id.linear_banner_ads_qr);
        radioGroup=findViewById(R.id.radio_group1);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_qr);
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

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3332967002509193/2299535485");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button_save.setTooltipText(getResources().getString(R.string.fab_save));
            button_share.setTooltipText(getResources().getString(R.string.fab_share));
            button_ref.setTooltipText(getResources().getString(R.string.fab_ref));
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radio_metin.isChecked()){
                    editText.setVisibility(View.VISIBLE);
                    sms_bar.setVisibility(View.GONE);
                    editText.setHint(getResources().getString(R.string.edit_text1_hint));
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                }else if(radio_url.isChecked()){
                    editText.setVisibility(View.VISIBLE);
                    sms_bar.setVisibility(View.GONE);
                    editText.setHint(getResources().getString(R.string.edit_text1_hint));
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                }else if(radio_tel.isChecked()){
                    editText.setVisibility(View.VISIBLE);
                    sms_bar.setVisibility(View.GONE);
                    editText.setHint(getResources().getString(R.string.edit_text2_hint));
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                }else if(radio_sms.isChecked()){
                    editText.setVisibility(View.GONE);
                    sms_bar.setVisibility(View.VISIBLE);
                }
            }
        });


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

        qrgEncoder = new QRGEncoder("https://yazilimmuhendisim.net/",null, QRGContents.Type.TEXT,smallerdimension);
            bitmap = qrgEncoder.encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sms_bar.getVisibility()==View.VISIBLE){
                    if(editText_phone.getText().toString().trim().length()<=0){
                        editText_phone.setError(getResources().getString(R.string.error_edt_txt1));
                    }else if(editText_sms_body.getText().toString().trim().length()<=0){
                        editText_sms_body.setError(getResources().getString(R.string.error_edt_txt1));
                    }else{
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

                            inputValue="SMSTO:"+editText_phone.getText().toString().trim()+":"+editText_sms_body.getText().toString().trim();
                            qrgEncoder = new QRGEncoder(inputValue,null, QRGContents.Type.TEXT,smallerdimension);

                            bitmap = qrgEncoder.encodeAsBitmap();
                            imageView.setImageBitmap(bitmap);
                            button_bar.setVisibility(View.VISIBLE);
                            hideKeyboard(QrGeneratorActivity.this);

                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }catch (Exception e){
                            Toast.makeText(QrGeneratorActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
                            button_bar.setVisibility(View.GONE);
                        }
                    }
                }else{
                    inputValue = editText.getText().toString().trim();
                    if(inputValue.length()>0){
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
                            if(radio_metin.isChecked()){
                                qrgEncoder = new QRGEncoder(inputValue,null, QRGContents.Type.TEXT,smallerdimension);
                            }else if (radio_url.isChecked()){

                                qrgEncoder = new QRGEncoder(inputValue,null, QRGContents.Type.TEXT,smallerdimension);
                            }
                            else if (radio_tel.isChecked()){
                                qrgEncoder = new QRGEncoder(inputValue,null, QRGContents.Type.PHONE,smallerdimension);
                            }
                            else{
                                inputValue="https://yazilimmuhendisim.net/";
                                qrgEncoder = new QRGEncoder(inputValue,null, QRGContents.Type.TEXT,smallerdimension);
                            }

                            bitmap = qrgEncoder.encodeAsBitmap();
                            imageView.setImageBitmap(bitmap);
                            button_bar.setVisibility(View.VISIBLE);
                            hideKeyboard(QrGeneratorActivity.this);

                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }catch (Exception e){
                            Toast.makeText(QrGeneratorActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
                            button_bar.setVisibility(View.GONE);
                        }

                    }else{
                        editText.setError(getResources().getString(R.string.error_edt_txt1));
                        button_bar.setVisibility(View.GONE);
                    }
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null&&inputValue.length()>0){
                    save(inputValue,bitmap);
                }
            }
        });
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null){
                    try {
                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,getResources().getString(R.string.file_yol), null);
                        Uri bitmapUri = Uri.parse(bitmapPath);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        shareIntent.setType("image/*");
                        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.file_yol)));
                    }catch (Exception e){

                    }

                }
            }
        });
        button_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_bar.setVisibility(View.GONE);
                bitmap = null;
                imageView.setImageBitmap(null);
                editText.setText("");
                editText_sms_body.setText("");
                editText_phone.setText("");

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

                    qrgEncoder = new QRGEncoder("https://yazilimmuhendisim.net/",null, QRGContents.Type.TEXT,smallerdimension);
                    bitmap = qrgEncoder.encodeAsBitmap();
                    imageView.setImageBitmap(bitmap);
                }catch (Exception e){

                }
            }
        });

    }
    private void save(String name, Bitmap bitmap) {
        try {
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
            Toast.makeText(QrGeneratorActivity.this,getResources().getString(R.string.save_basarili),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(QrGeneratorActivity.this,getResources().getString(R.string.error),Toast.LENGTH_SHORT).show();
        }
    }



    public static void hideKeyboard(Activity activity) {
        try{
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){

        }

    }
}
