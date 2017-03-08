package com.jx.wxhb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.model.LuckyGroupInfo;
import com.jx.wxhb.utils.CloudContentUtil;
import com.jx.wxhb.utils.ImageLoaderUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 徐俊 on 2017/3/5.
 */

public class PushGroupAdapter extends UltimateViewAdapter<PushGroupAdapter.PushGroupViewHolder> {
    private Context context;

    private List<LuckyGroupInfo> list;


    public PushGroupAdapter(Context context, List<LuckyGroupInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public PushGroupViewHolder newFooterHolder(View view) {
        return new PushGroupViewHolder(view);
    }

    @Override
    public PushGroupViewHolder newHeaderHolder(View view) {
        return new PushGroupViewHolder(view);
    }

    @Override
    public PushGroupViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_view, parent, false);
        return new PushGroupViewHolder(itemView);
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(PushGroupViewHolder holder, final int position) {
        final LuckyGroupInfo info = list.get(position);
        holder.nameTextView.setText(info.getOwer().getUsername());
        holder.contentTextView.setText(info.getContent());
        holder.dateTextView.setText(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(info.getPublishDate()));
        ImageLoaderUtil.displayImageByObjectId(info.getOwer().getString(CloudContentUtil.USER_AVATAR),holder.avatarImageView);
        if (info.getPhotoList()!=null && info.getPhotoList().size()>0){
            ImageLoaderUtil.displayImageByObjectId(info.getPhotoList().get(0),holder.photoImageView);
        }
        holder.addCommImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                    onClickListener.OnClick(info,position);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
    public void setGroupList(List<LuckyGroupInfo> newlist) {
        if (list!=null){
            list.clear();
        }
        list = newlist;
        notifyDataSetChanged();
    }

    public void insertList(List<LuckyGroupInfo> newlist){
        if (list!=null){
            insertInternal(list,newlist);
        }
    }


    public static class PushGroupViewHolder extends UltimateRecyclerviewViewHolder {

        @Bind(R.id.avatar_image_view)
        ImageView avatarImageView;
        @Bind(R.id.name_text_view)
        TextView nameTextView;
        @Bind(R.id.content_text_view)
        TextView contentTextView;
        @Bind(R.id.photo_gridview)
        GridView photoGridview;
        @Bind(R.id.date_text_view)
        TextView dateTextView;
        @Bind(R.id.photo_image_view)
        ImageView photoImageView;
        @Bind(R.id.add_comment_botton)
        ImageView addCommImageView;


        public PushGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private OnClickListener onClickListener;

    public interface OnClickListener{
        void OnClick(LuckyGroupInfo info,int position);
    }

}
