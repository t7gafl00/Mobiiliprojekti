package com.example.mobiiliprojekti.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.example.mobiiliprojekti.model.ReminderItem;


public class ReminderBroadcastReceiver extends BroadcastReceiver {

    private ReminderItem reminder_item = null;

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.i("LOGIDEBUG", "onReceive: ");

        // 1. Get reminderItem from intent
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            reminder_item = (ReminderItem)bundle.getSerializable("REMINDER_ITEM");
        }

        // 2. Recreate alarm for next day
        ReminderAlarmManager alarmManager = new ReminderAlarmManager(context);
        alarmManager.createReminderAlarm(reminder_item);

        /* Reimplemented in TextToSpechManager

        // 3. Define intent in order to start a Service containing
        // the text-to-speech feature and notification
        Intent textToSpeechIntent = new Intent(context, TextToSpeechManager.class)
                .putExtra("MESSAGE", reminder_item.getName());

        // 4. Start the service with different methods depending on the version
        // of the device

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(textToSpeechIntent);
        } else{
            context.startService(textToSpeechIntent);
        }

        */
        TextToSpeechManager tts = new TextToSpeechManager();
        tts.say_now(context, reminder_item.getName());

        // 5. Send message to watch
        new SendMessageThread("/my_path",reminder_item.getName() + ";" + reminder_item.getCategory(), context).start(); //Starts a new thread to send a message to a clock
    }
}


