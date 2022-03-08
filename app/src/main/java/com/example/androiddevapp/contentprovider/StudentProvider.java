package com.example.androiddevapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: StudentProvider
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/3/1 11:17 上午
 */
public class StudentProvider extends ContentProvider {

    private static final String TAG = "StudentProvider";
    public static final String AUTHORITY = "com.example.androiddevapp.contentprovider.StudentProvider";
    public static final Uri STUDENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/student");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    public static final int STUDENT_URI_CODE = 1;
    public static final int USER_URI_CODE = 2;
    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, "student", STUDENT_URI_CODE);
        URI_MATCHER.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate: 当前线程：" + Thread.currentThread().getName());
//        不推荐这里主线程执行耗时的数据库操作
        initProviderData();
        return true;
    }

    private void initProviderData() {
        sqLiteDatabase = new DBOpenHelper(getContext()).getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + DBOpenHelper.STUDENT_TABLE_NAME);
        sqLiteDatabase.execSQL("delete from " + DBOpenHelper.USER_TABLE_NAME);
        sqLiteDatabase.execSQL("insert into student values(6,'jack');");
        sqLiteDatabase.execSQL("insert into student values(7,'tom');");
        sqLiteDatabase.execSQL("insert into student values(8,'lucy');");
        sqLiteDatabase.execSQL("insert into user values(1,'aa',1);");
        sqLiteDatabase.execSQL("insert into user values(2,'bb',0);");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG, "query: 当前线程：" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("不合法的Uri：" + uri);
        }
        return sqLiteDatabase.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e(TAG, "getType: 当前线程：" + Thread.currentThread().getName());
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.e(TAG, "insert: 当前线程：" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("不合法的Uri：" + uri);
        }
        sqLiteDatabase.insert(table, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "delete: 当前线程：" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("不合法的Uri：" + uri);
        }
        int count = sqLiteDatabase.delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "update: 当前线程：" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("不合法的Uri：" + uri);
        }
        int row = sqLiteDatabase.update(table, contentValues, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (URI_MATCHER.match(uri)) {
            case STUDENT_URI_CODE:
                tableName = DBOpenHelper.STUDENT_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DBOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
