package com.mobiliha.eventsbadesaba.example.workmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadWorker extends Worker {

    public static final String PROGRESS = "PROGRESS";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if(!isInputDataValid())
            return Result.failure();

        uploadData();

        Data progress = new Data.Builder()
                .putInt(PROGRESS, 100)
                .build();
        setProgressAsync(progress);

        return Result.success();
    }

    private void uploadData() {
        try {
            // Simulating uploading process
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isInputDataValid() {
        Data data = getInputData();
        return data.hasKeyWithValueOfType(ExampleWorkActivity.NAME_KEY, String.class)
                && data.hasKeyWithValueOfType(ExampleWorkActivity.AGE_KEY, Integer.class);
    }

}
