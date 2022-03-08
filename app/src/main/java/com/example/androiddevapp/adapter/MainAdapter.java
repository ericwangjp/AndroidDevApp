package com.example.androiddevapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddevapp.bean.FeatureItem;
import com.example.androiddevapp.databinding.ItemMainBinding;

import java.util.List;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: MainAdapter
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/2/25 2:31 下午
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<FeatureItem> mDatas;
    private OnItemClickListener onItemClickListener;

    public MainAdapter(Context context, List<FeatureItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(ItemMainBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.bind(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    protected class MainViewHolder extends RecyclerView.ViewHolder {

        private ItemMainBinding binding;

        public MainViewHolder(@NonNull ItemMainBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void bind(FeatureItem featureItem) {
            binding.tvTitle.setText(featureItem.itemName);
            if (onItemClickListener != null) {
                itemView.setOnClickListener(view -> onItemClickListener.onItemClick(view, getAdapterPosition()));
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
