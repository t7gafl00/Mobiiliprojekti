package com.example.mobiiliprojekti.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mobiiliprojekti.R;

public class OnBootBroadcastReceiver extends BroadcastReceiver {

    ReminderAlarmManager reminderAlarmManager = null;

    /* This functions is executed on device boot up
    ** It restores all alarms for the reminder notifications */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            reminderAlarmManager = new ReminderAlarmManager(context);
            reminderAlarmManager.restoreAllReminderAlarms();
            showNotification(context);
        }
    }

    /* Display notification when alarms are restored */
    private void showNotification(Context context) {
        // Set sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Show notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ReminderApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Reminder app")
                .setContentText("Notifications created")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(alarmSound);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(999999, mBuilder.build());
    }
}
