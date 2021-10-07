package com.example.soham_pc.fridgemate.scheduler;

import android.app.job.JobParameters;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobService extends android.app.job.JobService {
    private static final String TAG = JobService.class.getSimpleName();
    boolean isWorking = false;
    boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started!");
        isWorking = true;
        // We need 'jobParameters' so we can call 'jobFinished'
        //startWorkOnNewThread(jobParameters); // Services do NOT run on a separate thread

        return isWorking;
        //return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
