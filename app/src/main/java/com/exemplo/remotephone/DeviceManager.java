package com.exemplo.remotephone;

import android.content.Context;
import android.content.SharedPreferences;

public class DeviceManager {
    private static final String PREF="device_prefs", KEY="deviceId";
    public static void saveDeviceId(Context c, String id){
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        p.edit().putString(KEY, id).apply();
    }
    public static String getDeviceId(Context c){
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY, null);
    }
}
