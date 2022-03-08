package com.example.androiddevapp.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.androiddevapp.INewAddListener;
import com.example.androiddevapp.IStudentManager;
import com.example.androiddevapp.bean.Student;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: StudentManagerService
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/2/26 5:54 下午
 */
public class StudentManagerService extends Service {
    private static final String TAG = "StudentManagerService";
    private AtomicBoolean mServiceDestroyed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Student> mStudentList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<INewAddListener> listeners = new RemoteCallbackList<>();
    private Binder binder = new IStudentManager.Stub() {
        @Override
        public List<Student> getStudentList() throws RemoteException {
//            模拟UI 线程调用耗时，导致ANR
//            SystemClock.sleep(10000);
            return mStudentList;
        }

        @Override
        public void addStudent(Student student) throws RemoteException {
            mStudentList.add(student);
        }

        @Override
        public void registerNewListener(INewAddListener listener) throws RemoteException {
            listeners.register(listener);
            Log.w(TAG, "registerNewListener: size：" + listeners.getRegisteredCallbackCount());
        }

        @Override
        public void unregisterNewListener(INewAddListener listener) throws RemoteException {
            listeners.unregister(listener);
            Log.w(TAG, "unregisterNewListener: size：" + listeners.getRegisteredCallbackCount());
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int permission = checkCallingOrSelfPermission("com.example.androiddevapp.permission.ACCESS_STUDENT_SERVICE");
            if (permission == PackageManager.PERMISSION_DENIED){
                return false;
            }
            String packageName = null;
            String[] packagesForUid = getPackageManager().getPackagesForUid(getCallingUid());
            if (packagesForUid!=null&&packagesForUid.length>0){
                packageName = packagesForUid[0];
            }
            if (!packageName.startsWith("com.example")){
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mStudentList.add(new Student("jack", "18"));
        mStudentList.add(new Student("lucy", "16"));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        mServiceDestroyed.set(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int permission = checkCallingOrSelfPermission("com.example.androiddevapp.permission.ACCESS_STUDENT_SERVICE");
        if (permission == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return binder;
    }


    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while (!mServiceDestroyed.get()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int size = mStudentList.size();
                Student student = new Student("bob" + ++size, "1" + ++size);
                try {
                    onNewStudentAdd(student);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewStudentAdd(Student student) throws RemoteException {
        mStudentList.add(student);
        Log.w(TAG, "onNewStudentAdd: 欢迎新同学");
        final int i1 = listeners.beginBroadcast();
        for (int i = 0; i < i1; i++) {
            INewAddListener iNewAddListener = listeners.getBroadcastItem(i);
            if (iNewAddListener != null) {
                iNewAddListener.onNewAdd(student);
            }
        }
        listeners.finishBroadcast();
    }
}
