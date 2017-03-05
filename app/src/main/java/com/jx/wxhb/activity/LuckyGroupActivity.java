package com.jx.wxhb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.NewsInfoAdapter;
import com.jx.wxhb.adapter.PushGroupAdapter;
import com.jx.wxhb.model.LuckyGroupInfo;
import com.jx.wxhb.presenter.LuckyGroupContract;
import com.jx.wxhb.presenter.LuckyGroupPresenter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LuckyGroupActivity extends BaseActivity implements LuckyGroupContract.View{

    @Bind(R.id.lucky_group_list_view)
    UltimateRecyclerView luckyGroupListView;

    private PushGroupAdapter adapter;

    private LuckyGroupContract.Presenter presenter;

    public static Intent newIntent(Context context){
        Intent intent = new Intent();
        intent.setClass(context,LuckyGroupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_group_layout);
        ButterKnife.bind(this);
        initView();
        presenter = new LuckyGroupPresenter(this);
        presenter.pullGroupList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.pullGroupList();
    }

    private void initView(){
        luckyGroupListView.setLayoutManager(new LinearLayoutManager(this));
        luckyGroupListView.displayDefaultFloatingActionButton(true);
        luckyGroupListView.getDefaultFloatingActionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PublishLuckyGroupActivity.newIntent(LuckyGroupActivity.this));
            }
        });
    }

    @Override
    public void refreshListsuccess(List<LuckyGroupInfo> groupInfoList) {
        if (adapter == null){
            adapter = new PushGroupAdapter(this,groupInfoList);
            luckyGroupListView.setAdapter(adapter);
        } else {
            adapter.setGroupList(groupInfoList);
        }
    }

    @Override
    public void refreshListFaild() {

    }

    @Override
    public void loadMoreDataSuccess(List<LuckyGroupInfo> groupInfoList) {
        if (adapter == null) {
            adapter = new PushGroupAdapter(this, groupInfoList);
            luckyGroupListView.setAdapter(adapter);
        } else {
            adapter.insertList(groupInfoList);
        }
    }

    @Override
    public void loadMoreDataFaild() {

    }
}
