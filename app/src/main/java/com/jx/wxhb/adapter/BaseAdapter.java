package com.jx.wxhb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/22.
 */

public class BaseAdapter<VH extends RecyclerView.ViewHolder,T> extends UltimateViewAdapter<VH> {

    List<T> data;

    @Override
    public VH newFooterHolder(View view) {
        return null;
    }

    @Override
    public VH newHeaderHolder(View view) {
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void addData(){

    }
}
