package com.example.androiddevapp.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddevapp.INewAddListener;
import com.example.androiddevapp.IStudentManager;
import com.example.androiddevapp.bean.Student;
import com.example.androiddevapp.databinding.ActivityAidlBinding;

import java.util.List;

public class AidlActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AidlActivity";
    private ActivityAidlBinding binding;
    private IStudentManager iStudentManager;
    private boolean isBind;
    private int index;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBind = true;
            Log.w(TAG, "onServiceConnected: 连接成功");
            iStudentManager = IStudentManager.Stub.asInterface(iBinder);
            try {
                iStudentManager.registerNewListener(iNewAddListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
            Log.w(TAG, "onServiceConnected: 连接异常断开");
        }
    };
    public static final int NEW_STUDENT_ADD = 1;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NEW_STUDENT_ADD:
                    Student student = (Student) msg.obj;
                    Toast.makeText(AidlActivity.this, "欢迎新同学：" + student.name, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private INewAddListener iNewAddListener = new INewAddListener.Stub() {
        @Override
        public void onNewAdd(Student student) throws RemoteException {
            handler.obtainMessage(NEW_STUDENT_ADD, student).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAidlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAddStudent.setOnClickListener(this);
        binding.btnGetStudent.setOnClickListener(this);
        Intent intent = new Intent(this, StudentManagerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        isBind = false;
        if (iStudentManager != null && iStudentManager.asBinder().isBinderAlive()) {
            try {
                iStudentManager.unregisterNewListener(iNewAddListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (binding.btnGetStudent == view) {
            if (isBind && iStudentManager != null) {
                try {
                    List<Student> studentList = iStudentManager.getStudentList();
                    Log.w(TAG, "onServiceConnected: " + studentList.getClass().getCanonicalName());
                    if (studentList != null && studentList.size() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < studentList.size(); i++) {
                            stringBuilder.append(studentList.get(i).name + " : " + studentList.get(i).age).append("\n");
                        }
                        binding.tvStudentList.setText(stringBuilder.toString());
                    } else {
                        Log.w(TAG, "onClick: 获取失败 "+(studentList.size()) );
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.w(TAG, "onClick: 远程服务链接失败 " + (iStudentManager == null));
            }
        } else if (binding.btnAddStudent == view) {
            if (isBind && iStudentManager != null) {
                try {
                    iStudentManager.addStudent(new Student("tom" + (++index), "1" + (++index)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.w(TAG, "onClick: 远程服务链接失败 " + (iStudentManager == null));
            }
        }
    }
}