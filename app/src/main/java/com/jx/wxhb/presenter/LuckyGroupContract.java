package com.jx.wxhb.presenter;

import com.jx.wxhb.model.HotNewInfo;
import com.jx.wxhb.model.LuckyGroupInfo;

import java.util.List;

/**
 * Created by 徐俊 on 2017/3/5.
 */

public class LuckyGroupContract {
    public interface View{
        void refreshListsuccess(List<LuckyGroupInfo> groupInfoList);

        void refreshListFaild();

        void loadMoreDataSuccess(List<LuckyGroupInfo> groupInfoList);

        void loadMoreDataFaild();
    }

    public interface Presenter {
        void pullGroupList();

        void publishGrouInfo();

        void addComment(String content, final String id);
    }

}
