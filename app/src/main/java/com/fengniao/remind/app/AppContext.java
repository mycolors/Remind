package com.fengniao.remind.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;


public class AppContext extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }

}
