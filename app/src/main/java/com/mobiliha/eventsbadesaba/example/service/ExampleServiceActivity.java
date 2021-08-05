package com.mobiliha.eventsbadesaba.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.TextView;

import com.mobiliha.eventsbadesaba.R;

public class ExampleServiceActivity extends AppCompatActivity {

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            randomService = ((RandomGeneratorService.LocalBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            randomService = null;
        }
    };

    private RandomGeneratorService randomService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_service);

        findViewById(R.id.btn_random_int).setOnClickListener(v -> {
            if(randomService != null) {
                ((TextView) v).setText(String.valueOf(randomService.getRandom()));
            }
        });

        findViewById(R.id.btn_play_music).setOnClickListener(v -> {
            Intent intent = new Intent(ExampleServiceActivity.this, MusicPlayerService.class);
            Uri[] uris = {Settings.System.DEFAULT_RINGTONE_URI}; // Uris of some audios
            int randomIndex = (int) (Math.random() * uris.length);
            intent.putExtra(MusicPlayerService.EXTRA_MUSIC_URI, uris[randomIndex]);
            startService(intent);
        });

        findViewById(R.id.btn_show_notification).setOnClickListener(v -> {
            Intent intent = new Intent(ExampleServiceActivity.this, NotifyService.class);
            intent.setAction(NotifyService.ACTION_START_FOREGROUND);
            intent.putExtra(Intent.EXTRA_TITLE, "Notification Title");
            startService(intent);
        });

        findViewById(R.id.btn_hide_notificaiton).setOnClickListener(v -> {
            Intent intent = new Intent(this, NotifyService.class);
            intent.setAction(NotifyService.ACTION_STOP_FOREGROUND);
            startService(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, RandomGeneratorService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(connection);
        randomService = null;

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, NotifyService.class));

        super.onDestroy();
    }
}