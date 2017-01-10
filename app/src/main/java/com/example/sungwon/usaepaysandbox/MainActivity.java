package com.example.sungwon.usaepaysandbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.usaepay.api.jaxws.UeSecurityToken;
import com.usaepay.api.jaxws.UeSoapServerPortType;
import com.usaepay.api.jaxws.usaepay;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Main";
    String SourceKey = "_95k469ivih82RKXPv7T9dE44vKHR71Y";
    String Pin = "p6wfNC";
    String ClientIP = "76.169.64.251";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            UeSoapServerPortType client = usaepay.getClient("sandbox.usaepay.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        try {
            UeSecurityToken token = usaepay.getToken(SourceKey, Pin, ClientIP);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }
}
