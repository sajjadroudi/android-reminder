package com.mobiliha.eventsbadesaba.example.workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobiliha.eventsbadesaba.R;

import java.util.concurrent.TimeUnit;

public class ExampleWorkActivity extends AppCompatActivity {

    public static final String TAG = "ExampleWorkActivity";

    public static final String NAME_KEY = "name";
    public static final String AGE_KEY = "age";

    private static final String SYNC_WORK_UNIQUE_NAME = "sync_work";

    private WorkManager workManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_work);

        workManager = WorkManager.getInstance(this);

        findViewById(R.id.btn_onetime).setOnClickListener(v -> {
            setupUpload();
        });

        findViewById(R.id.btn_periodic).setOnClickListener(v -> {
            setupSync();
        });


    }

    private void setupUpload() {
        Data userData = new Data.Builder()
                .putString(NAME_KEY, "Bill Gates")
                .putInt(AGE_KEY, 60)
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build();

        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(constraints)
                .setInputData(userData)
                .addTag("upload")
                .build();

        workManager.enqueue(uploadWorkRequest);

        workManager.getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(ExampleWorkActivity.this, info -> {
                    if(info == null) return;
                    Data progress = info.getProgress();
                    int percent = progress.getInt(UploadWorker.PROGRESS, 0);

                    // Show progress percent somehow
                    Toast.makeText(this, String.format("Progress: %d/100", percent), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupSync() {
        Constraints.Builder constraintsBuilder = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraintsBuilder.setRequiresDeviceIdle(true);
        }

        PeriodicWorkRequest syncWorkRequest = new PeriodicWorkRequest.Builder(SyncWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(1, TimeUnit.MINUTES) // Notice: only the first run would be delayed.
                .setConstraints(constraintsBuilder.build())
                .setBackoffCriteria(BackoffPolicy.LINEAR, WorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .addTag("sync")
                .build();

        // We can also chain one-shot works by using similar methods
        workManager.enqueueUniquePeriodicWork(
                SYNC_WORK_UNIQUE_NAME, ExistingPeriodicWorkPolicy.KEEP, syncWorkRequest
        );

        workManager.getWorkInfoByIdLiveData(syncWorkRequest.getId())
                .observe(this, info -> {
                    if(info.getState() == WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(this, "The work completed successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cancelSyncWork() {
        workManager.cancelUniqueWork(SYNC_WORK_UNIQUE_NAME);
    }

    private void cancelAllUploadWorks() {
        workManager.cancelAllWorkByTag("upload");
    }

}