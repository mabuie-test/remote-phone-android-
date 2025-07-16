package com.exemplo.remotephone;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_ENABLE_ADMIN = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1002;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_main);

        ComponentName comp = new ComponentName(this, LoginActivity.class);
        getPackageManager().setComponentEnabledSetting(
            comp,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        );

        startService(new Intent(this, LocationService.class));
        findViewById(R.id.btnEnableAdmin).setOnClickListener(v -> enableAdmin());
        findViewById(R.id.btnCapturePhoto).setOnClickListener(v -> dispatchTakePictureIntent());
        SyncScheduler.schedule(this);
    }

    private void enableAdmin() {
        DevicePolicyManager dpm = (DevicePolicyManager)
            getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName admin = new ComponentName(this, MyDeviceAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            "Permita bloqueio remoto");
        startActivityForResult(intent, REQUEST_ENABLE_ADMIN);
    }

    private void dispatchTakePictureIntent() {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == REQUEST_IMAGE_CAPTURE && res == RESULT_OK) {
            UploadHelper.handleCapturedPhoto(this, data);
        }
    }
}
