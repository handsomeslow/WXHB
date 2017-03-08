package com.jx.wxhb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.OfficialBrowsingAdapter;
import com.jx.wxhb.model.Item;
import com.jx.wxhb.utils.UIUtils;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.hack.HackyViewPager;

public class OfficialBrowsingActivity extends BaseActivity {

    private static final Item[] ITEMS = {
            new Item(R.drawable.xkxxh_logo),
            new Item(R.drawable.chapin_logo),
            new Item(R.drawable.rmrb_logo),
            new Item(R.drawable.xkxxh_logo),
            new Item(R.drawable.chapin_logo),
            new Item(R.drawable.rmrb_logo),
            new Item(R.drawable.xkxxh_logo),
            new Item(R.drawable.chapin_logo),
            new Item(R.drawable.rmrb_logo)
    };

    @Bind(R.id.official_browsing_viewpager)
    HackyViewPager officialBrowsingViewpager;

    @Bind(R.id.official_browsing_list_view)
    UltimateRecyclerView officialBrowsingListView;

    OfficialBrowsingAdapter adapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, OfficialBrowsingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_browsing_layout);
        ButterKnife.bind(this);
        showBackButton();
        setTitle("公众号");

        adapter = new OfficialBrowsingAdapter(getSupportFragmentManager(),ITEMS);
        officialBrowsingViewpager.setClipToPadding(false);
        int s = UIUtils.dip2Px(10);
        officialBrowsingViewpager.setPadding(s, 0, s, 0);
        officialBrowsingViewpager.setAdapter(adapter);
        officialBrowsingViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
