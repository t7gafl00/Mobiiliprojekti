package com.example.mobiiliprojekti;

import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Typeface;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wear.widget.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends WearableActivity {

    private static final String TAG = "MyWatchApp";

    private boolean flip = false;

    private BoxInsetLayout myLayout;
    private TextView myNotificationText;
    private ImageView myImage;
    private String codeString;
    private int imageResource;
    private String textPlaceholder;
    private String imagePlaceholder;
    private LinearLayout textLayout;
    private Intent intent;


    Vibrator myVibrator;
    VibrationEffect effect;
    long[] mVibratePattern = new long[]{500, 100, 500, 100, 500};
    int[] myAmplitude = new int[]{255, 0, 255, 0, 255};

    Timer timer;
    public static int repeatCounter = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");


/*
 *******************************************************************
 Initializing layout, notification text, image and vibratorservice
 *******************************************************************
 */

        myLayout = findViewById(R.id.boxInsetLayout);
        myLayout.setVisibility(View.VISIBLE);
        myNotificationText = new TextView(MainActivity.this);
        myNotificationText.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        myNotificationText.setTypeface(Typeface.DEFAULT_BOLD);
        myNotificationText.setTextColor(ContextCompat.getColor(this, R.color.white));
        myNotificationText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        myNotificationText.setGravity(Gravity.CENTER);
        myNotificationText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        textLayout = new LinearLayout(MainActivity.this);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        textLayout.addView(myNotificationText, myNotificationText.getLayoutParams());

        myLayout.addView(textLayout, textLayout.getLayoutParams());



        myImage = new ImageView(MainActivity.this);

        myVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        timer = new Timer();
        repeatCounter = 0;
        flip = false;

        intent = getIntent();
        try
        {
            textPlaceholder = intent.getSerializableExtra("message").toString();
            textPlaceholder = parseMessage(textPlaceholder);

        }
        catch(Exception e)
        {
            textPlaceholder = "Error fetching message;warning";
            textPlaceholder = parseMessage(textPlaceholder);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

/*
 *******************************************************************
 This timer will run in a loop, running the refreshScreen function that puts either the icon or the text on the screen,
 depending on the state of flip bool. The function first fetches what text and icon to show in the alarm. repeatCounter
 increases each run-through, until it reaches 6, at which point the activity will move to the background.
 *******************************************************************
 */
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                imagePlaceholder = iconChoice(codeString);
                imageResource = getResources().getIdentifier(imagePlaceholder, "drawable", getPackageName());

                Log.i(TAG, "Entered run loop");
                if(repeatCounter < 6)
                {
                    refreshScreen(imageResource, flip);
                    effect = VibrationEffect.createWaveform(mVibratePattern, myAmplitude, -1);
                    myVibrator.vibrate(effect);
                    flip = !flip;
                    repeatCounter++;
                }
                else
                {
                    Log.i(TAG, "Closing Activity");
                    finish();
                }
            }

        }, 0, 3000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
/*
*************************************
Cancelling timer, just incase
*************************************
*/
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    /*
    ***************************************************************************************************
    refreshScreen function takes flip as argument to determine whether to show text or show image on the
    screen. It is declared to run on ui thread, as it gets called from another thread.
    ***************************************************************************************************
    */
    public void refreshScreen(final int imageResource, final boolean choice)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                if (choice == true)
                {
                    myLayout.removeAllViews();
                    myLayout.addView(textLayout, textLayout.getLayoutParams());
                    myNotificationText.setText(textPlaceholder);
                    setContentView(myLayout);

                }
                else
                {
                    myLayout.removeAllViews();
                    myImage.setImageResource(imageResource);
                    myLayout.addView(myImage);
                    setContentView(myLayout);
                }
            }
        });
    }

/*
*****************************************************************************************************
Function to send out image resource ID for fetching the right icon to use in alarm.
*******************************************************************************************************
*/
    public String iconChoice(String code)
    {
        String imageName = "";
        switch(code)
        {

            case "medication":
                imageName = "icons_colors_01";
                break;

            case "eat":
                imageName = "icons_colors_02";
                break;

            case "toilet":
                imageName = "icons_colors_03";
                break;

            case "shower":
                imageName = "icons_colors_04";
                break;

            case "drink":
                imageName = "icons_colors_05";
                break;

            case "social":
                imageName = "icons_colors_06";
                break;

            case "warning":
                imageName = "icons_colors_07";
                break;
        }

        return imageName;
    }

/*
*****************************************************************************************************
Function to parse message brought from MessageService. Assuming the message comes as one string, with
the message first on it, code after, separated by ';'. So the function first replaces all the spaces
with newlines to make the text appear nicely on the watch; then it splits the message to two variables;
after that the variables are set and parsed into their own variables. Function returns the message part,
while the code gets set inside the function to the class variable imageCode.
*******************************************************************************************************
*/
    public String parseMessage(String message)
    {
        String parsedMessage = "";
        parsedMessage = message.replaceAll(" ", "\n");
        String [] splitMessage = parsedMessage.split(";");
        parsedMessage = splitMessage[0];
        codeString = splitMessage[1];

        return parsedMessage;
    }
}
