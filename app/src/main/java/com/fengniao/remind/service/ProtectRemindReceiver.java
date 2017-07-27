package com.fengniao.remind.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProtectRemindReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        startRemindService(context);
    }

    public void startRemindService(Context context) {
        Intent intent = new Intent(context, RemindService.class);
        context.startService(intent);
    }
}
