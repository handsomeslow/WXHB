package com.jx.wxhb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;

/**
 * Desc
 * Created by Jun on 2017/2/24.
 */

public class OfficialAdapter extends UltimateDifferentViewTypeAdapter {
    @Override
    public Enum getEnumFromPosition(int position) {
        return null;
    }

    @Override
    public Enum getEnumFromOrdinal(int ordinal) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
