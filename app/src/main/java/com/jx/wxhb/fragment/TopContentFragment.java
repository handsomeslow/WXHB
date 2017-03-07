package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.jx.wxhb.R;
import com.jx.wxhb.activity.WebViewActivity;
import com.jx.wxhb.adapter.TopContentAdapter;
import com.jx.wxhb.model.OfficialInfo;
import com.jx.wxhb.model.TopContent;
import com.jx.wxhb.utils.CloudContentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.hack.HackyViewPager;

/**
 *
 */
public class TopContentFragment extends BaseFragment {
    @Bind(R.id.top_content_viewpager)
    HackyViewPager topContentViewpager;

    TopContentAdapter adapter;


    public static TopContentFragment newInstance() {
        TopContentFragment fragment = new TopContentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_content_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTopContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getTopContent(){
        AVQuery<AVObject> pushQuery = new AVQuery<>(CloudContentUtil.PUSH_CONTENT_TABLE);
        pushQuery.whereEqualTo(CloudContentUtil.PUSH_CONTENT_IS_EFFECTIVE, 1);
        pushQuery.orderByAscending(CloudContentUtil.PUSH_CONTENT_PRIORITY);
        pushQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e!=null){
                    e.printStackTrace();
                    hideParentView();
                    return;
                }
                List<TopContent> topContentList = new ArrayList<TopContent>();
                for (AVObject object : list){
                    TopContent content = new TopContent();
                    content.setTitle(object.getString(CloudContentUtil.PUSH_CONTENT_TITLE));
                    content.setImage(object.getString(CloudContentUtil.PUSH_CONTENT_IMAGE));
                    content.setOfficialId(object.getString(CloudContentUtil.PUSH_CONTENT_OFFICIAL_ID));
                    content.setType(object.getInt(CloudContentUtil.PUSH_CONTENT_TYPE));
                    content.setUrl(object.getString(CloudContentUtil.PUSH_CONTENT_URL));
                    topContentList.add(content);
                }
                if (adapter == null){
                    adapter = new TopContentAdapter(getActivity(),topContentList);
                    topContentViewpager.setAdapter(adapter);
                    adapter.setOnGotoActivity(new TopContentAdapter.OnGotoActivity() {
                        @Override
                        public void onClick(TopContent content, int position) {
                            if (content.getType() == 1 && content.getUrl() != null){
                                startActivity(WebViewActivity.newIntent(getActivity(),content.getTitle(),content.getUrl()));
                            }
                        }
                    });
                }
                showParentView();
            }
        });
    }
}
