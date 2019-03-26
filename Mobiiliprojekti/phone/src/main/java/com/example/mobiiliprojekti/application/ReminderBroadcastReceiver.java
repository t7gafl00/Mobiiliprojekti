package com.example.mobiiliprojekti.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.mobiiliprojekti.R;
import com.example.mobiiliprojekti.model.ReminderItem;

import java.io.Serializable;
import java.util.Calendar;

import static android.content.Intent.getIntent;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    private ReminderItem reminder_item = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("LOGIDEBUG", "onReceive: ");

        // 1. Get reminderItem from intent
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            reminder_item = (ReminderItem)bundle.getSerializable("REMINDER_ITEM");
        }

        // 2. Recreate alarm for next day
        ReminderAlarmManager alarmManager = new ReminderAlarmManager(context);
        alarmManager.createReminderAlarm(reminder_item);

        // 3. Build and show notification
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ReminderApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Reminder App")
                .setContentText(reminder_item.getName())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(alarmSound);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(234, mBuilder.build());
    }
}
