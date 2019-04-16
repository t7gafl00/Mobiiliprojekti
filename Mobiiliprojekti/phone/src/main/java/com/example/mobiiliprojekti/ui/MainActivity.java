package com.example.mobiiliprojekti.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.example.mobiiliprojekti.R;
import com.example.mobiiliprojekti.application.BeaconController;
import com.example.mobiiliprojekti.application.ReminderAlarmManager;
import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.model.ReminderItemArrayAdapter;
import com.example.mobiiliprojekti.model.ReminderModel;
import com.example.mobiiliprojekti.model.db.ReminderItemContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ArrayList<ReminderItem> reminderItems_ArrayList = new ArrayList<>();
    ListView listView = null;

    ReminderModel model = null;
    ReminderItem reminderItem = null;

    Context context;
    protected Handler myHandler;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        BeaconController beaconController = (BeaconController) getApplication();
        context = beaconController.getApplicationContext();

        //Require permission for beacon scanning
        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("LOGIDEBUG", "requirements fulfilled");
                                //application.enableBeaconNotifications();
                                return null;
                            }
                        },
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("LOGIDEBUG", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("LOGIDEBUG", "requirements error: " + throwable);
                                return null;
                            }
                        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = findViewById(R.id.spinner_categories);
        Spinner spinner = (Spinner)findViewById(R.id.spinner_categories);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        String[] array = {"drink", "eat", "medication", "shower", "social", "toilet", "warning"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        DropDownMenuAdapter adapter = new DropDownMenuAdapter(this, R.layout.spinner_item, arrayList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Tarvitaan
        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                //messageText(stuff.getString("messageText"));
                return true;
            }
        });


        model = new ReminderModel((this));
        listView = findViewById(R.id.main_list_view);
        findViewById(R.id.add_FloatingActionButton).setOnClickListener(this);

        // Long click on item in ListView to remove it
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you really want to remove this item ?")
                        .setTitle("Remove item")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User confirmed action
                                reminderItem = (ReminderItem) listView.getItemAtPosition(position);
                                //int reminder_id = model.getId(reminderItem);

                                //ReminderAlarmManager reminderAlarmManager = new ReminderAlarmManager(this.getApplicationContext());
                                ReminderAlarmManager reminderAlarmManager = new ReminderAlarmManager(getApplicationContext());
                                reminderAlarmManager.cancelReminderNotificationAlarm((int) reminderItem.getDb_id());

                                model.deleteReminderItemFromDb(reminderItem);
                                refreshList("all");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled action
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.reminderListItem_checked_CheckBox:
                // Get reminder item whose checkbox was pressed on
                View parentRow = (View) view.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);
                reminderItem = (ReminderItem) listView.getItemAtPosition(position);

                // Set checkbox value
                if (checked) {
                    model.setChecked(reminderItem, 1);
                } else {
                    model.setChecked(reminderItem, 0);
                }
                refreshList("all");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Click on "+" button
            case R.id.add_FloatingActionButton:
                Intent addNewToDoItem = new Intent(this, AddNewReminderActivity.class);
                startActivity(addNewToDoItem);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList("all");
    }

    /* Refresh item list depending on what category has been selected (category_filter) */
    private void refreshList(String category_filter) {
        // Clear list
        reminderItems_ArrayList.clear();

        // Get all items based on filter_value
        Cursor cursor = model.getReminderItemsList(category_filter);

        // Fetch data from items in db and add them to ArrayList
        while (cursor.moveToNext()) {
            int id = (int) cursor.getLong(cursor.getColumnIndex("_id"));
            String time = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_TIME)));
            String category = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_CATEGORY)));
            String name = (cursor.getString(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_NAME)));
            int checked = cursor.getInt(cursor.getColumnIndexOrThrow(ReminderItemContract.ReminderItem.COLUMN_NAME_CHECKED));

            ReminderItem reminderItem = new ReminderItem(id, time, name, category, checked);
            reminderItems_ArrayList.add(reminderItem);
        }
        cursor.close();

        // Update ArrayAdapter
        ReminderItemArrayAdapter adapter = new ReminderItemArrayAdapter(this, reminderItems_ArrayList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Change text's color according to category.
        String category = ((String) parent.getItemAtPosition(position)).toLowerCase().toString();
        switch (category){
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
        refreshList(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
