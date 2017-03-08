package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.NewsInfoAdapter;
import com.jx.wxhb.model.HotNewInfo;
import com.jx.wxhb.presenter.NewsContract;
import com.jx.wxhb.presenter.NewsPresenter;
import com.jx.wxhb.utils.ContentUtil;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 微信热门文章
 */
public class HotNewsFragment extends BaseFragment implements NewsContract.View {

    private List<HotNewInfo> newList;

    @Bind(R.id.new_list_view)
    UltimateRecyclerView newListView;


    private String title;

    private NewsInfoAdapter adapter;

    private NewsPresenter presenter;

    private TopContentFragment topContentFragment;

    public static HotNewsFragment newInstance(String title) {
        HotNewsFragment fragment = new HotNewsFragment();
        Bundle args = new Bundle();
        args.putString(ContentUtil.EXTRA_ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ContentUtil.EXTRA_ARG_TITLE);
        }
        newList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_news_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newListView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        newListView.setNormalHeader(LayoutInflater.from(getActivity())
//                .inflate(R.layout.view_bottom_progressbar, null));

        newListView.setLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.view_bottom_progressbar, null));
        newListView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                Log.d("jun", "loadMore");
                presenter.pullNewsFromCloud();
            }
        });
        newListView.enableDefaultSwipeRefresh(false);
        presenter = new NewsPresenter(this);

        presenter.pullNewsFromCloud();
//        newListView.post(new Runnable() {
//            @Override
//            public void run() {
//                newListView.setRefreshing(true);
//            }
//        });
        initTopContentFragment();
    }

    private void initTopContentFragment(){
        if (topContentFragment == null){
            topContentFragment = TopContentFragment.newInstance();
            addFragment(topContentFragment,R.id.top_content_wrap);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        adapter = null;
    }

    @Override
    public void loadMoreDataSuccess(List<HotNewInfo> newsList) {
        if (adapter == null) {
            adapter = new NewsInfoAdapter(getActivity(), newsList);
            newListView.setAdapter(adapter);
        } else {
            adapter.insertInternal(newsList,newList);
        }

    }

    @Override
    public void loadnoMoreData() {
        newListView.disableLoadmore();
        Toast.makeText(getActivity(),"没有更多内容",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadMoreDataFail() {

    }


    @Override
    public void refreshDataSuccess(List<HotNewInfo> newsList) {
        if (adapter == null) {
            adapter = new NewsInfoAdapter(getActivity(), newsList);
            newListView.setAdapter(adapter);
        } else {
            adapter.setNewInfoList(newsList);
        }
    }

    @Override
    public void refreshDataFail() {
        Toast.makeText(getActivity(),"没有更多内容",Toast.LENGTH_SHORT).show();
    }
}
