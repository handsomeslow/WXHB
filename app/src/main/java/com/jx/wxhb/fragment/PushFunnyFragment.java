package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;
import com.jx.wxhb.presenter.FunnyContract;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public class PushFunnyFragment extends BaseFragment implements FunnyContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.list_view)
    UltimateRecyclerView listView;

    private String mParam1;
    private String mParam2;

    public static PushFunnyFragment newInstance(String param1, String param2) {
        PushFunnyFragment fragment = new PushFunnyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_push_funny, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshListView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
