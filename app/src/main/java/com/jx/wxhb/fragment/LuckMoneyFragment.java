package com.jx.wxhb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.HistoryAdapter;
import com.jx.wxhb.model.HBinfo;
import com.jx.wxhb.utils.ContentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 */
public class LuckMoneyFragment extends BaseFragment {

    @Bind(R.id.money_count_text_view)
    TextView moneyCountTextView;
    @Bind(R.id.history_count_text_view)
    TextView historyCountTextView;
    @Bind(R.id.list_view)
    RecyclerView listView;

    private String title;

    private Realm realm;
    private RealmResults<HBinfo> hbinfos;

    HistoryAdapter adapter;

    public static LuckMoneyFragment newInstance(String title) {
        LuckMoneyFragment fragment = new LuckMoneyFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_luck_money_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getDefaultInstance();

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //hbinfos = realm.where(HBinfo.class).findAllAsync();

    }

    @Override
    public void onResume() {
        super.onResume();
        hbinfos = realm.where(HBinfo.class).findAllAsync();
        hbinfos.addChangeListener(new RealmChangeListener<RealmResults<HBinfo>>() {
            @Override
            public void onChange(RealmResults<HBinfo> element) {
                initView();
            }
        });
    }

    private void initView() {
        float f = (float) Math.round(hbinfos.sum("money").floatValue() * 100) / 100;
        moneyCountTextView.setText(f + " 元");
        historyCountTextView.setText(hbinfos.size() + "个");
        hbinfos = hbinfos.sort("date", Sort.DESCENDING);
        if (adapter == null) {
            adapter = new HistoryAdapter(realm.copyFromRealm(hbinfos));
            listView.setAdapter(adapter);
        } else {
            adapter.sethBinfoList(realm.copyFromRealm(hbinfos));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        realm.close();
    }
}
