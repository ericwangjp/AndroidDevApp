package com.example.androiddevapp.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddevapp.databinding.ActivitySocketBinding;
import com.example.androiddevapp.utils.AppUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketActivity extends AppCompatActivity {

    private ActivitySocketBinding binding;
    private static final String TAG = "SocketActivity";
    public static final int MSG_RECEIVE_NEW_MSG = 1;
    public static final int MSG_SOCKET_CONNECTED = 2;
    public static final int MSG_SOCKET_SEND = 3;
    private Socket mClientSocket = null;
    private PrintWriter printWriter;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SOCKET_CONNECTED:
                    Toast.makeText(SocketActivity.this, "服务已连接", Toast.LENGTH_SHORT).show();
                    binding.btnSend.setEnabled(true);
                    break;
                case MSG_RECEIVE_NEW_MSG:
                    binding.tvMsgList.setText(binding.tvMsgList.getText().toString() + msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    private HandlerThread handlerThread = new HandlerThread("tcpThread");
    private Handler workHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
    }

    private void initData() {
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_SOCKET_SEND:
                        if (printWriter != null) {
                            printWriter.println(msg.obj);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        binding.btnSend.setOnClickListener(view -> {
//            发送消息
            final String sendMsg = binding.edtMsg.getText().toString();
            if (!TextUtils.isEmpty(sendMsg) && printWriter != null) {
                workHandler.obtainMessage(MSG_SOCKET_SEND, sendMsg).sendToTarget();
                binding.edtMsg.setText("");
                String time = AppUtils.formatDateTime(System.currentTimeMillis());
                final String showedMsg = "【client】 " + time + ": " + sendMsg + "\n";
                binding.tvMsgList.setText(binding.tvMsgList.getText().toString() + showedMsg);
            }
        });
        Intent intent = new Intent(this, TCPService.class);
        startService(intent);
        new Thread() {
            @Override
            public void run() {
                super.run();
                connectTcpServer();
            }
        }.start();
    }

    private void connectTcpServer() {
        while (mClientSocket == null) {
            try {
                mClientSocket = new Socket("localhost", 8688);
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MSG_SOCKET_CONNECTED);
            } catch (IOException e) {
                e.printStackTrace();
                SystemClock.sleep(1000);
                Log.w(TAG, "connectTcpServer: 连接失败，尝试重连");
            }
        }

//        接收服务端消息
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(mClientSocket.getInputStream())));
            while (!this.isFinishing()) {
                String msg = bufferedReader.readLine();
                Log.w(TAG, "收到服务端消息: " + msg);
                if (!TextUtils.isEmpty(msg)) {
                    String time = AppUtils.formatDateTime(System.currentTimeMillis());
                    final String showedMsg = "【server】 " + time + ": " + msg + "\n";
                    mHandler.obtainMessage(MSG_RECEIVE_NEW_MSG, showedMsg).sendToTarget();
                }
            }
            Log.w(TAG, "connectTcpServer: 结束聊天");
            printWriter.close();
            bufferedReader.close();
            mClientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (handlerThread != null) {
            handlerThread.quit();
        }
        super.onDestroy();
    }
}