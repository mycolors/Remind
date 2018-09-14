package com.fengniao.remind.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.fengniao.remind.ProjectServiceInf;
import com.fengniao.remind.util.PzLogUtil;

public class ProtectService extends Service {
    private static final String TAG = ProtectService.class.getSimpleName();

    private ProtectBinder.Stub mBinder = new ProtectBinder();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PzLogUtil.i(TAG, "remindService 连接成功");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            PzLogUtil.i(TAG, "remindService被杀死 开始重新连接");
            Intent intent = new Intent(ProtectService.this, RemindService.class);
            startService(intent);
            bindService(intent, connection, Context.BIND_IMPORTANT);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, RemindService.class), connection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    static class ProtectBinder extends ProjectServiceInf.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return null;
        }
    }
}
