package com.example.mobiiliprojekti.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mobiiliprojekti.R;
import com.example.mobiiliprojekti.application.ReminderAlarmManager;
import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.model.ReminderModel;

import java.util.ArrayList;
import java.util.Arrays;

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
        getSupportActionBar().hide();

        model = new ReminderModel((this));

        //Set up TimePicker
        reminderName_EditText = findViewById(R.id.addNewReminder_name_EditText);
        reminderTime_TimePicker = findViewById(R.id.addNewReminder_time_TimePicker);
        reminderTime_TimePicker.setIs24HourView(true);

        //Set up Spinner containing categories
        category_Spinner = findViewById(R.id.category_Spinner);
        category_Spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        // Create an ArrayAdapter using the string array and a default spinner layout

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/


        //Create an ArrayAdapter using the string array and a custom spinner layout
        //Resources resources = getResources();
        /*//String[] array = resources.getStringArray(R.array.categories_array);
        int ID = getResources().getIdentifier(
                "categories_array",
                "values",
                this.getPackageName()
        );
        Log.d("kimmo", Integer.toString(ID));
        String[] array = getResources().getStringArray(ID);*/
        String[] array = {"drink", "eat", "medication", "shower", "social", "toilet", "warning"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        DropDownMenuAdapter adapter = new DropDownMenuAdapter(this, R.layout.drop_down_menu_item_custom, arrayList);

        // Apply the adapter to the spinner
        Log.d("kimmo", "asetetaan adapteri");
        category_Spinner.setAdapter(adapter);
        Log.d("kimmo", "asetetaan OnItemSelectedListener");
        category_Spinner.setOnItemSelectedListener(this);

        /* Set up done Button */
        done_Button = findViewById(R.id.addNewReminder_done_Button);
        done_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.d("kimmo", "onClick: ");
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
        Log.d("kimmo", "saveReminder: ");
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
        switch (((String) parent.getItemAtPosition(position)).toLowerCase()){
            case("drink"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.drinkWater));
                break;
            case("eat"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.eat));
                break;
            case("medication"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.medicine));
                break;
            case("shower"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.shower));
                break;
            case("social"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.meeting));
                break;
            case("toilet"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.toilet));
                break;
            case("warning"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.alert));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
