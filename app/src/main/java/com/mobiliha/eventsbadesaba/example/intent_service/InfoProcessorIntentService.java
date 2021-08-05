package com.mobiliha.eventsbadesaba.example.intent_service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;

import com.mobiliha.eventsbadesaba.BuildConfig;

/**
 * An example of intent service processing some information.
 */
public class InfoProcessorIntentService extends IntentService {

    public static final int RESULT_CODE = 0;
    public static final String EXTRA_INFO = BuildConfig.APPLICATION_ID + ".info";
    public static final String EXTRA_RESULT = BuildConfig.APPLICATION_ID + ".result";
    public static final String EXTRA_PENDING_RESULT = BuildConfig.APPLICATION_ID + ".pending_result";

    public InfoProcessorIntentService() {
        super("InfoProcessorIntentService");
    }

    // Runs on a background thread.
    @Override
    protected void onHandleIntent(Intent intent) {
        String info = intent.getStringExtra(EXTRA_INFO);

        int result = process(info);

        sendResultBack(intent, result);
    }

    private int process(String info) {
        // Simulating an expensive operation
        SystemClock.sleep(100);

        return info == null ? 0 : info.length();
    }

    private void sendResultBack(Intent intent, int result) {
        try {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_RESULT, result);

            PendingIntent reply = intent.getParcelableExtra(EXTRA_PENDING_RESULT);
            reply.send(this, RESULT_CODE, resultIntent);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

}