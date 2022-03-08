package com.example.androiddevapp.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddevapp.databinding.ActivityProviderBinding;

public class ProviderActivity extends AppCompatActivity {

    private ActivityProviderBinding binding;
    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProviderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
    }

    private void initData() {
//        学生信息操作
        Uri studentUri = Uri.parse("content://com.example.androiddevapp.contentprovider.StudentProvider/student");
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 10);
        contentValues.put("name", "张大傻");
        ContentResolver contentResolver = getContentResolver();
        contentResolver.insert(studentUri, contentValues);
        Cursor studentCursor = contentResolver.query(studentUri, new String[]{"_id", "name"}, null, null, null);
        while (studentCursor.moveToNext()) {
            int studentId = studentCursor.getInt(0);
            String studentName = studentCursor.getString(1);
            Log.w(TAG, "学生id: " + studentId + " 学生名称：" + studentName);
        }
        studentCursor.close();

//        用户信息操作
        Uri userUri = Uri.parse("content://com.example.androiddevapp.contentprovider.StudentProvider/user");
        Cursor userCursor = contentResolver.query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            int userId = userCursor.getInt(0);
            String userName = userCursor.getString(1);
            boolean isMale = userCursor.getInt(2) == 1;
            Log.w(TAG, "用户id: " + userId + " 用户名称：" + userName + " 用户性别：" + isMale);
        }
        userCursor.close();
    }
}