package com.jx.wxhb;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.jx.wxhb.activity.BaseActivity;
import com.jx.wxhb.adapter.HistoryAdapter;
import com.jx.wxhb.model.HBinfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


public class MainActivity extends BaseActivity {

    @Bind(R.id.list_view)
    RecyclerView listView;

    @Bind(R.id.money_count_text_view)
    TextView moneyCountTextView;

    @Bind(R.id.history_count_text_view)
    TextView historyCountTextView;


    private Realm realm;
    private RealmResults<HBinfo> hbinfos;

    HistoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        listView.setLayoutManager(new LinearLayoutManager(this));
        hbinfos = realm.where(HBinfo.class).findAllAsync();
        hbinfos.addChangeListener(new RealmChangeListener<RealmResults<HBinfo>>() {
            @Override
            public void onChange(RealmResults<HBinfo> element) {
                initView();
            }
        });
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                hbinfos = realm.where(HBinfo.class).findAll();
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                hbinfos = hbinfos.sort("date", Sort.DESCENDING);
////                if (adapter == null) {
////                    adapter = new HistoryAdapter(realm.copyFromRealm(hbinfos));
////                    listView.setAdapter(adapter);
////                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initView();
//                    }
//                });
//
//            }
//        });

    }

    private void initView() {
        float f = (float)Math.round(hbinfos.sum("money").floatValue()*100)/100;
        moneyCountTextView.setText( f +" 元");
        historyCountTextView.setText(hbinfos.size()+"个");
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //realm.close();
    }
}
