package com.fengniao.remind.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.fengniao.remind.R;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.data.local.LocalDataSource;
import com.fengniao.remind.map.MapManager;
import com.fengniao.remind.map.MapUtils;
import com.fengniao.remind.ui.activity.MainActivity;

import java.util.List;

import static com.fengniao.remind.app.Constant.REMIND_FINISHED;


public class RemindService extends Service {


    private MapManager mManager;

    private MyBinder mBinder = new MyBinder();

    private Handler mHandler = new Handler();

    private List<Location> mList;

    private Vibrator mVibrator;

    private long shockTime;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mManager.getMyLocation() != null) {
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
        LatLng mLatlng = new LatLng(mManager.getMyLocation().getLatitude(),
                mManager.getMyLocation().getLongitude());
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
        mManager = new MapManager(this);
        mManager.enableLocation(true);
        mHandler.postDelayed(runnable, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intent1 = new Intent(this, MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
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
        mManager.onDestroy();
        //停止前台服务
        stopForeground(true);
        //拉起服务
        Intent intent = new Intent(this, RemindService.class);
        startService(intent);
    }

    public class MyBinder extends Binder {

        public void startRemind() {
            mList = null;
        }

    }
}
