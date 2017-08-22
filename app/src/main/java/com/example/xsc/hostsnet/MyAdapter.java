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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener{

    private ArrayList<Item> mData;
    public static interface OnItemClickListener {
        void onItemClick(View view ,int position);
    }
    private OnItemClickListener mOnItemClickListener = null;

    public MyAdapter(ArrayList<Item> data) {
        this.mData = data;
    }

    public MyAdapter updateData(ArrayList<Item> data) {
        this.mData = data;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        if(MainActivity.version == 19){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist19, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            v.setOnClickListener(this);
            return viewHolder;
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            v.setOnClickListener(this);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.city.setText(mData.get(position).getCity());
        holder.ip.setText(mData.get(position).getIp());
        holder.time.setText(mData.get(position).getTime());
        holder.itemView.setTag(position);
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

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //getTag获取的即是点击位置
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

