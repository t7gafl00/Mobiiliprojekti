package com.example.mobiiliprojekti.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.ui.MainActivity;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Random;


public class ReminderBroadcastReceiver extends BroadcastReceiver {

    private ReminderItem reminder_item = null;
    protected Handler myHandler;


    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i("LOGIDEBUG", "onReceive: ");

        // 1. Get reminderItem from intent
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            reminder_item = (ReminderItem)bundle.getSerializable("REMINDER_ITEM");
        }

        // 2. Recreate alarm for next day
        ReminderAlarmManager alarmManager = new ReminderAlarmManager(context);
        alarmManager.createReminderAlarm(reminder_item);

        // 3. Define intent in order to start a Service containing
        // the text-to-speech feature and notification
        Intent textToSpeechIntent = new Intent(context, TextToSpeechManager.class)
                .putExtra("MESSAGE", reminder_item.getName());

        // 4. Start the service with different methods depending on the version
        // of the device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(textToSpeechIntent);
        } else{
            context.startService(textToSpeechIntent);
        }
        class NewThread extends Thread {
            String path;
            String message;

            public NewThread(String p, String m) {
                path = p;
                message = m;
            }

            public void run() {

                Task<List<Node>> wearableList =
                        Wearable.getNodeClient(context).getConnectedNodes();
                try {
                    List<Node> nodes = Tasks.await(wearableList);
                    for (Node node : nodes) {
                        Task<Integer> sendMessageTask =
                                Wearable.getMessageClient(context).sendMessage(node.getId(), path, message.getBytes());
                        try {
                            Integer result = Tasks.await(sendMessageTask);
                            sendmessage(message);

                        } catch (Exception exception) {

                        }
                    }
                } catch (Exception exception) {

                }
            }

            public void sendmessage(String messageText) {
                Bundle bundle = new Bundle();
                bundle.putString("messageText", messageText);
                Message msg = myHandler.obtainMessage();
                Log.d("testi", msg.toString());
                msg.setData(bundle);
                myHandler.sendMessage(msg);
                Log.d("testi", msg.toString());
            }
        }
        new NewThread("/my_path", reminder_item.getName() + ";" + reminder_item.getCategory()).start();
    }
}


