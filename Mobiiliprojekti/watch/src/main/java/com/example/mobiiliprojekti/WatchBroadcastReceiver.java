package com.example.mobiiliprojekti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WatchBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        Here we should use the SAME INTENT created in ReminderAlarmManager for two different BroadcastReceivers
        (first BroadcastReceiver on the phone and second one on the watch)
        */
    }
}
