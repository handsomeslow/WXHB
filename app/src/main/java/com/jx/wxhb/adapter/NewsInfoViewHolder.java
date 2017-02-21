package com.jx.wxhb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.activity.WebViewActivity;

/**
 * Created by 徐俊 on 2017/2/18.
 */

public class NewsInfoViewHolder extends RecyclerView.ViewHolder {
    Context context;
    View itemView;

    TextView titleView;
    TextView officialView;
    TextView readCountView;
    TextView likeCountView;


    public NewsInfoViewHolder(Context context,View itemView) {
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        bindView(itemView);
    }

    private void bindView(View view){
        titleView = (TextView) view.findViewById(R.id.title_tv);
        officialView = (TextView) view.findViewById(R.id.official_count_text_view);
        readCountView = (TextView) view.findViewById(R.id.read_count_text_view);
        likeCountView = (TextView) view.findViewById(R.id.like_count_text_view);
    }

}
