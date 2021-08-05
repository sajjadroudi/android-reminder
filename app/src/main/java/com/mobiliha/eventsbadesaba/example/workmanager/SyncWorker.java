package com.mobiliha.eventsbadesaba.example.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        boolean isSuccessful = sync();

        return isSuccessful ? Result.success() : Result.retry();
    }

    private boolean sync() {
        try {
            // Simulating synchronization process
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // The operation was successful
        return true;
    }

    @Override
    public void onStopped() {
        // Release resources
    }
}
