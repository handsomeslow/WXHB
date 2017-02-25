package com.jx.wxhb.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jx.wxhb.R;
import com.jx.wxhb.adapter.MainTabAdapter;
import com.jx.wxhb.fragment.HotNewsFragment;
import com.jx.wxhb.fragment.LuckMoneyFragment;
import com.jx.wxhb.fragment.MyFragment;
import com.jx.wxhb.fragment.RandomFunnyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainTabActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener,BottomNavigationBar.OnTabSelectedListener{
    LuckMoneyFragment luckMoneyFragment;
    HotNewsFragment hotNewsFragment;
    MyFragment myFragment;
    RandomFunnyFragment randomFunnyFragment;

    // 底部tab
    @Bind(R.id.bottom_bar)
    BottomNavigationBar bottomBar;


    @Bind(R.id.fragment_view_pager)
    ViewPager fragmentViewPager;

    MainTabAdapter mainTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        ButterKnife.bind(this);

        bottomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomBar.addItem(new BottomNavigationItem(R.drawable.cup, "红包")).setActiveColor(R.color.colorPrimary)
                .addItem(new BottomNavigationItem(R.drawable.contacts, "热点")).setActiveColor(R.color.GreenColor)
                .addItem(new BottomNavigationItem(R.drawable.eye, "发现")).setActiveColor(R.color.OrangeColor)
                .addItem(new BottomNavigationItem(R.drawable.settings, "设置")).setActiveColor(R.color.DarkGrayColor)
                .setFirstSelectedPosition(0)
                .initialise();

        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager(), setFragments());
        fragmentViewPager.setAdapter(mainTabAdapter);
        fragmentViewPager.addOnPageChangeListener(this);

    }

    private List<Fragment> setFragments(){
        List<Fragment> fragments = new ArrayList<>();
        luckMoneyFragment = LuckMoneyFragment.newInstance("红包大作战");
        fragments.add(luckMoneyFragment);
        hotNewsFragment = HotNewsFragment.newInstance("今日微信热点");
        fragments.add(hotNewsFragment);
        randomFunnyFragment = RandomFunnyFragment.newInstance("猜猜乐");
        fragments.add(randomFunnyFragment);
        myFragment = MyFragment.newInstance("设置");
        fragments.add(myFragment);
        return fragments;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(int position) {
        fragmentViewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
