package com.jx.wxhb.fragment;


import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;
import com.jx.wxhb.activity.LuckyGroupActivity;
import com.jx.wxhb.activity.OfficialBrowsingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class FunnyFragment extends BaseFragment {

    @Bind(R.id.official_push_layout)
    CardView officialPushLayout;
    @Bind(R.id.lucky_group_layout)
    CardView luckyGroupLayout;
    @Bind(R.id.daiguo_layout)
    CardView daiguoLayout;


    public static FunnyFragment newInstance() {
        FunnyFragment fragment = new FunnyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_funny_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.lucky_group_layout)
    public void onGroupClick(){
        getActivity().startActivity(LuckyGroupActivity.newIntent(getActivity()));
    }

    @OnClick(R.id.official_push_layout)
    public void onOfficialClick(){
//        ActivityOptions options =
//                ActivityOptions.makeSceneTransitionAnimation(getActivity(), officialPushLayout, officialPushLayout.getTransitionName());
        getActivity().startActivity(OfficialBrowsingActivity.newIntent(getActivity()));
    }
}
