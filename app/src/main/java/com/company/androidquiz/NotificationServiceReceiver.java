package com.company.androidquiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class NotificationServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), NotificationService.RESTART_SERVICE_ACTION)) {
            context.startService(new Intent(context, NotificationService.class));
        }
    }
}
