package com.example.androiddevapp.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BinderPoolService
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/3/2 7:39 下午
 */
public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";
    private Binder binderPool = new BinderPool.BinderPoolImpl();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: ");
    }
}
