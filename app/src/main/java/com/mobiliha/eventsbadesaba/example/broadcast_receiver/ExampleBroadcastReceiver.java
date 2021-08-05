package com.mobiliha.eventsbadesaba.example.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ExampleBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String message = null;
        if(Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
            boolean isEnabled = intent.getBooleanExtra("state", false);
            message = "Airplane mode is " + (isEnabled ? "enabled" : "disabled");
        } else if(Intent.ACTION_HEADSET_PLUG.equals(action)) {
            message = "Headset state changed";
        }

        if(message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}