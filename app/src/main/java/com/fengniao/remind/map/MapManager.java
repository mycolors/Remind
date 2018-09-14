package com.fengniao.remind.map;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;


import com.fengniao.remind.R;

import java.util.List;

public abstract class MapManager {

    abstract void init();

    //开启定位功能
    public abstract void enableLocation(boolean enable);

    //开启跟随状态
    public   abstract void enableFollow(boolean enable);


    public abstract void showMyLocation(boolean enable);


    public  abstract void onResume();

    public  abstract void onPause();

    public  abstract void onDestroy();


}
