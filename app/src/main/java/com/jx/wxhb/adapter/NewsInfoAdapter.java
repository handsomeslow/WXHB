package com.jx.wxhb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;
import com.jx.wxhb.activity.WebViewActivity;
import com.jx.wxhb.model.HotNewInfo;
import com.jx.wxhb.utils.HtmlDivUtil;

import java.util.List;

/**
 * Created by 徐俊 on 2017/2/18.
 */

public class NewsInfoAdapter extends RecyclerView.Adapter<NewsInfoViewHolder> {


    Context context;

    List<HotNewInfo> newInfoList;

    public NewsInfoAdapter(Context context,List<HotNewInfo> newInfoList) {
        this.context = context;
        this.newInfoList = newInfoList;
    }

    @Override
    public NewsInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_news_list_layout,null);
        return new NewsInfoViewHolder(parent.getContext(),itemView);
    }

    @Override
    public void onBindViewHolder(NewsInfoViewHolder holder, int position) {
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

    @Override
    public int getItemCount() {
        return newInfoList.size();
    }

    public void setNewInfoList(List<HotNewInfo> newInfoList) {
        this.newInfoList = newInfoList;
    }
}
