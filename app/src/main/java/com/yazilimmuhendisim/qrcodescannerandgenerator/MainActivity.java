package com.yazilimmuhendisim.qrcodescannerandgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    LinearLayout social_bar,banner_ads;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner_ads = findViewById(R.id.linear_banner_ads);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
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

        social_bar = findViewById(R.id.lineer_social_bar);

        TextView textViewSurum = findViewById(R.id.textView2);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            textViewSurum.setText(version);

        } catch(PackageManager.NameNotFoundException e) {
            textViewSurum.setText("1.0.4");
        }


        CardView cardView1 = findViewById(R.id.card1);
        CardView cardView2 = findViewById(R.id.card2);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestWriteExternalPermission();
            }
        });

        FloatingActionButton button_share = findViewById(R.id.button10);
        final FloatingActionButton button_rate= findViewById(R.id.button11);
        final FloatingActionButton button_social= findViewById(R.id.button12);
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, getResources().getString(R.string.app_name));
                startActivity(shareIntent);

            }catch (Exception e){

            }
            }
        });

        button_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        button_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(social_bar.getVisibility()==View.GONE){
                    social_bar.setVisibility(View.VISIBLE);
                    button_social.setImageResource(R.drawable.ic_arrow_upward);
                }else{
                    social_bar.setVisibility(View.GONE);
                    button_social.setImageResource(R.drawable.ic_arrow_downward);
                }
            }
        });

        FloatingActionButton button_web,button_instagram,button_facebook,button_linkedin;

        button_web=findViewById(R.id.button13);
        button_instagram=findViewById(R.id.button14);
        button_facebook=findViewById(R.id.button15);
        button_linkedin=findViewById(R.id.button16);

        button_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yazilimmuhendisim.net/"));
                    startActivity(browserIntent);
                }catch (Exception e){

                }
            }
        });
        button_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Uri uri = Uri.parse("http://instagram.com/_u/yazilimmuhendisim");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                startActivity(likeIng);

                }
                catch (ActivityNotFoundException e)
                {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/yazilimmuhendisim")));
                }
            }
        });
        button_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("https://www.facebook.com/yazilimmuhendisim");
                    PackageManager packageManager = getPackageManager();
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo("com.facebook.katana", 0);
                    if (applicationInfo.enabled) {
                        uri = Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/yazilimmuhendisim");
                    }

                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                } catch (PackageManager.NameNotFoundException ignored) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/yazilimmuhendisim")));
                }
            }
        });
        button_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri linkedUri = Uri.parse("https://tr.linkedin.com/in/yaz%C4%B1l%C4%B1m-m%C3%BChendisim-8278a1182");
                    Intent linkedInt = new Intent(Intent.ACTION_VIEW, linkedUri);
                    linkedInt.setPackage("com.linkedin.android");

                    if (linkedInt.resolveActivity(getPackageManager()) != null) {
                        startActivity(linkedInt);
                    }else{
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://tr.linkedin.com/in/yaz%C4%B1l%C4%B1m-m%C3%BChendisim-8278a1182")));
                    }
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://tr.linkedin.com/in/yaz%C4%B1l%C4%B1m-m%C3%BChendisim-8278a1182")));
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button_rate.setTooltipText(getResources().getString(R.string.fab_rate));
            button_social.setTooltipText(getResources().getString(R.string.fab_more));
            button_share.setTooltipText(getResources().getString(R.string.fab_share));
        }

    }

    private void requestCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Intent intent_scanner = new Intent(MainActivity.this,ScannerActivity.class);
            startActivity(intent_scanner);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},2342);
        }

    }
    private void requestWriteExternalPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent_generator = new Intent(MainActivity.this,QrGeneratorActivity.class);
            startActivity(intent_generator);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2323);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2342){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent_scanner = new Intent(MainActivity.this,ScannerActivity.class);
                startActivity(intent_scanner);
            }else{

            }

        }
        if(requestCode==2323){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent_generator = new Intent(MainActivity.this,QrGeneratorActivity.class);
                startActivity(intent_generator);
            }else{

            }

        }
    }
}
