package com.example.mobiiliprojekti;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/my_path")) {
            try{
                final String message = new String(messageEvent.getData());
                String[] array = message.split(";");
                Log.d("kimmo", "trying to launch");
                PackageManager pm = this.getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage(this.getPackageName());
                intent.putExtra("message", array);
                this.startActivity(intent);
            }catch(Exception e){
                Log.d("kimmo", e.toString());
            }
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}