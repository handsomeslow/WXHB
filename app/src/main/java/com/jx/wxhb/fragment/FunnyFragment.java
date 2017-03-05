package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;
import com.jx.wxhb.activity.LuckyGroupActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FunnyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FunnyFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.official_push_layout)
    CardView officialPushLayout;
    @Bind(R.id.lucky_group_layout)
    CardView luckyGroupLayout;
    @Bind(R.id.daiguo_layout)
    CardView daiguoLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static FunnyFragment newInstance() {
        FunnyFragment fragment = new FunnyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
}
