package com.example.androiddevapp.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: TCPService
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/3/2 10:30 上午
 */
public class TCPService extends Service {
    private static final String TAG = "TCPService";
    private boolean mIsServiceDestroyed = false;
    private String[] defaultMessages = new String[]{
            "你好，哈哈",
            "呵呵哒，臭弟弟",
            "小姐姐，你好美",
            "小哥哥，你好帅",
            "今天天气不错",
            "多人同时连接，在线聊天"
    };

    @Override
    public void onCreate() {
        new Thread(new TcpWorker()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed = true;
        super.onDestroy();
    }

    private class TcpWorker implements Runnable {

        ServerSocket serverSocket = null;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                Log.w(TAG, "socket创建失败: 8688端口 ");
                e.printStackTrace();
            }
            while (!mIsServiceDestroyed) {
                try {
                    Socket socket = serverSocket.accept();
                    Log.w(TAG, "run: socket 连接");
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                responseToClient(socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseToClient(Socket socket) throws IOException {
//        接收客户端消息
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        向客户端发送消息
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        printWriter.println("欢迎来到聊天室！");
        while (!mIsServiceDestroyed) {
            String clientMsg = bufferedReader.readLine();
            Log.w(TAG, "responseToClient: 收到客户端消息：" + clientMsg);
            if (clientMsg == null) {
                break;
            }
            int msgPos = new Random().nextInt(defaultMessages.length);
            String replyMsg = defaultMessages[msgPos];
            printWriter.println(replyMsg);
            Log.w(TAG, "responseToClient: 发送服务端回复消息" + replyMsg);
        }
        Log.w(TAG, "responseToClient: 客户端已退出");
        bufferedReader.close();
        printWriter.close();
        socket.close();
    }
}
