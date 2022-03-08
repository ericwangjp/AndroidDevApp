// INewAddListener.aidl
package com.example.androiddevapp;

// Declare any non-default types here with import statements
import com.example.androiddevapp.bean.Student;

interface INewAddListener {
    void onNewAdd(in Student student);
}