package com.jx.wxhb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jx.wxhb.R;

/**
 * Desc
 * Created by Jun on 2017/2/15.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    TextView dateView;
    TextView titleView;
    TextView descView;
    TextView moneyView;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        bindView(itemView);
    }

    private void bindView(View itemView){
        dateView = (TextView) itemView.findViewById(R.id.date_text_view);
        titleView = (TextView) itemView.findViewById(R.id.title_text_view);
        descView = (TextView) itemView.findViewById(R.id.desc_text_view);
        moneyView = (TextView) itemView.findViewById(R.id.money_text_view);
    }
}
