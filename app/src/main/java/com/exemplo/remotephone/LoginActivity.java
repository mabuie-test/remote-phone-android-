package com.exemplo.remotephone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_PERMS = 100;
    EditText etUser, etPass;
    Button btnLogin;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_login);

        etUser   = findViewById(R.id.etUsername);
        etPass   = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        ActivityCompat.requestPermissions(this, new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_CALL_LOG
        }, REQUEST_PERMS);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("username", etUser.getText().toString());
            json.put("password", etPass.getText().toString());
        } catch (Exception ignored) {}

        RequestBody body = RequestBody.create(
            json.toString(),
            MediaType.get("application/json")
        );
        Request req = new Request.Builder()
            .url("https://remote-phone-backend.onrender.com/auth/login")
            .post(body)
            .build();

        client.newCall(req).enqueue(new Callback() {
            @Override public void onFailure(Call c, IOException e) { }
            @Override public void onResponse(Call c, Response r) throws IOException {
                String resp = r.body().string();
                try {
                    String token = new JSONObject(resp).getString("token");
                    AuthManager.saveToken(LoginActivity.this, token);
                    registerDevice();
                } catch (Exception ignored) {}
            }
        });
    }

    private void registerDevice() {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try { json.put("name", "Device-"+System.currentTimeMillis()); }
        catch (Exception ignored) {}

        RequestBody body = RequestBody.create(
            json.toString(),
            MediaType.get("application/json")
        );
        Request req = new Request.Builder()
            .url("https://remote-phone-backend.onrender.com/devices")
            .addHeader("Authorization", "Bearer " + AuthManager.getToken(this))
            .post(body)
            .build();

        client.newCall(req).enqueue(new Callback() {
            @Override public void onFailure(Call c, IOException e) { }
            @Override public void onResponse(Call c, Response r) throws IOException {
                String resp = r.body().string();
                String deviceId = new JSONObject(resp).getString("_id");
                DeviceManager.saveDeviceId(LoginActivity.this, deviceId);
                runOnUiThread(() -> {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                });
            }
        });
    }
}

