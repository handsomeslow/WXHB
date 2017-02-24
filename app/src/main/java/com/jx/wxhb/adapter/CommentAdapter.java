package com.jx.wxhb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.model.CommentInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Desc
 * Created by Jun on 2017/2/24.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public void refreshCommentInfoList(List<CommentInfo> commentList) {
        if (commentInfoList == null){
            return;
        }
        commentInfoList.clear();
        commentInfoList.addAll(commentList);
        notifyDataSetChanged();
    }

    List<CommentInfo> commentInfoList;

    public CommentAdapter(List<CommentInfo> commentInfoList) {
        this.commentInfoList = commentInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_comment_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentInfo commentInfo = commentInfoList.get(position);
        holder.contentTextView.setText(commentInfo.getContent());
        holder.owerNameTextView.setText(commentInfo.getName());
        holder.timeNameTextView.setText(commentInfo.getCreateTime());
        holder.numberTextView.setText(position+"楼");
    }

    @Override
    public int getItemCount() {
        return commentInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.number_text_view)
        TextView numberTextView;
        @Bind(R.id.ower_name_text_view)
        TextView owerNameTextView;
        @Bind(R.id.time_name_text_view)
        TextView timeNameTextView;
        @Bind(R.id.content_text_view)
        TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
