package com.mobiliha.eventsbadesaba.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.util.Log;

import com.mobiliha.eventsbadesaba.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ExampleAlarmActivity extends AppCompatActivity {

    private static final int ONE_TIME_REQ_ID = "onetime".hashCode();
    private static final int REPEATING_REQ_ID = "repeating".hashCode();

    public static final String TAG = "ExampleAlarmActivity";

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_alarm);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        findViewById(R.id.btn_repeating).setOnClickListener(v -> {
            setRepeatingAlarm();
        });

        findViewById(R.id.btn_onetime).setOnClickListener(v -> {
            setOneTimeAlarm();
        });

    }

    private void setRepeatingAlarm() {
        Intent intent = new Intent(this, ExampleAlarmReceiver.class);
        intent.putExtra(Intent.EXTRA_TITLE, "repeating");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, REPEATING_REQ_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, +1);
        calendar.set(Calendar.SECOND, 0);

        // Using 'inexact' methods improve battery usage
        // By using this method we have to use predefined constants as interval
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent
        );
    }

    private void setOneTimeAlarm() {
        Intent intent = new Intent(this, ExampleAlarmReceiver.class);
        intent.putExtra(Intent.EXTRA_TITLE, "one-time");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, ONE_TIME_REQ_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        long trigger = SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    trigger,
                    pendingIntent
            );
        } else {
            alarmManager.set(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    trigger,
                    pendingIntent
            );
        }
    }

}