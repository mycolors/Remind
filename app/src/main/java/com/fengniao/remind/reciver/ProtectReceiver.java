package com.fengniao.remind.reciver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fengniao.remind.service.RemindService;

//通过reciver拉起后台service
public class ProtectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        startRemindService(context);
    }

    public void startRemindService(Context context) {
        Intent intent = new Intent(context, RemindService.class);
        context.startService(intent);
    }
}
