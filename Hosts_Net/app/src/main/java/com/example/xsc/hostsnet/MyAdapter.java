package com.example.xsc.hostsnet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Delcas on 2017/8/14.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<Item> mData;

    public MyAdapter(ArrayList<Item> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<Item> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.city.setText(mData.get(position).getCity());
        holder.ip.setText(mData.get(position).getIp());
        holder.time.setText(mData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView city,ip,time;

        public ViewHolder(View itemView) {
            super(itemView);
            city = (TextView) itemView.findViewById(R.id.txt_city);
            ip = (TextView) itemView.findViewById(R.id.txt_ip);
            time = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }
}

