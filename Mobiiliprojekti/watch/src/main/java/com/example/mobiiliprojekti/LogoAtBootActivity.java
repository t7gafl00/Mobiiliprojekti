package com.example.mobiiliprojekti;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wear.widget.BoxInsetLayout;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class LogoAtBootActivity extends WearableActivity {

    /*    private BoxInsetLayout myLayout;
        private ImageView myImage;*/
    private Timer timer;
    private Boolean timeToClose = false;

/*    Vibrator myVibrator;
    VibrationEffect effect;
    long[] mVibratePattern = new long[]{500, 100, 500, 100, 500};
    int[] myAmplitude = new int[]{255, 0, 255, 0, 255};*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.logo);

        timer = new Timer();

    }


    @Override
    protected void onResume() {
        super.onResume();

        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                if(timeToClose)
                {
                    finish();
                }
                else
                {
                    timeToClose = true;
                }
            }
        }, 0, 5000);
    }
}



/*

        myLayout = findViewById(R.id.boxInsetLayout);
        myImage = new ImageView(LogoAtBootActivity.this);
        myImage.setImageResource(getResources().getIdentifier("testi_ikoni", "drawable", getPackageName()));
        myLayout.addView(myImage);
        setContentView(myLayout);

        myVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        timer = new Timer();
        timeToClose = false;

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()

            {
                if(timeToClose == true)
                {
                    finish();
                }
                else
                {
                    effect = VibrationEffect.createWaveform(mVibratePattern, myAmplitude, -1);
                    myVibrator.vibrate(effect);
                    timeToClose = true;
                }
            }

        }, 0, 3000);

    }

    @Override
    protected void onStop() {
        super.onStop();
*/
/*
*************************************
Cancelling timer, just incase
*************************************
*//*



        timer.cancel();
    }

}

*/
