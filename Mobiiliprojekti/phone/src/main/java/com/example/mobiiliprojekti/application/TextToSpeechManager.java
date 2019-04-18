package com.example.mobiiliprojekti.application;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;

import com.example.mobiiliprojekti.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class TextToSpeechManager extends Service implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener, ServiceConnection {

    TextToSpeech textToSpeech;
    private String spokenText;
    private Context context;
    private boolean attemptingToBind = false;
    private boolean bound = false;
    Notification notification;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int status) {
        // Initialize text-to-speech
        if(status == TextToSpeech.SUCCESS) {
            textToSpeech.setOnUtteranceCompletedListener(this);
            int result = textToSpeech.setLanguage(Locale.US);
            if(result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED){
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0");
                textToSpeech.speak(spokenText, TextToSpeech.QUEUE_FLUSH, params);
            }
        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        //Listen when text-to-speech object is finished speaking
        unbindFromService();
        stopSelf();
    }

    @Override
    public void onDestroy(){
        if(textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        spokenText = Objects.requireNonNull(intent.getExtras()).getString("MESSAGE");
        bindToService();
        textToSpeech = new TextToSpeech(this, this);
        int notificationID = 234;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder mBuilder = new Notification.Builder(this, ReminderApplication.CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("Reminder App")
                    .setContentText(spokenText)
                    .setAutoCancel(true);

            notification = mBuilder.build();
            startForeground(notificationID, notification);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("Reminder App")
                    .setContentText(spokenText)
                    .setAutoCancel(true);

            notification = mBuilder.build();
            startForeground(notificationID, notification);
        }
        stopForeground(false);
        return START_NOT_STICKY;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        attemptingToBind = false;
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }

    public void bindToService() {
        if(!attemptingToBind) {
            attemptingToBind = true;
            context.bindService(new Intent(context, TextToSpeechManager.class),
                    this, Context.BIND_AUTO_CREATE);
        }
    }

    public void unbindFromService() {
        attemptingToBind = false;
        if(bound) {
            context.unbindService(this);
            bound = false;
        }
    }

    public void say_now(Context context, String message) {
        // Define intent in order to start a Service containing
        // the text-to-speech feature and notification
        Intent textToSpeechIntent = new Intent(context, TextToSpeechManager.class)
                .putExtra("MESSAGE", message);

        // Start the service with different methods depending on the version
        // of the device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(textToSpeechIntent);
        } else{
            context.startService(textToSpeechIntent);
        }
    }
}
