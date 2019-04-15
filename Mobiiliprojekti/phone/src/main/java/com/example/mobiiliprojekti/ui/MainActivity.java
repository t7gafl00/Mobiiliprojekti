package com.example.mobiiliprojekti.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mobiiliprojekti.R;
import com.example.mobiiliprojekti.application.ReminderAlarmManager;
import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.model.ReminderItemArrayAdapter;
import com.example.mobiiliprojekti.model.ReminderModel;
import com.example.mobiiliprojekti.model.db.ReminderItemContract;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ArrayList<ReminderItem> reminderItems_ArrayList = new ArrayList<>();
    ListView listView = null;

    ReminderModel model = null;
    ReminderItem reminderItem = null;

    Context context;
    private String category_from_Spinner;
    protected Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner)findViewById(R.id.spinner_categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
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
                                refreshList(0);
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
                refreshList(0);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* Handle clicks on categories icons
    * Does nothing at the moment */
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Sort according to date
            case R.id.category_1:
                refreshList(0);

                return true;
            case R.id.category_2:
                ReminderAlarmManager reminderAlarmManager2 = new ReminderAlarmManager(this.getApplicationContext());
                reminderAlarmManager2.restoreAllReminderAlarms();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
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
        refreshList(0);
    }

    /* Refresh item list depending on what category has been selected (filter_value) */
    private void refreshList(int filter_value) {

        // Clear list
        reminderItems_ArrayList.clear();

        // Get all items based on filter_value
        Cursor cursor = model.getReminderItemsList(filter_value);

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
        category_from_Spinner = (String) parent.getItemAtPosition(position);
        category_from_Spinner = (String) parent.getItemAtPosition(position);

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
