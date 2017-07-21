package com.fengniao.remind.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.data.local.LocalDataSource;
import com.fengniao.remind.map.MapManager;
import com.fengniao.remind.map.MapUtils;

import java.util.List;


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
                if (LocalDataSource.getInstance(RemindService.this).arrivedLocation(mList.get(i))) {
                    mList.remove(mList.get(i));
                }
                shockTime = 0;
                Toast.makeText(getApplicationContext(), "到达目的地", Toast.LENGTH_SHORT).show();
                startShock();
            }

        }
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.onDestroy();
    }

    public class MyBinder extends Binder {

        public void startRemind() {
            mList = null;
        }

    }
}
