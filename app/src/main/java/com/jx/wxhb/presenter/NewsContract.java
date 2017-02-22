package com.jx.wxhb.presenter;

import com.jx.wxhb.model.HotNewInfo;

import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/21.
 */

public class NewsContract {
    public interface View{

        void refreshListView(List<HotNewInfo> newList);

    }

    public interface Presenter{
        void pullNewsFromCloud();

        void loadMoreNewsFromCloud();
    }
}
