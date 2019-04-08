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
                //final String message = new String(messageEvent.getData());
                //PackageManager pm = this.getPackageManager();
                //Normal way did not work so intent is created using packagemanager.
                //Returns intent for launchable acttivity and in this case it is MainActivity.
                Intent intent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
                intent.putExtra("message", messageEvent.getData().toString());
                this.startActivity(intent);
            }catch(Exception e){
                //Process errors here. Currently this is not needed.
            }
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}