package com.exemplo.remotephone;

import android.content.Context;
import androidx.work.*;
import java.util.concurrent.TimeUnit;

public class SyncScheduler {
    public static void schedule(Context ctx) {
        Constraints cons = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();
        PeriodicWorkRequest w = new PeriodicWorkRequest.Builder(
            SyncWorker.class, 60, TimeUnit.MINUTES)
            .setConstraints(cons)
            .build();
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
            "sync_data",
            ExistingPeriodicWorkPolicy.KEEP,
            w
        );
    }
}
