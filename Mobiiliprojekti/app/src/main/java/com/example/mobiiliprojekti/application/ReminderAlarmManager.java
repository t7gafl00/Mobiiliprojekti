package com.example.mobiiliprojekti.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.model.ReminderModel;
import com.example.mobiiliprojekti.model.db.ReminderItemContract;

import java.util.Calendar;
import static android.content.Context.ALARM_SERVICE;

public class ReminderAlarmManager {
    Context context;

    public ReminderAlarmManager(Context context)
    {
        this.context = context;
    }

    public void createReminderAlarm(ReminderItem reminderItem) {

        // 1. Prepare data (id, hour, minute, message)
        int id = 0;

        // Parse time string to two int values
        String reminder_time = reminderItem.getTime();
        String reminder_hour_String = reminder_time.substring(0, reminder_time.indexOf(":"));
        String reminder_minute_String = reminder_time.substring(reminder_time.indexOf(":") + 1);
        int reminder_hour = Integer.valueOf(reminder_hour_String);
        int reminder_minute = Integer.valueOf(reminder_minute_String);

        String reminder_message = reminderItem.getName();

        // 2. Create alarm
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager)this.context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this.context, ReminderBroadcastReceiver.class);
        intent.putExtra("TEXT", reminder_message);
        alarmIntent = PendingIntent.getBroadcast(this.context, id, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, reminder_hour);
        calendar.set(Calendar.MINUTE, reminder_minute);

        // Check whether the time is earlier than current time. If so, set it to tomorrow.
        // Otherwise, all alarms for earlier time will fire
        Calendar now = Calendar.getInstance();
        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        // CONTINUE HERE
        alarmMgr.setExactAndAllowWhileIdle(0, );
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

        // Fetch reminders from db and create corresponding alarms
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
