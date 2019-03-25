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
    Context context;

    public ReminderAlarmManager(Context context)
    {
        this.context = context;
    }

    public void createReminderAlarm(ReminderItem reminderItem) {
        // 1. Prepare data (id, hour, minute, message)
        int id = 1;

        // Parse time string to two int values
        String reminder_time = reminderItem.getTime();
        String reminder_hour_String = reminder_time.substring(0, reminder_time.indexOf(":"));
        String reminder_minute_String = reminder_time.substring(reminder_time.indexOf(":") + 1);
        int reminder_hour = Integer.valueOf(reminder_hour_String);
        int reminder_minute = Integer.valueOf(reminder_minute_String);

        // 2. Create alarm calendar object
        Calendar alarm_Calendar = getInstance();
        alarm_Calendar.setTimeInMillis(System.currentTimeMillis());
        alarm_Calendar.set(HOUR_OF_DAY, reminder_hour);
        alarm_Calendar.set(MINUTE, reminder_minute);

        // Check whether the time is earlier than current time. If so, set it to tomorrow.
        // Otherwise, all alarms for earlier time will fire
        Calendar now = getInstance();
        now.add(MINUTE, 1);
        if(alarm_Calendar.before(now)){
            alarm_Calendar.add(DATE, 1);
        }

        // 3. Create intent
        Intent intent = new Intent(this.context, ReminderBroadcastReceiver.class);
        // reminderItem is passed in order to recreate alarm for the next day in BroadcastReceiver
        // A bundle must be used to pass reminderItem object,
        // as it doesn't work on the BroadcastReceiver side if we try to pass it directly to intent
        // using intent.putExtra(...
        Bundle bundle = new Bundle();
        bundle.putSerializable("REMINDER_ITEM", reminderItem);
        intent.putExtra("bundle", bundle);

        PendingIntent alarmIntent;
        alarmIntent = PendingIntent.getBroadcast(this.context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 4. Set alarm
        AlarmManager alarmMgr;
        alarmMgr = (AlarmManager)this.context.getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm_Calendar.getTimeInMillis(), alarmIntent);
        }
        else {
            // This one is for old versions (SDK < 19 ?), not tested.
            alarmMgr.set(AlarmManager.RTC_WAKEUP, alarm_Calendar.getTimeInMillis(), alarmIntent);
        }

        Log.i("LOGIDEBUG", "createReminderAlarm: Alarm created");
    }

/*
    private void cancelReminderNotificationAlarm(int id) {
        Intent myIntent = new Intent(this, ReminderBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();

        AlarmManager alarmManager;
        alarmManager.cancel(pendingIntent);
    }
*/

    public void restoreAllReminderAlarms()
    {
        ReminderModel model = new ReminderModel((this.context));;
        Cursor cursor = model.getReminderItemsList(0);

        // Fetch all reminders from db and create corresponding alarms
        while (cursor.moveToNext()) {

            String name = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_NAME)));
            String time = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_TIME)));
            int checked = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_CHECKED));

            ReminderItem reminderItem = new ReminderItem(name, time, checked);
            createReminderAlarm(reminderItem);
        }
        cursor.close();
    }
}
