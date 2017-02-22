package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 */
public class HotNewsFragment extends BaseFragment implements NewsContract.View {

    List<HotNewInfo> newList;

    @Bind(R.id.new_list_view)
    UltimateRecyclerView newListView;


    private String title;

    NewsInfoAdapter adapter;

    NewsPresenter presenter;

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case ContentUtil.MSG_REFRESH:
//                    initView();
//                    break;
//            }
//        }
//    };


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
        getActivity().setTitle(title);

        newListView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        newListView.reenableLoadmore();

        newListView.displayDefaultFloatingActionButton(true);
        newListView.setLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.view_bottom_progressbar, null));
        newListView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                newListView.setRefreshing(true);
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

    }
//
//    private void initView() {
//        if (adapter == null) {
//            adapter = new NewsInfoAdapter(getActivity(), newList);
//            newListView.setAdapter(adapter);
//        } else {
//            adapter.setNewInfoList(newList);
//            adapter.notifyDataSetChanged();
//        }
//        newListView.setRefreshing(false);
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        adapter = null;
    }

    @Override
    public void refreshListView(List<HotNewInfo> newsList) {
        if (adapter == null) {
            adapter = new NewsInfoAdapter(getActivity(), newsList);
            newListView.setAdapter(adapter);
        } else {
            adapter.insertInternal(newsList,newList);
        }
        newListView.setRefreshing(false);

    }

}
