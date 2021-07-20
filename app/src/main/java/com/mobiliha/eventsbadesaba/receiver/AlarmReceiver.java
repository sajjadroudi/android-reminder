package com.mobiliha.eventsbadesaba.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobiliha.eventsbadesaba.util.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "AlarmReceiver";

    public static final String EXTRA_TASK_TITLE = "title";
    public static final String EXTRA_TASK_ID = "task_id";
    public static final String EXTRA_TASK_DUE_DATE = "due_date";

    private static final int NOT_DEFINED = -1;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Extract extras
        String title = intent.getStringExtra(EXTRA_TASK_TITLE);
        int taskId = intent.getIntExtra(EXTRA_TASK_ID, NOT_DEFINED);
        long dueDate = intent.getLongExtra(EXTRA_TASK_DUE_DATE, NOT_DEFINED);

        if(taskId == NOT_DEFINED || dueDate == NOT_DEFINED) {
            String message = "Task id or due date have not been put to intent";
            throw new IllegalArgumentException(message);
        }

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendNotification(taskId, title, dueDate);

    }

}