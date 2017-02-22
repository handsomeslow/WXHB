package com.jx.wxhb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;
import com.jx.wxhb.activity.WebViewActivity;
import com.jx.wxhb.model.HotNewInfo;
import com.jx.wxhb.utils.HtmlDivUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.List;

/**
 * Created by 徐俊 on 2017/2/18.
 */


public class NewsInfoAdapter extends UltimateViewAdapter<NewsInfoViewHolder> {


    Context context;

    List<HotNewInfo> newInfoList;

    public NewsInfoAdapter(Context context,List<HotNewInfo> newInfoList) {
        this.context = context;
        this.newInfoList = newInfoList;
    }

    @Override
    public void onBindViewHolder(NewsInfoViewHolder holder, int position) {
        if (position >= newInfoList.size()){
            return;
        }
        HotNewInfo newInfo = newInfoList.get(position);
        final String title = newInfoList.get(position).getTitle();
        final String url = newInfoList.get(position).getUrl();
        holder.titleView.setText(title);
        holder.officialView.setText(newInfo.getOfficial());
        String count = newInfo.getReadcount()>= HtmlDivUtil.MAX_COUNT_FLOAT ?
                HtmlDivUtil.MAX_COUNT:String.valueOf((int)newInfo.getReadcount());
        holder.readCountView.setText(count);
        count = newInfo.getLikecount()>= HtmlDivUtil.MAX_COUNT_FLOAT ?
                HtmlDivUtil.MAX_COUNT:String.valueOf((int)newInfo.getLikecount());
        holder.likeCountView.setText(count);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(WebViewActivity.newIntent(context,title,url));
            }
        });
    }


    public void setNewInfoList(List<HotNewInfo> newInfoList) {
        this.newInfoList = newInfoList;
    }

    @Override
    public NewsInfoViewHolder newFooterHolder(View view) {
        return new NewsInfoViewHolder(view);
    }

    @Override
    public NewsInfoViewHolder newHeaderHolder(View view) {
        return new NewsInfoViewHolder(view);
    }

    @Override
    public NewsInfoViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list_layout,parent,false);
        return new NewsInfoViewHolder(itemView);
    }

    @Override
    public int getAdapterItemCount() {
        return newInfoList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
