package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;

/**
 *
 */
public class OfficialHistoryFragment extends BaseFragment {

    public static OfficialHistoryFragment newInstance() {
        OfficialHistoryFragment fragment = new OfficialHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_official_history_layout, container, false);
    }

}
