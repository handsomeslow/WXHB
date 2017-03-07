package com.jx.wxhb.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.model.TopContent;
import com.jx.wxhb.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Desc
 * Created by Jun on 2017/3/7.
 */

public class TopContentAdapter extends PagerAdapter {
    private Context context;
    private List<TopContent> topContentList;

    public TopContentAdapter(Context context, List<TopContent> topContentList) {
        this.topContentList = topContentList;
        this.context = context;
    }


    @Override
    public int getCount() {
        if (topContentList == null) {
            return 0;
        }
        return topContentList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final TopContent content = topContentList.get(position);
        View view = View.inflate(context, R.layout.view_top_content, null);
        ImageView backgoundImageView = (ImageView) view.findViewById(R.id.backgound_image_view);
        TextView titleTextView = (TextView) view.findViewById(R.id.title_text_view);
        TextView positionTextView = (TextView) view.findViewById(R.id.position_text_view);
        titleTextView.setText(content.getTitle());
        if (getCount() > 1){
            positionTextView.setText(String.format("%d/%d",position+1,getCount()));
        }
        ImageLoaderUtil.displayImageByObjectId(content.getImage(),backgoundImageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGotoActivity!=null){
                    onGotoActivity.onClick(content,position);
                }
            }
        });
        container.addView(view);
        return view;
    }

    public void setOnGotoActivity(OnGotoActivity onGotoActivity) {
        this.onGotoActivity = onGotoActivity;
    }

    private OnGotoActivity onGotoActivity;

    public interface OnGotoActivity {
        void onClick(TopContent content,int position);
    }

}
