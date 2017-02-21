package com.jx.wxhb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.HistoryAdapter;
import com.jx.wxhb.model.HBinfo;

import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Desc
 * Created by Jun on 2017/2/16.
 */

public class HistoryListFragment extends BaseFragment implements Observer{

    @Bind(R.id.list_view)
    RecyclerView listView;


    HistoryAdapter adapter;

    public static HistoryListFragment newInstance() {

        Bundle args = new Bundle();
        HistoryListFragment fragment = new HistoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<HBinfo> hBinfos = realm.where(HBinfo.class).findAll();
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter(realm.copyFromRealm(hBinfos.sort("date", Sort.DESCENDING)));

        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
