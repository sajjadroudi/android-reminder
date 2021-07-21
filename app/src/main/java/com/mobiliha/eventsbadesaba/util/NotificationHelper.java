package com.mobiliha.eventsbadesaba.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ui.details.TaskDetailsFragmentArgs;

import java.util.Calendar;

public class NotificationHelper {

    public static final String PRIMARY_CHANNEL_ID = "primary_channel_id";
    public static final String PRIMARY_CHANNEL_NAME = "Primary Channel";

    private final Context context;
    private final NotificationManager manager;

    public NotificationHelper(Context context) {
        this.context = context.getApplicationContext();

        manager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void createPrimaryChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    PRIMARY_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            String description = context.getString(R.string.primary_channel_description);
            channel.setDescription(description);
            manager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(int taskId, String title, long dueDate) {
        Notification notification = buildNotification(taskId, title, dueDate);
        manager.notify(taskId, notification);
    }

    private Notification buildNotification(int taskId, String title, long dueDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dueDate);

        String time = TimeUtils.extractTime(calendar);
        String contentText = context.getString(R.string.alarm_at, time);

        // TODO: Literal string shouldn't be used as key
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", taskId);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.taskDetailsFragment)
                .setArguments(bundle)
                .createPendingIntent();

        return new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build();
    }

}
