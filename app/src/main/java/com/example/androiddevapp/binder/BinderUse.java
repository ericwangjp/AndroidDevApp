package com.example.androiddevapp.binder;

import android.os.IBinder;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BinderUse
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/2/24 4:43 下午
 */
class BinderUse {
    private IBookManager iBookManager = new BookManagerImpl();
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iBookManager == null){
                return;
            }
            iBookManager.asBinder().unlinkToDeath(deathRecipient,0);
            iBookManager = null;
            // TODO: 2022/2/24 重新绑定远程service
        }
    };
}
