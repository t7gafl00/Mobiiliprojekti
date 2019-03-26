package com.example.mobiiliprojekti.ui;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mobiiliprojekti.R;
import com.example.mobiiliprojekti.application.ReminderAlarmManager;
import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.model.ReminderModel;

public class AddNewReminderActivity extends AppCompatActivity implements View.OnClickListener {

    // Model to manipulate data in database
    private ReminderModel model = null;

    // Items from AddNewReminderActivity
    private TimePicker reminderTime_TimePicker = null;
    private TextView reminderName_EditText = null;
    private Button done_Button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder);

        model = new ReminderModel((this));

        reminderName_EditText = findViewById(R.id.addNewReminder_name_EditText);
        reminderTime_TimePicker = findViewById(R.id.addNewReminder_time_TimePicker);
        reminderTime_TimePicker.setIs24HourView(true);

        done_Button = findViewById(R.id.addNewReminder_done_Button);
        done_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        // Handle press on "DONE" button
        if (viewId == R.id.addNewReminder_done_Button) {
            saveReminder();
            this.finish();
        }
    }

    /* this function inserts an object built with data from AddNewReminderActivity to local database */
    private void saveReminder() {

        // Get time from TimePicker
        int hour;
        int minute;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = reminderTime_TimePicker.getHour();
            minute = reminderTime_TimePicker.getMinute();
        }
        else {
            hour = reminderTime_TimePicker.getCurrentHour();
            minute = reminderTime_TimePicker.getCurrentMinute();
        }

        // Append leading zeros to get time in form HH:MM
        String hour_string = String.valueOf(hour);
        if (hour < 10) {
            hour_string = "0" + hour_string;
        }
        String minute_string = String.valueOf(minute);
        if (minute < 10) {
            minute_string = "0" + minute_string;
        }

        // Build time string
        String time = hour_string + ":" + minute_string;

        // Get name of reminder from EditText
        String name = String.valueOf(reminderName_EditText.getText());

        // Create object and insert data into db
        ReminderItem reminderItem = new ReminderItem(name, time, 0);
        int id_from_database = model.addReminderItemToDb(reminderItem);
        reminderItem.setDb_id(id_from_database);

        // Create notification
        ReminderAlarmManager reminderAlarmManager = new ReminderAlarmManager(this);
        reminderAlarmManager.createReminderAlarm(reminderItem);
    }
}
