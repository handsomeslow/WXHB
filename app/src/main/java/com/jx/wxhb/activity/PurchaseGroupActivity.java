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
    OfficialFragment officialFragment;
    OfficialHistoryFragment officialHistoryFragment;


    public static Intent newIntent(Context context){
        Intent intent = new Intent();
        intent.setClass(context,PurchaseGroupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_purchase_group_layout);
        ButterKnife.bind(this);
        showBackButton();
        addFragmentList();
    }

    private void addFragmentList(){
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        officialFragment = OfficialFragment.newInstance("58af97bb570c35006919254f");
        fragmentList.add(officialFragment);
        titleList.add("简介");
        officialHistoryFragment = OfficialHistoryFragment.newInstance();
        fragmentList.add(officialHistoryFragment);
        titleList.add("历史文章");
        tabAdapter = new MainTabAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(tabAdapter);
        tablayout.setImageBackground("58be8da5128fe1007e65c81d")
                .setTitle("公众号详情")
                .setUpWithViewPager(viewPager);
    }
}
