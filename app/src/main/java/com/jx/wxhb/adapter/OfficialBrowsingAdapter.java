package com.jx.wxhb.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jx.wxhb.fragment.OfficialBrowsingFragment;
import com.jx.wxhb.model.Item;
import com.jx.wxhb.model.OfficialInfo;

import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/3/8.
 */

public class OfficialBrowsingAdapter extends FragmentStatePagerAdapter {

    private List<OfficialInfo>  officialInfoList;

    private boolean isFirst;

    public OfficialBrowsingAdapter(FragmentManager fm,List<OfficialInfo> officialInfoList) {
        super(fm);
        this.officialInfoList = officialInfoList;
        isFirst = true;
    }

    @Override
    public Fragment getItem(int position) {
        boolean is = isFirst && position==0;
        isFirst = false;
        return OfficialBrowsingFragment.newInstance(officialInfoList.get(position),is);
    }

    @Override
    public int getCount() {
        return officialInfoList.size();
    }
}
