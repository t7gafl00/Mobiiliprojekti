package com.example.mobiiliprojekti;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

    boolean flip = false;
    public static boolean done = false;

    BoxInsetLayout myLayout;
    TextView myNotificationText;
    ImageView myImage;
    int imageCode;
    int imageResource;
    String textPlaceholder;
    String imagePlaceholder;
    LinearLayout textLayout;


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

        //startService();


/*
 *******************************************************************
 Initializing layout, notification text, image and vibrationservice
 *******************************************************************
 */
        myLayout = findViewById(R.id.boxInsetLayout);
        myNotificationText = new TextView(MainActivity.this);
        myNotificationText.setTextSize(70);
        myNotificationText.setTypeface(Typeface.DEFAULT_BOLD);
        myNotificationText.setTextColor(ContextCompat.getColor(this, R.color.white));

        textLayout = new LinearLayout(MainActivity.this);
        textLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        myLayout.addView(textLayout, textLayout.getLayoutParams());
        myNotificationText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        myNotificationText.setGravity(Gravity.CENTER);
        myNotificationText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textLayout.addView(myNotificationText, myNotificationText.getLayoutParams());


        myImage = new ImageView(MainActivity.this);

        myVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Enables Always-on
        //setAmbientEnabled();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        timer = new Timer();
        repeatCounter = 0;
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
                //textPlaceholder = getText(messageFromPhone);
                //imageCode = getInt(intFromPhone);
                imageCode = 4;
                imagePlaceholder = iconChoice(imageCode);
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
                    moveTaskToBack(true);
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
        done = true;
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
                    //myNotificationText.setText(textPlaceholder);
                    myNotificationText.setText("EAT");
                    //myLayout.addView(myNotificationText);
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
    public String iconChoice(int code)
    {
        String imageName = "";
        switch(code)
        {

            case 1:
                imageName = "icons_colors_01";
            break;

            case 2:
                imageName = "icons_colors_02";
            break;

            case 3:
                imageName = "icons_colors_03";
            break;

            case 4:
                imageName = "icons_colors_04";
            break;

            case 5:
                imageName = "icons_colors_05";
            break;

            case 6:
                imageName = "icons_colors_06";
            break;

            case 7:
                imageName = "icons_colors_07";
            break;
        }

        return imageName;
    }



    // public void startService() {
    //startService(new Intent(getBaseContext(), MyService.class));
    //  }
}
