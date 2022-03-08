package com.example.androiddevapp.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BinderPool
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/3/2 8:12 下午
 */
public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY_CENTER = 1;

    private Context mContext;
    private IBinderPool mBinderPool;
    private static volatile BinderPool instance;
    private CountDownLatch mBinderCountDownLatch;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                mBinderPool.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBinderCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    private BinderPool(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    private void connectBinderPoolService() {
        mBinderCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        try {
            mBinderCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        if (mBinderPool!= null){
            try {
                binder = mBinderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_SECURITY_CENTER:
                    binder = new SecurityCenterImpl();
                    break;
                case BINDER_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }
}
