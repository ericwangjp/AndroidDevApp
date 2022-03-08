package com.example.androiddevapp.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: BookManagerImpl
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/2/24 4:12 下午
 */
class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if ((iInterface != null) && (iInterface instanceof IBookManager)) {
            return (IBookManager) iInterface;
        }
        return new BookManagerImpl.Proxy(obj);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    //    运行在服务端的Binder线程池中
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> bookList = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(bookList);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book book;
                if (0 != data.readInt()) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                this.addBook(book);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        // TODO: 2022/2/24 待实现
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        // TODO: 2022/2/24 待实现
    }

    private static class Proxy implements IBookManager {

        private IBinder mRemote;

        public Proxy(IBinder mRemote) {
            this.mRemote = mRemote;
        }

        //        运行在客户端
        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0); // 此时线程挂起，服务端的onTransact方式执行,直到RPC过程返回后当前线程继续执行
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }
    }

}
