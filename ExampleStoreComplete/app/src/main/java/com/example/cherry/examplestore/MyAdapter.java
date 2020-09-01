package com.example.cherry.examplestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.HolderView> {
    ArrayList<Upload> list;
    Context ct;

    public MyAdapter(ArrayList<Upload> list, Context ct) {
        this.list = list;
        this.ct = ct;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderView(LayoutInflater.from(ct).inflate(R.layout.row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        Glide.with(ct)
                .load(list.get(position).getUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .override(300,300)
                .centerCrop()
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {
        ImageView iv;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
