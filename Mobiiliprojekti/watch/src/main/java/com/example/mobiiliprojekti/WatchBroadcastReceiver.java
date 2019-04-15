package com.example.mobiiliprojekti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class WatchBroadcastReceiver extends BroadcastReceiver
{

/*
****************************************************************************************
Broadcast receiver to get message from watch when the boot has completed, so it then starts
logo showing activity.
****************************************************************************************
*/

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Intent i = new Intent(context, LogoAtBootActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}