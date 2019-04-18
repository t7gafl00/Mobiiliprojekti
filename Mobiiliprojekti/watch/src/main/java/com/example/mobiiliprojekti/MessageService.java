package com.example.mobiiliprojekti;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageService extends WearableListenerService {

    private static final String TAG = "WatchMessage";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/my_path")) {
            try{
                final String message = new String(messageEvent.getData());
                PackageManager pm = this.getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage(this.getPackageName());
                intent.putExtra("message", message);
                Log.d("testi", message);
                this.startActivity(intent);
            }catch(Exception e){
                //Process error here
            }
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}