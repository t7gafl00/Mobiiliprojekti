package com.example.mobiiliprojekti.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.mobiiliprojekti.R;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TEST", "onReceive: ");

        // Get reminder text from sent broadcast
        String text = intent.getExtras().getString("TEXT");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        // Show notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ReminderApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Reminder App")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(alarmSound);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(234, mBuilder.build());
    }
}
