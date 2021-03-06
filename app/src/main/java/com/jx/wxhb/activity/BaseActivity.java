package com.jx.wxhb.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;

/**
 * Desc
 * Created by Jun on 2017/2/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected void addFragment(Fragment fragment, int wrap){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(wrap,fragment).commitAllowingStateLoss();
    }

    protected Fragment findFragment(int id){
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentById(id);
    }

    protected void showBackButton(){
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
