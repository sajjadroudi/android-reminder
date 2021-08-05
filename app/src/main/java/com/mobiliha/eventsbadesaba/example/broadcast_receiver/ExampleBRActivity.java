package com.mobiliha.eventsbadesaba.example.broadcast_receiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.mobiliha.eventsbadesaba.R;

public class ExampleBRActivity extends AppCompatActivity {

    private final ExampleBroadcastReceiver receiver = new ExampleBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_bractivity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);

        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);

        super.onStop();
    }
}