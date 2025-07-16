package com.exemplo.remotephone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject;
import java.net.URISyntaxException;

public class LocationService extends Service {
    private Socket socket;
    private FusedLocationProviderClient fusedClient;
    private static final String CHANNEL_ID = "loc_service";

    @Override public void onCreate() {
        super.onCreate();
        createChannel();
        startForeground(1, buildNotification());

        try {
            socket = IO.socket("https://remote-phone-backend.onrender.com");
            socket.connect();
        } catch (URISyntaxException e) { e.printStackTrace(); }

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest req = LocationRequest.create()
            .setInterval(5000)
            .setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedClient.requestLocationUpdates(req, new LocationCallback(){
            @Override public void onLocationResult(LocationResult res) {
                for (Location loc : res.getLocations()) {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("deviceId", DeviceManager.getDeviceId(getApplicationContext()));
                        obj.put("latitude", loc.getLatitude());
                        obj.put("longitude", loc.getLongitude());
                        socket.emit("location", obj);
                    } catch (Exception ignored){}
                }
            }
        }, Looper.getMainLooper());
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Localização ativa")
            .setContentText("Enviando em tempo real")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build();
    }

    private void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(
                CHANNEL_ID, "LocService", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(chan);
        }
    }

    @Override public IBinder onBind(Intent i) { return null; }
}
