package com.jx.wxhb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;
import com.jx.wxhb.model.HBinfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/15.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    List<HBinfo> hBinfoList;

    public HistoryAdapter(List<HBinfo> hBinfoList) {
        this.hBinfoList = hBinfoList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_history_layout,null);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd  HH:mm");
        holder.dateView.setText(simpleDateFormat.format(new Date(hBinfoList.get(position).getDate())));
        holder.titleView.setText(hBinfoList.get(position).getTitle());
        holder.descView.setText(hBinfoList.get(position).getDesc());
        holder.moneyView.setText(hBinfoList.get(position).getMoney()+"元");
    }

    @Override
    public int getItemCount() {
        return hBinfoList.size();
    }

    public void sethBinfoList(List<HBinfo> hBinfoList) {
        this.hBinfoList = hBinfoList;
    }

}
