package com.example.mobiiliprojekti.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mobiiliprojekti.R;
import com.example.mobiiliprojekti.application.ReminderAlarmManager;
import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.model.ReminderModel;

public class AddNewReminderActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Model to manipulate data in database
    private ReminderModel model = null;

    // Items from AddNewReminderActivity
    private TimePicker reminderTime_TimePicker = null;
    private Spinner category_Spinner;
    private TextView reminderName_EditText = null;
    private Button done_Button = null;

    private String category_from_Spinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder);

        model = new ReminderModel((this));

        /* Set up TimePicker */
        reminderName_EditText = findViewById(R.id.addNewReminder_name_EditText);
        reminderTime_TimePicker = findViewById(R.id.addNewReminder_time_TimePicker);
        reminderTime_TimePicker.setIs24HourView(true);

        /* Set up Spinner containing categories */
        category_Spinner = findViewById(R.id.category_Spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        category_Spinner.setAdapter(adapter);
        category_Spinner.setOnItemSelectedListener(this);

        /* Set up done Button */
        done_Button = findViewById(R.id.addNewReminder_done_Button);
        done_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        /* Handle press on "DONE" button */
        if (viewId == R.id.addNewReminder_done_Button) {
            saveReminder();
            this.finish();
        }
    }

    /* This function inserts an object built with data from AddNewReminderActivity to local database */
    private void saveReminder() {

        // Get time from TimePicker
        int hour;
        int minute;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = reminderTime_TimePicker.getHour();
            minute = reminderTime_TimePicker.getMinute();
        } else {
            //hour = reminderTime_TimePicker.getCurrentHour();
            hour = reminderTime_TimePicker.getHour();
            //minute = reminderTime_TimePicker.getCurrentMinute();
            minute = reminderTime_TimePicker.getMinute();

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

        // Get category from Spinner
        String category = category_from_Spinner;

        // Create object and insert data into db
        ReminderItem reminderItem = new ReminderItem(time, name, category, 0);
        int id_from_database = model.addReminderItemToDb(reminderItem);
        reminderItem.setDb_id(id_from_database);

        // Create notification
        ReminderAlarmManager reminderAlarmManager = new ReminderAlarmManager(this);
        reminderAlarmManager.createReminderAlarm(reminderItem);
    }

    /* Function executed when Spinner containing categories is used */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        category_from_Spinner = (String) parent.getItemAtPosition(position);
        category_from_Spinner = (String) parent.getItemAtPosition(position);
        Log.i("LOGIDEBUG", "onItemSelected: " + category_from_Spinner);

        //Change text's color according to category.
        switch (((String) parent.getItemAtPosition(position)).toLowerCase().toString()){
            case("drink"):
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.drinkWater));
                break;
            case("eat"):
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.eat));
                break;
            case("medication"):
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.medicine));
                break;
            case("shower"):
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.shower));
                break;
            case("social"):
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.social));
                break;
            case("toilet"):
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.toilet));
                break;
            case("warning"):
                ((TextView) parent.getChildAt(0)).setTextColor(this.getResources().getColor(R.color.alert));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
