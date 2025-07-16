package com.exemplo.remotephone;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREF="auth_prefs", KEY="token";
    public static void saveToken(Context c, String t){
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        p.edit().putString(KEY, t).apply();
    }
    public static String getToken(Context c){
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY, null);
    }
}
