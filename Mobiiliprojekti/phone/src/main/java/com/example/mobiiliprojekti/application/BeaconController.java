package com.example.mobiiliprojekti.application;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.mobiiliprojekti.ui.MainActivity;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;


public class BeaconController extends Application implements BeaconConsumer {

    private BackgroundPowerSaver backgroundPowerSaver;
    private Context context;
    private NotificationManager notificationManager;
    private BeaconManager beaconManager;
    private Region doorRegion, toiletRegion;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        notificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);

        //Create different regions based on the personal UUID, Major and Minor of each beacon
        doorRegion = new Region("DoorRegion", Identifier.parse("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Identifier.parse("62342"),Identifier.parse("15558"));
        toiletRegion = new Region("WcRegion", Identifier.parse("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), Identifier.parse("60298"),Identifier.parse("31914"));

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        //Specify a beacon model that we are scanning for.
        //Every manufacturer has a different layout
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        Log.i("LOGIDEBUG", Integer.toString(beaconManager.getBeaconParsers().size()));

        //Set scans to appear in foreground so that we can scan in shorter periods
        String title = "Memini";
        String text = "scanning...";
        beaconManager.enableForegroundServiceScanning(buildNotification(title, text), 456);
        beaconManager.setEnableScheduledScanJobs(false);
        beaconManager.setBackgroundBetweenScanPeriod(0);
        beaconManager.setBackgroundScanPeriod(1100);
        beaconManager.setForegroundBetweenScanPeriod(0);
        beaconManager.setForegroundScanPeriod(1100);
        beaconManager.bind(this);
        BeaconManager.setDebug(true);

        //backgroundPowerSaver is useful in order to save the device battery
        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    private Notification buildNotification(String title, String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel contentChannel = new NotificationChannel(
                    "content_channel", "Things near you", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(contentChannel);
        }

        return new NotificationCompat.Builder(context, "content_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            //Determine what happens, when resident is detected in certain beacon regions
            @Override
            public void didEnterRegion(Region region) {
                if(region.getUniqueId().equals(doorRegion.getUniqueId())) {
                    //Do arrangements when resident enters apartment
                    Log.i("LOGIDEBUG", "didEnterRegion: Door");
                }
                else if(region.getUniqueId().equals(toiletRegion.getUniqueId())) {
                    //Do arrangements when resident enters wc
                    Log.i("LOGIDEBUG", "didEnterRegion: Wc");
                }
                Toast.makeText(context, "ENTER", Toast.LENGTH_SHORT).show();
            }
            //Determine what happens when resident goes outside of certain beacon regions
            @Override
            public void didExitRegion(Region region) {
                if(region.getUniqueId().equals(doorRegion.getUniqueId())) {
                    //Do arrangements when resident exits apartment
                    Log.i("LOGIDEBUG", "didExitRegion: Door");
                }
                else if(region.getUniqueId().equals(toiletRegion.getUniqueId())) {
                    //Do arrangements when resident exits wc
                    Log.i("LOGIDEBUG", "didExitRegion: Wc");
                }
                Toast.makeText(context, "EXIT", Toast.LENGTH_SHORT).show();
            }

            //Don't need this to our purposes
            @Override
            public void didDetermineStateForRegion(int i, Region region) { }
        });
        try{
            beaconManager.startMonitoringBeaconsInRegion(doorRegion);
            beaconManager.startMonitoringBeaconsInRegion(toiletRegion);
        } catch (RemoteException e) {
            Log.i("LOGIDEBUG", e.getMessage());
        }
        Log.i("LOGIDEBUG", "Service connected");
    }
}
