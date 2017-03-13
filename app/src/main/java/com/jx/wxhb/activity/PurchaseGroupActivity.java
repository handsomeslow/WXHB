package com.jx.wxhb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.MainTabAdapter;
import com.jx.wxhb.fragment.OfficialFragment;
import com.jx.wxhb.fragment.OfficialHistoryFragment;
import com.jx.wxhb.model.OfficialInfo;
import com.jx.wxhb.utils.ContentUtil;
import com.jx.wxhb.widget.CoordinatorTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PurchaseGroupActivity extends BaseActivity {

    private List<Fragment> fragmentList;
    private List<String> titleList;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.tablayout)
    CoordinatorTabLayout tablayout;

    private MainTabAdapter tabAdapter;

    private OfficialFragment officialFragment;

    private OfficialHistoryFragment officialHistoryFragment;

    private OfficialInfo info;
    public static Intent newIntent(Context context,OfficialInfo info){
        Intent intent = new Intent();
        intent.putExtra(ContentUtil.EXTRA_OFFICIAL_INFO,info);
        intent.setClass(context,PurchaseGroupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_purchase_group_layout);
        ButterKnife.bind(this);
        if (getIntent()!=null){
            info = getIntent().getParcelableExtra(ContentUtil.EXTRA_OFFICIAL_INFO);
        }

        showBackButton();
        addFragmentList();
    }

    private void addFragmentList(){
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        officialFragment = OfficialFragment.newInstance(info.getId());
        fragmentList.add(officialFragment);
        titleList.add("简介");
        officialHistoryFragment = OfficialHistoryFragment.newInstance();
        fragmentList.add(officialHistoryFragment);
        titleList.add("历史文章");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(tabAdapter);
        tablayout.setImageBackground(info.getImage())
                .setTitle("公众号详情")
                .setName(info.getName())
                .setDesc(info.getDesc())
                .setWxId(info.getWxId())
                .setCategory(info.getTags())
                .setCompany(info.getOrganization())
                .setUpWithViewPager(viewPager);
    }
}
