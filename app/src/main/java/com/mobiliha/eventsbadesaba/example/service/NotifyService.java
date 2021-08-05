package com.mobiliha.eventsbadesaba.example.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.mobiliha.eventsbadesaba.BuildConfig;
import com.mobiliha.eventsbadesaba.R;

/**
 * An example of a foreground service.
 */
public class NotifyService extends Service {

    public static final String ACTION_START_FOREGROUND = BuildConfig.APPLICATION_ID + ".ACTION_START_FOREGROUND";
    public static final String ACTION_STOP_FOREGROUND = BuildConfig.APPLICATION_ID + ".ACTION_STOP_FOREGROUND";

    private static final String CHANNEL_ID = "channel";
    private NotificationManager notifManager;

    @Override
    public void onCreate() {


    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()) {
            case ACTION_START_FOREGROUND:
                String title = intent.getStringExtra(Intent.EXTRA_TITLE);
                startForeground(1, buildNotification(title));
                break;
            case ACTION_STOP_FOREGROUND:
                stopForeground(true);
                break;
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification buildNotification(String title) {
        notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent notifIntent = new Intent(this, ExampleServiceActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifIntent, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Main", NotificationManager.IMPORTANCE_DEFAULT);
            notifManager.createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.example_ic_alarm)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
    }

}