package com.jx.wxhb.presenter;

import com.jx.wxhb.model.HotNewInfo;

import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/21.
 */

public class NewsContract {
    public interface View{

        // 下拉刷新动作
        void refreshDataSuccess(List<HotNewInfo> newList);

        void refreshDataFail();

        // 上拉动作
        void loadMoreDataSuccess(List<HotNewInfo> newList);

        void loadnoMoreData();

        void loadMoreDataFail();

    }

    public interface Presenter{
        void pullNewsFromCloud();

        void refreshNewsFromCloud();
    }
}
