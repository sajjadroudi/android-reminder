package com.mobiliha.eventsbadesaba.example.job_scheduler;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;

@SuppressLint("SpecifyJobSchedulerIdRange")
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UploadJobService extends JobService {

    private class UploadAsyncTask extends AsyncTask<JobParameters, Void, Void> {

        @Override
        protected Void doInBackground(JobParameters... jobParameters) {
            PersistableBundle bundle = jobParameters[0].getExtras();
            upload(bundle);

            jobFinished(jobParameters[0], false);

            return null;
        }

        private void upload(PersistableBundle bundle) {
            // Simulating expensive operation
            SystemClock.sleep(1000);
        }

    }

    private UploadAsyncTask task;

    @Override
    public boolean onStartJob(JobParameters params) {
        task = new UploadAsyncTask();
        task.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        task.cancel(true);
        return false;
    }

}
