package com.example.mobiiliprojekti.application;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.List;

public class SendMessageThread extends Thread {    //Class for sending messages to clock
    private String path;                           //An unique identifier for wearable to access the message
    private String message;                        //The message to wearable
    private Context context;
    protected Handler handler;

    public SendMessageThread(String mPath, String mMessage, Context context) {
        path = mPath;
        message = mMessage;
        this.context = context;
    }

    public void sendmessage(String messageText) {   //Function that is called inside the run-function in NewThread class
        Bundle bundle = new Bundle();
        bundle.putString("messageText", messageText);
        Message msg = handler.obtainMessage(); //Returns a new message from  the global message pool
        msg.setData(bundle);
        handler.sendMessage(msg); //Pushes a message onto the end of the message queue
    }

    public void run() {
        //Gets a list of connected nodes to which the device is connected
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
}