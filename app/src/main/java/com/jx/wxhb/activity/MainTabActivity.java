package com.jx.wxhb.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jx.wxhb.R;
import com.jx.wxhb.fragment.BaseFragment;
import com.jx.wxhb.fragment.HotNewsFragment;
import com.jx.wxhb.fragment.LuckMoneyFragment;
import com.jx.wxhb.fragment.MyFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainTabActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener{
    LuckMoneyFragment luckMoneyFragment;
    HotNewsFragment hotNewsFragment;
    MyFragment myFragment;

    @Bind(R.id.fragment_wrap)
    FrameLayout fragmentWrap;

    // 底部tab
    @Bind(R.id.bottom_bar)
    BottomNavigationBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        ButterKnife.bind(this);

        bottomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomBar.addItem(new BottomNavigationItem(R.drawable.cup,"红包")).setActiveColor(R.color.colorPrimary)
                .addItem(new BottomNavigationItem(R.drawable.contacts,"热点")).setActiveColor(R.color.GreenColor)
                .addItem(new BottomNavigationItem(R.drawable.eye,"发现")).setActiveColor(R.color.OrangeColor)
                .addItem(new BottomNavigationItem(R.drawable.settings,"设置")).setActiveColor(R.color.DarkGrayColor)
                .setFirstSelectedPosition(0)
                .initialise();

        setDefaultFragment();
        bottomBar.setTabSelectedListener(this);
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_wrap, LuckMoneyFragment.newInstance("红包大作战"));
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (position){
            case 0:
                if (luckMoneyFragment==null){
                    luckMoneyFragment = LuckMoneyFragment.newInstance("红包大作战");
                }
                ft.replace(R.id.fragment_wrap,luckMoneyFragment);
                break;

            case 1:
                if (hotNewsFragment==null){
                    hotNewsFragment = HotNewsFragment.newInstance("今日微信热点");
                }
                ft.replace(R.id.fragment_wrap,hotNewsFragment);
                break;
            case 2:
            case 3:
                if (myFragment==null){
                    myFragment = MyFragment.newInstance("设置");
                }
                ft.replace(R.id.fragment_wrap,myFragment);
                break;
        }

        ft.commitAllowingStateLoss();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
