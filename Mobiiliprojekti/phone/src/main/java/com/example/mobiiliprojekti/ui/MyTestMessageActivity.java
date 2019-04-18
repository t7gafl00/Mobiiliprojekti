package com.example.mobiiliprojekti.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.mobiiliprojekti.R;
import com.example.mobiiliprojekti.application.MyTestMessageReceiver;

import java.util.ArrayList;
import java.util.Arrays;

public class MyTestMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Spinner category_Spinner;
    private EditText text = null;
    private String category_from_Spinner = null;
    private String message = null;
    private Button sendButton = null;
    private Context context;

    private final String TAG = "MyTestMessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_message);

        text = findViewById(R.id.test_text);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = text.getText().toString();
                String sendMessage = message + ';' + category_from_Spinner.toLowerCase();

                Intent intent = new Intent(MyTestMessageActivity.this, MyTestMessageReceiver.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", sendMessage);
                intent.putExtra("bundle", bundle);
                Log.i(TAG, "sending intent");
                sendBroadcast(intent);


/*                Intent intent = new Intent(MyTestMessageActivity.this, MyTestMessageReceiver.class);
                intent.putExtra("message", sendMessage);
                startActivity(intent);*/
/*                Intent intent = new Intent();
                intent.setAction("com.example.mobiiliprojekti.application.MyTestMessageReceiver");
                intent.putExtra("message", sendMessage);
                sendBroadcast(intent);*/
                //MyTestMessageActivity.this.finish();
            }
        });

        /* Set category Spinner */
        category_Spinner = findViewById(R.id.test_Spinner);
        category_Spinner.getBackground().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        // Get contents (= category names)
        Resources resources = getResources();
        String[] array = resources.getStringArray(R.array.categories_array);
        // Create an adapter
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
        DropDownMenuAdapter adapter = new DropDownMenuAdapter(this, R.layout.drop_down_menu_item_custom, arrayList);
        // Apply the adapter to the spinner
        category_Spinner.setAdapter(adapter);
        category_Spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        category_from_Spinner = (String) parent.getItemAtPosition(position);
        // Change the color of the category selected in category spinner
        switch (((String) parent.getItemAtPosition(position)).toLowerCase()) {
            case ("drink"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.drinkWater));
                break;
            case ("eat"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.eat));
                break;
            case ("medication"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.medicine));
                break;
            case ("shower"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.shower));
                break;
            case ("social"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.meeting));
                break;
            case ("toilet"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.toilet));
                break;
            case ("warning"):
                ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.alert));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

