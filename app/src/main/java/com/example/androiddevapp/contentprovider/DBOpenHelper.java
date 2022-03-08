package com.example.androiddevapp.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: DBOpenHelper
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/3/1 4:28 下午
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBOpenHelper";

    public static final String DB_NAME = "student_provider.db";
    public static final String STUDENT_TABLE_NAME = "student";
    public static final String USER_TABLE_NAME = "user";

    public static final int DB_VERSION = 1;

    private String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT)";
    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT," + "sex INT)";

    public DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.w(TAG, "onCreate: 当前线程：" + Thread.currentThread().getName());
        sqLiteDatabase.execSQL(CREATE_STUDENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
