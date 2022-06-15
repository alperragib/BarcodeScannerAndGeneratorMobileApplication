package com.yazilimmuhendisim.qrcodescannerandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        ScannerView.setClickable(true);
        setContentView(ScannerView);


    }

    @Override
    public void handleResult(Result result) {

        if(!result.getText().isEmpty()){
            finish();
            Intent intent = new Intent(ScannerActivity.this,ResultActivity.class);
            intent.putExtra("result",result.getText());
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}

