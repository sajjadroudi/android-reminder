package com.mobiliha.eventsbadesaba.example.job_scheduler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;

@SuppressLint("SpecifyJobSchedulerIdRange")
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SyncJobService extends JobService {

    private class SyncAsyncTask extends AsyncTask<JobParameters, Void, Void> {

        @Override
        protected Void doInBackground(JobParameters... jobParameters) {
            sync();

            // Assume the operation was successful
            jobFinished(jobParameters[0], false);

            return null;
        }

        private void sync() {
            // Simulating expensive operation
            SystemClock.sleep(1000);
        }

    }

    private SyncAsyncTask task;

    @Override
    public boolean onStartJob(JobParameters params) {
        task = new SyncAsyncTask();
        task.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        task.cancel(true);
        return false;
    }
}
