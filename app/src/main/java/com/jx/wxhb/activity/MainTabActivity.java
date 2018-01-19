package com.jx.wxhb.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jx.wxhb.MyApplication;
import com.jx.wxhb.R;
import com.jx.wxhb.adapter.MainTabAdapter;
import com.jx.wxhb.fragment.FunnyFragment;
import com.jx.wxhb.fragment.HotNewsFragment;
import com.jx.wxhb.fragment.LuckMoneyFragment;
import com.jx.wxhb.fragment.MyFragment;
import com.jx.wxhb.fragment.OfficialFragment;
import com.jx.wxhb.fragment.QuestionAssistantFragment;
import com.jx.wxhb.fragment.RandomFunnyFragment;
import com.jx.wxhb.utils.AccessibilityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainTabActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener,BottomNavigationBar.OnTabSelectedListener{
    private static final String TAG = "MainTabActivity";

    LuckMoneyFragment luckMoneyFragment;
    HotNewsFragment hotNewsFragment;
    MyFragment myFragment;
    RandomFunnyFragment randomFunnyFragment;
    FunnyFragment funnyFragment;
    QuestionAssistantFragment questionAssistantFragment;
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
        bottomBar.setTabSelectedListener(this);

        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager(), setFragments());
        fragmentViewPager.setOffscreenPageLimit(3);
        fragmentViewPager.setAdapter(mainTabAdapter);
        fragmentViewPager.addOnPageChangeListener(this);
        setTitle("红包大作战");
        checkAccessibilityStatus();
    }

    private List<Fragment> setFragments(){
        List<Fragment> fragments = new ArrayList<>();
        luckMoneyFragment = LuckMoneyFragment.newInstance("红包大作战");
        fragments.add(luckMoneyFragment);
        hotNewsFragment = HotNewsFragment.newInstance("今日微信热点");
        fragments.add(hotNewsFragment);
//        randomFunnyFragment = RandomFunnyFragment.newInstance("猜猜乐");
//        fragments.add(randomFunnyFragment);
//        funnyFragment = FunnyFragment.newInstance();
//        fragments.add(funnyFragment);
        questionAssistantFragment = QuestionAssistantFragment.newInstance();
          fragments.add(questionAssistantFragment);
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
        switch (position){
            case 0:
                setTitle("红包大作战");
                break;
            case 1:
                setTitle("微信热点");
                break;
            case 2:
                setTitle("发现有趣");
                break;
            case 3:
                setTitle("设置");
                break;
        }
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

    //记录用户首次点击返回键的时间
    private long firstTime=0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(MainTabActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    /**
//     * 判断自己的应用的AccessibilityService是否在运行
//     *
//     * @return
//     */
//    private boolean serviceIsRunning() {
//        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Short.MAX_VALUE);
//        for (ActivityManager.RunningServiceInfo info : services) {
//            if (info.service.getClassName().equals(getPackageName() + ".service.QHBAccessibilityService")) {
//                return true;
//            }
//        }
//        return false;
//    }

    private void checkAccessibilityStatus() {
        // 判断辅助功能是否开启
        if (!AccessibilityUtil.serviceIsRunning(".service.QHBAccessibilityService")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("检测未开启无障碍功能，开启后才能使用抢红包和答题助手等功能，是否去开启？")
                    .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 引导至辅助功能设置页面
                            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                        }
                    })
                    .setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainTabActivity.this, "抢红包和答题助手等功能目前不可用！", Toast.LENGTH_LONG).show();
                        }
                    });
            builder.create().show();

        }
    }
}
