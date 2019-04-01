package com.example.mobiiliprojekti.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.model.ReminderModel;
import com.example.mobiiliprojekti.model.db.ReminderItemContract;

import java.util.Calendar;
import static android.content.Context.ALARM_SERVICE;
import static java.util.Calendar.*;

public class ReminderAlarmManager {
    private Context context;

    public ReminderAlarmManager(Context context)
    {
        this.context = context;
    }

    public void createReminderAlarm(ReminderItem reminderItem) {
        /* 1. Prepare data (id, hour, minute, message) */

        // Use reminderItem id from db (_id value) as our pendingIntent id
        // Each pendingIntent needs its own unique id, otherwise the last one created overwrites the previous one
        int reminder_id = (int) reminderItem.getDb_id();
        Log.i("LOGIDEBUG", "createReminderAlarm: first id=" + reminder_id);

        // Parse time string to two int values
        String reminder_time = reminderItem.getTime();
        String reminder_hour_String = reminder_time.substring(0, reminder_time.indexOf(":"));
        String reminder_minute_String = reminder_time.substring(reminder_time.indexOf(":") + 1);
        int reminder_hour = Integer.valueOf(reminder_hour_String);
        int reminder_minute = Integer.valueOf(reminder_minute_String);

        /* 2. Create alarm calendar object */
        Calendar alarm_Calendar = getInstance();
        alarm_Calendar.setTimeInMillis(System.currentTimeMillis());
        alarm_Calendar.set(HOUR_OF_DAY, reminder_hour);
        alarm_Calendar.set(MINUTE, reminder_minute);

        // Check whether the time is earlier than current time. If so, set it to tomorrow.
        // Otherwise, all alarms for earlier time will fire
        Calendar now_Calendar = getInstance();
        now_Calendar.add(MINUTE, 1);
        if(alarm_Calendar.before(now_Calendar)){
            alarm_Calendar.add(DATE, 1);
        }

        /* 3. Create intent */
        Intent intent = new Intent(this.context, ReminderBroadcastReceiver.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // reminderItem is passed in order to recreate alarm for the next day in BroadcastReceiver
        // A bundle must be used to pass reminderItem object,
        // as it doesn't work on the BroadcastReceiver side if we try to pass it directly to intent
        // using intent.putExtra(...
        Bundle bundle = new Bundle();
        bundle.putSerializable("REMINDER_ITEM", reminderItem);
        intent.putExtra("bundle", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, reminder_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /* 4. Set alarm */
        AlarmManager alarmManager = (AlarmManager)this.context.getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm_Calendar.getTimeInMillis(), pendingIntent);
        }
        else {
            // This one is for old versions (SDK < 19 ?), not tested.
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_Calendar.getTimeInMillis(), pendingIntent);
        }

        Log.i("LOGIDEBUG", "createReminderAlarm: Alarm created, id: " + reminder_id);
    }


    public void cancelReminderNotificationAlarm(int reminder_id) {
        Log.i("LOGIDEBUG", "cancelReminderNotificationAlarm: Alarm canceled, id:" + reminder_id);

        Intent intent = new Intent(this.context, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, reminder_id, intent, 0);
        AlarmManager alarmManager = (AlarmManager)this.context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void restoreAllReminderAlarms()
    {
        Log.i("LOGIDEBUG", "restoreAllReminderAlarms: ");
        ReminderModel reminderModel = new ReminderModel((this.context));;
        Cursor cursor = reminderModel.getReminderItemsList(0);

        // Fetch all reminders from database and create corresponding alarms
        while (cursor.moveToNext()) {
            int id = (int) cursor.getLong(cursor.getColumnIndex("_id"));
            String time = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_TIME)));
            String name = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_NAME)));
            String category = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_CATEGORY)));
            int checked = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_CHECKED));

            ReminderItem reminderItem = new ReminderItem(id, time, name, category, checked);
            createReminderAlarm(reminderItem);
        }
        cursor.close();
    }
}
