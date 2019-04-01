package com.example.mobiiliprojekti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class WatchBroadcastReceiver extends BroadcastReceiver {

    private TextView textView;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        Here we should use the SAME INTENT created in ReminderAlarmManager for two different BroadcastReceivers
        (first BroadcastReceiver on the phone and second one on the watch)
        */
        String message = intent.getSerializableExtra("message").toString();
        String[] separated = message.split("separator");
        String onMessageReceived = "I just received a  message from the handheld\r\n" + intent.getSerializableExtra("message").toString();
        textView.setText(onMessageReceived);
    }
}
