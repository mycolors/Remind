package com.fengniao.remind.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.fengniao.remind.R;
import com.fengniao.remind.RemindServiceInf;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.data.local.LocalDataSource;
import com.fengniao.remind.map.GDMapManger;
import com.fengniao.remind.map.MapManager;
import com.fengniao.remind.map.MapUtils;
import com.fengniao.remind.ui.activity.MainActivity;
import com.fengniao.remind.util.PzLogUtil;

import java.util.List;

import static com.fengniao.remind.app.Constant.REMIND_FINISHED;


public class RemindService extends Service {

    private static final String TAG = RemindService.class.getSimpleName();

    private RemindBinder.Stub mBinder = new RemindBinder();

    private Handler mHandler = new Handler();

    private List<Location> mList;

    private Vibrator mVibrator;

    private long shockTime;

    private AMapLocation mLocation;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PzLogUtil.i(TAG, "ProjectService 连接成功");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            PzLogUtil.i(TAG, "projectService 被杀死 开始重新连接");
            Intent intent = new Intent(RemindService.this, ProtectService.class);
            startService(intent);
            bindService(intent, connection, Context.BIND_IMPORTANT);
        }
    };


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mLocation != null) {
                if (mList == null) {
                    mList = LocalDataSource.getInstance(RemindService.this).getActivateLocation();
                } else {
                    shockTime++;
                    handleLocationList();
                }
            }
            if (shockTime == 5)
                stopShock();
            mHandler.postDelayed(this, 1000);
        }
    };


    public void handleLocationList() {
        if (mList.isEmpty()) return;
        LatLng mLatlng = new LatLng(mLocation.getLatitude(),
                mLocation.getLongitude());
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i) == null)
                continue;
            double distance = MapUtils.calculateDistance(mLatlng, new LatLng((mList.get(i).getLatitude()),
                    mList.get(i).getLongitude()));
            if (distance < 100) {
                //完成提醒时发出广播，通知LocationListFragment刷新location列表
                sendRemindFinishedBroadcast(mList.get(i).getId());
                if (LocalDataSource.getInstance(RemindService.this).arrivedLocation(mList.get(i))) {
                    mList.remove(mList.get(i));
                }
                shockTime = 0;
                Toast.makeText(getApplicationContext(), "到达目的地", Toast.LENGTH_SHORT).show();
                startShock();
            }
        }
    }

    public void sendRemindFinishedBroadcast(long id) {
        Intent intent = new Intent();
        intent.setAction(REMIND_FINISHED);
        intent.putExtra("id", id);
        sendBroadcast(intent);
    }


    //开始震动
    public void startShock() {
        if (mVibrator == null)
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        mVibrator.vibrate(pattern, 2);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }

    public void stopShock() {
        if (mVibrator != null)
            mVibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GDMapManger mManager = new GDMapManger(this);
        mManager.enableLocation(true);
        mHandler.postDelayed(runnable, 1000);

        mManager.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                PzLogUtil.i(TAG, "my location is " + aMapLocation.toStr());
                mLocation = aMapLocation;
            }
        });

        Intent intent = new Intent(this, ProtectService.class);
        bindService(intent, connection, Context.BIND_IMPORTANT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent(this, MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "remind");
        //设置小图标
        Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle(getString(R.string.app_name))
                .setContentText("running")
                .setWhen(System.currentTimeMillis())
                //设置penddingIntent
                .setContentIntent(PendingIntent.getActivity(this, 0, intent1, 0))
                .build();
        //设为前台service
        startForeground(100, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mManager.onDestroy();
        //停止前台服务
        stopForeground(true);
        //拉起服务
        Intent intent = new Intent(this, RemindService.class);
        startService(intent);
    }

    public class RemindBinder extends RemindServiceInf.Stub {

        @Override
        public void startRemind(){
            mList = null;
        }
    }
}
