package com.example.androiddevapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androiddevapp.adapter.MainAdapter;
import com.example.androiddevapp.aidl.AidlActivity;
import com.example.androiddevapp.bean.FeatureItem;
import com.example.androiddevapp.binderpool.BinderPoolActivity;
import com.example.androiddevapp.contentprovider.ProviderActivity;
import com.example.androiddevapp.databinding.ActivityMainBinding;
import com.example.androiddevapp.messenger.MessengerActivity;
import com.example.androiddevapp.socket.SocketActivity;
import com.example.androiddevapp.viewbase.CustomViewActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<FeatureItem> featureItemLists = new ArrayList<>();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
    }

    private void initData() {
        featureItemLists.add(new FeatureItem("messenger", ""));
        featureItemLists.add(new FeatureItem("aidl", ""));
        featureItemLists.add(new FeatureItem("provider", ""));
        featureItemLists.add(new FeatureItem("socket", ""));
        featureItemLists.add(new FeatureItem("binderPool", ""));
        featureItemLists.add(new FeatureItem("customView", ""));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvMain.setLayoutManager(linearLayoutManager);
        MainAdapter mainAdapter = new MainAdapter(this, featureItemLists);
        binding.rvMain.setAdapter(mainAdapter);
        mainAdapter.setOnItemClickListener((itemView, position) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, MessengerActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, AidlActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, ProviderActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(MainActivity.this, SocketActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(MainActivity.this, BinderPoolActivity.class));
                    break;
                case 5:
                    startActivity(new Intent(MainActivity.this, CustomViewActivity.class));
                    break;
                default:
                    break;
            }
        });
    }


}