package com.example.beadando1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button scanButton;
    Button mentButton;
    TextView textView;
    private Adatbazis adatbazis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adatbazis = new Adatbazis(MainActivity.this);
        init();

        Context c = this;

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator =
                        new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        mentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrcode = textView.getText().toString();
                if (qrcode.length() > 0) {
                    String ip = wifiIpCim();
                    adatbazis.ujQRcodeFelvitel(qrcode, ip);
                    textView.setText("");
                    Toast.makeText(c, "Elmentve", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(c, "Nincs adat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null ) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Visszaléptél a scannelésből", Toast.LENGTH_SHORT).show();
            } else {
                textView.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void init(){
        scanButton = findViewById(R.id.scanButton);
        mentButton = findViewById(R.id.mentButton);
        textView = findViewById(R.id.textView);
    }

    private String wifiIpCim()
    {
        try {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) {
                WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();
                return Formatter.formatIpAddress(ip);
            }
        } catch (Exception e) {}
        return null;
    }

}