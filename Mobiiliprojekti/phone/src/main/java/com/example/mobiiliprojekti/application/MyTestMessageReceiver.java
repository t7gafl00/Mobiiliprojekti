package com.example.mobiiliprojekti.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class MyTestMessageReceiver extends BroadcastReceiver {

    private String data;
    private final String TAG = "MyTestMessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Broadcastissa");
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            data = (String)bundle.getSerializable("message");
        }
            Log.i(TAG, data);
            new SendMessageThread("/my_path", data, context).start();
        }
    }

