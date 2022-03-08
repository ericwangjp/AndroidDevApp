// IStudentManager.aidl
package com.example.androiddevapp;

// Declare any non-default types here with import statements
import com.example.androiddevapp.bean.Student;
import com.example.androiddevapp.INewAddListener;

interface IStudentManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    List<Student> getStudentList();
    void addStudent(in Student student);
    void registerNewListener(INewAddListener listener);
    void unregisterNewListener(INewAddListener listener);
}