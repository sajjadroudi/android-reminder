package com.mobiliha.eventsbadesaba.example.alarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ExampleAlarmReceiver extends BroadcastReceiver {

    public static final String TAG = "ExampleAlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(Intent.EXTRA_TITLE);
        Log.i(TAG, "onReceive: " + message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}