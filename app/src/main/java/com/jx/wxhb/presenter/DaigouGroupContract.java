package com.jx.wxhb.presenter;

import com.jx.wxhb.model.CommentInfo;
import com.jx.wxhb.model.LuckyGroupInfo;

import java.util.List;

/**
 * Created by 徐俊 on 2017/3/5.
 */

public class DaigouGroupContract {
    public interface View{
        void refreshListsuccess(List<LuckyGroupInfo> groupInfoList);

        void refreshListFaild();

        void loadMoreDataSuccess(List<LuckyGroupInfo> groupInfoList);

        void loadMoreDataFaild();

        void refreshComment(List<CommentInfo> comments, int position);
    }

    public interface Presenter {
        void pullGroupList();

        void refreshItemComment(String id, int position);

        void addComment(String content, String id, int position);
    }

}
