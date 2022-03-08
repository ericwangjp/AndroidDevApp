package com.example.androiddevapp.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androiddevapp.consts.AppConsts;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: MessengerService
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/2/24 8:21 下午
 */
public class MessengerService extends Service {
    private static final String TAG = "MessengerService-【服务端】";
    public static String replyMsg = "【自动回复】消息已收到，正在处理";

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case AppConsts.MSG_FROM_CLIENT:
                    String clientMsg = msg.getData().getString("msg");
                    Log.w(TAG, "收到客户端消息：" + clientMsg);
                    Messenger replyMessenger = msg.replyTo;
                    Message replyMessage = Message.obtain(null, AppConsts.MSG_SERVICE_REPLY_DISPLAY);
                    Bundle replyBundle = new Bundle();
                    Log.w(TAG, "服务端界面展示接收到的消息");
                    replyBundle.putString("reply", clientMsg);
                    replyMessage.setData(replyBundle);
                    try {
                        replyMessenger.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case AppConsts.MSG_SERVICE_REPLY_UPDATE:
                    String newReplyMsg = msg.getData().getString("msg");
                    Log.w(TAG, "界面修改了回复内容：" + newReplyMsg);
                    if (!TextUtils.isEmpty(newReplyMsg)) {
                        replyMsg = newReplyMsg;
                    }
                    break;
                case AppConsts.MSG_SERVICE_START_REPLY:
                    Messenger messenger = msg.replyTo;
                    Message message = Message.obtain(null, AppConsts.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    Log.w(TAG, "开始回复客户端");
                    bundle.putString("reply", replyMsg);
                    message.setData(bundle);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private final Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
