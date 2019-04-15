package com.example.mobiiliprojekti;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.WindowManager;


import java.util.Timer;
import java.util.TimerTask;

public class LogoAtBootActivity extends WearableActivity {


    private Timer timer;
    private Boolean timeToClose = false;



/*
*************************************************************************************
Activity for showing Memini logo at startup. As the watch's startup is so slow, it
takes a while to show. It simply shows the logo for few seconds.
**************************************************************************************
*/

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

