package com.example.androiddevapp.binder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: IBookManager
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/2/24 3:16 下午
 */
public interface IBookManager extends IInterface {
    static final String DESCRIPTOR = "com.example.demo.binder.IBookManager";
    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    public List<Book> getBookList() throws RemoteException;

    public void addBook(Book book) throws RemoteException;
}
