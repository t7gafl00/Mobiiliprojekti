package com.example.mobiiliprojekti.application;

import android.os.Bundle;
import android.os.Message;

import com.example.mobiiliprojekti.model.ReminderItem;
import com.example.mobiiliprojekti.ui.MainActivity;


import org.w3c.dom.Node;

import java.util.List;

public class SendMessageThread extends Thread {
    String path;
    String message;

    SendMessageThread(String p, String m) {
        path = p;
        message = m;
    }
    /*
    public void run() {

        Task<List<Node>> wearableList =
                Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
        try {
            List<Node> nodes = Tasks.await(wearableList);
            for (Node node : nodes) {
                Task<Integer> sendMessageTask =
                        Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), path, message.getBytes());
                try {
                    Integer result = Tasks.await(sendMessageTask);
                    sendmessage("I just sent the wearable a message " + sentMessageNumber++);

                } catch (Exception exception) {

                }
            }
        } catch (Exception exception) {

        }
    }
    */
    public void sendMessage(ReminderItem reminderItem) {
        Bundle bundle = new Bundle();
        String messageText = reminderItem.getName();
        String messageAction = reminderItem.getCategory();
        //String that is sent to clock application
        String messageString = messageAction + " * " + messageText;
/*
        bundle.putString("messageText", messageString);
        Message msg = myHandler.obtainMessage();
        msg.setData(bundle);
        myHandler.sendMessage(msg);
        */
    }
}
