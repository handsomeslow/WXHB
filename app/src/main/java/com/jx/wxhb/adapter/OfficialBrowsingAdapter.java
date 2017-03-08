package com.jx.wxhb.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jx.wxhb.fragment.OfficialBrowsingFragment;
import com.jx.wxhb.model.Item;

/**
 * Desc
 * Created by Jun on 2017/3/8.
 */

public class OfficialBrowsingAdapter extends FragmentStatePagerAdapter {

    private final Item[] items;

    private boolean isFirst;

    public OfficialBrowsingAdapter(FragmentManager fm,Item[] items) {
        super(fm);
        this.items = items;
        isFirst = true;
    }

    @Override
    public Fragment getItem(int position) {
        boolean is = isFirst && position==0;
        isFirst = false;
        return OfficialBrowsingFragment.newInstance(items[position],is);
    }

    @Override
    public int getCount() {
        return items.length;
    }
}
