package com.mobiliha.eventsbadesaba.example.job_scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mobiliha.eventsbadesaba.BuildConfig;

import com.mobiliha.eventsbadesaba.R;

import java.util.concurrent.TimeUnit;

public class ExampleJobActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = BuildConfig.APPLICATION_ID + ".name";
    public static final String EXTRA_AGE = BuildConfig.APPLICATION_ID + ".age";

    private static final int ONE_TIME_JOB_ID = "onetime".hashCode();
    private static final int PERIODIC_JOB_ID = "periodic".hashCode();

    private JobScheduler jobScheduler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_job);

        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        findViewById(R.id.btn_onetime).setOnClickListener(v -> {
            setupUpload();
        });

        findViewById(R.id.btn_periodic).setOnClickListener(v -> {
            setupSync();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupUpload() {
        PersistableBundle userData = new PersistableBundle();
        userData.putString(EXTRA_NAME, "Bill Gates");
        userData.putInt(EXTRA_AGE, 60);

        ComponentName componentName = new ComponentName(this, UploadJobService.class);
        JobInfo info = new JobInfo.Builder(ONE_TIME_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setExtras(userData)
                .build();

        int result = jobScheduler.schedule(info);
        boolean wasSuccessful = result != JobScheduler.RESULT_FAILURE;
        Toast.makeText(this, wasSuccessful ? "Succeeded" : "Failed", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupSync() {
        ComponentName componentName = new ComponentName(this, SyncJobService.class);
        JobInfo info = new JobInfo.Builder(PERIODIC_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresDeviceIdle(true)
                .setPersisted(true)
                .setPeriodic(TimeUnit.DAYS.toMillis(1))
                .setBackoffCriteria(TimeUnit.SECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR)
                .setMinimumLatency(TimeUnit.MINUTES.toMillis(1))
                .build();


        jobScheduler.schedule(info);
    }

}