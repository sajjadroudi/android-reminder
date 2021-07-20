package com.mobiliha.eventsbadesaba.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.mobiliha.eventsbadesaba.receiver.AlarmReceiver;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;

import java.util.Calendar;

public class AlarmHelper {

    private final Context context;
    private final AlarmManager alarmManager;

    public AlarmHelper(@NonNull Context context) {
        this.context = context.getApplicationContext();

        alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(@NonNull Task task) {

        if(isPast(task.getDueDate()))
            return;

        long dueDate = task.getDueDate().getTimeInMillis();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.EXTRA_TASK_ID, task.getTaskId());
        intent.putExtra(AlarmReceiver.EXTRA_TASK_TITLE, task.getTitle());
        intent.putExtra(AlarmReceiver.EXTRA_TASK_DUE_DATE, dueDate);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, task.getTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    dueDate,
                    pendingIntent
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    dueDate,
                    pendingIntent
            );
        } else {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    dueDate,
                    pendingIntent
            );
        }
    }

    public void cancelAlarm(@NonNull Task task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, task.getTaskId(), intent, PendingIntent.FLAG_NO_CREATE
        );
        if(pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private boolean isPast(Calendar calendar) {
        return calendar.getTimeInMillis() < System.currentTimeMillis();
    }

}
