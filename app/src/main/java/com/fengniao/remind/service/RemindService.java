package com.fengniao.remind.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mManager.getMyLocation() != null) {
                if (mList == null) {
                    mList = LocalDataSource.getInstance(RemindService.this).getActivateLocation();
                } else {
                    if (!mList.isEmpty() && mManager.getMyLocation() != null) {
                        if (mList.get(0).getActivate()) {
                            LatLng mLatlng = new LatLng(mManager.getMyLocation().getLatitude(),
                                    mManager.getMyLocation().getLongitude());
                            double distance = MapUtils.calculateDistance(mLatlng, new LatLng((mList.get(0).getLatitude()),
                                    mList.get(0).getLongitude()));
                            if (distance < 100) {
                                LocalDataSource.getInstance(RemindService.this).arrivedLocation(mList.get(0));
                                Log.i("test", "到达目的地");
                            }
                        }
                    }
                }
            }
            mHandler.postDelayed(this, 1000);
        }
    };

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
