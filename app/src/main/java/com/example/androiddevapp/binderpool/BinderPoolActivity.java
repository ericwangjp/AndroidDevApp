package com.example.androiddevapp.binderpool;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddevapp.databinding.ActivityBinderPoolBinding;

public class BinderPoolActivity extends AppCompatActivity {

    private ActivityBinderPoolBinding binding;
    private static final String TAG = "BinderPoolActivity";
    private HandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBinderPoolBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
    }

    private void initData() {
        handlerThread = new HandlerThread("binderPool");
        handlerThread.start();
        Handler workHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 100:
                        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
                        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
                        ISecurityCenter iSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
                        String info = "hello，小姐姐";
                        Log.w(TAG, "原始串: " + info);
                        try {
                            String encrypt = iSecurityCenter.encrypt(info);
                            Log.w(TAG, "加密串: " + encrypt);
                            String decrypt = iSecurityCenter.decrypt(encrypt);
                            Log.w(TAG, "解密串: " + decrypt);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
                        ICompute iCompute = ComputeImpl.asInterface(computeBinder);
                        try {
                            int add = iCompute.add(5, 3);
                            Log.w(TAG, "计算结果: " + add);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        binding.btnStart.setOnClickListener(view -> workHandler.sendEmptyMessage(100));
    }

    @Override
    protected void onDestroy() {
        handlerThread.quit();
        super.onDestroy();
    }

}