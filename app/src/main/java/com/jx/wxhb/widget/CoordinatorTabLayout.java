package com.jx.wxhb.widget;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.utils.ImageLoaderUtil;

/**
 * Desc
 * Created by Jun on 2017/2/6.
 */

public class CoordinatorTabLayout extends CoordinatorLayout {
    private Context context;

    public Toolbar getToolbar() {
        return toolbar;
    }

    private Toolbar toolbar;

    public ActionBar getActionBar() {
        return actionBar;
    }

    private ActionBar actionBar;
    private TabLayout tabLayout;
    private ImageView imageView;
    private TextView nameTextView;
    private TextView descTextView;
    private TextView categoryTextView;
    private TextView wxIdView;
    private TextView companyTextView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public CoordinatorTabLayout(Context context) {
        super(context);
        this.context = context;
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.view_coordinatortab_layout,this);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);
        actionBar = ((AppCompatActivity) context).getSupportActionBar();
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        imageView = (ImageView) findViewById(R.id.imageView);
        nameTextView = (TextView) findViewById(R.id.name_text_view);
        descTextView = (TextView) findViewById(R.id.desc_text_view);
        categoryTextView = (TextView) findViewById(R.id.category_text_view);
        wxIdView = (TextView) findViewById(R.id.wxid_text_view);
        companyTextView = (TextView) findViewById(R.id.company_text_view);
    }

    public CoordinatorTabLayout setTitle(String title){
        if (actionBar!=null){
            actionBar.setTitle(title);
        }
        return this;
    }

    private void setUpTabLayout(final String imageId){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ImageLoaderUtil.displayImageByObjectId(imageId,imageView);
                imageView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_dismiss));
                //imageView.setImageResource(imageArray[tab.getPosition()]);
                imageView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_show));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public CoordinatorTabLayout setImageBackground(String imageId){
        if (!TextUtils.isEmpty(imageId)){
            ImageLoaderUtil.displayImageByObjectId(imageId,imageView);
            //setUpTabLayout(imageId);
        }
        return this;
    }

    public CoordinatorTabLayout setUpWithViewPager(ViewPager viewPager){
        tabLayout.setupWithViewPager(viewPager);
        return this;
    }

    public CoordinatorTabLayout setName(String name){
        nameTextView.setText(name);
        return this;
    }

    public CoordinatorTabLayout setDesc(String desc){
        descTextView.setText(desc);
        return this;
    }
    public CoordinatorTabLayout setCategory(String category){
        categoryTextView.setText(category);
        return this;
    }
    public CoordinatorTabLayout setCompany(String company){
        companyTextView.setText(company);
        return this;
    }

    public CoordinatorTabLayout setWxId(String wxId){
        wxIdView.setText(String.format("微信号：%s",wxId));
        return this;
    }


}
