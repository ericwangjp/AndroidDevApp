package com.example.androiddevapp.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddevapp.consts.AppConsts;
import com.example.androiddevapp.databinding.ActivityMessengerBinding;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity-【客户端】";

    private Messenger messenger;
    private boolean isBind;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBind = true;
            messenger = new Messenger(iBinder);
//            Message message = Message.obtain(null, AppConsts.MSG_FROM_CLIENT);
//            Bundle bundle = new Bundle();
//            bundle.putString("msg", binding.edtClientSend.getText().toString());
//            message.setData(bundle);
//            message.replyTo = replyMessenger;
//            try {
//                Log.w(TAG, "onServiceConnected: 客户端开始发送消息");
//                messenger.send(message);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }
    };
    public static ActivityMessengerBinding binding;

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case AppConsts.MSG_FROM_SERVICE:
                    String reply = msg.getData().getString("reply");
                    Log.w(TAG, "handleMessage: 收到服务端消息：" + reply);
                    binding.edtClientReceive.setText(reply);
                    break;
                case AppConsts.MSG_SERVICE_REPLY_DISPLAY:
                    String serviceReceive = msg.getData().getString("reply");
                    Log.w(TAG, "handleMessage: 展示服务端接收到的消息：" + serviceReceive);
                    binding.edtServiceReceive.setText(serviceReceive);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger replyMessenger = new Messenger(new MessengerHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
    }

    private void initData() {
        binding.edtClientSend.setText("hello, client send msg");
        binding.edtServiceSend.setText(MessengerService.replyMsg);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBind) {
                    Message message = Message.obtain(null, AppConsts.MSG_FROM_CLIENT);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", binding.edtClientSend.getText().toString());
                    message.setData(bundle);
                    message.replyTo = replyMessenger;
                    try {
                        Log.w(TAG, "onServiceConnected: 客户端开始发送消息");
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MessengerActivity.this, "连接已经异常断开，请重新连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBind) {
                    Message message = Message.obtain(null, AppConsts.MSG_SERVICE_START_REPLY);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", binding.edtServiceSend.getText().toString());
                    message.setData(bundle);
                    message.replyTo = replyMessenger;
                    try {
                        Log.w(TAG, "onServiceConnected: 服务端开始回复客户端消息");
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MessengerActivity.this, "连接已经异常断开，请重新连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.edtServiceSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isBind) {
                    Message message = Message.obtain(null, AppConsts.MSG_SERVICE_REPLY_UPDATE);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", binding.edtServiceSend.getText().toString());
                    message.setData(bundle);
//                    此时不需要回复
//                    message.replyTo = replyMessenger;
                    try {
                        Log.w(TAG, "onServiceConnected: 客户端开始更新服务端回复消息内容");
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MessengerActivity.this, "连接已经异常断开，请重新连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        isBind = false;
        unbindService(serviceConnection);
        super.onDestroy();
    }
}