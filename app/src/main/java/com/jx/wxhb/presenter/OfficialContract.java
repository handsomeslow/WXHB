package com.jx.wxhb.presenter;

import com.jx.wxhb.model.CommentInfo;
import com.jx.wxhb.model.OfficialInfo;

import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/24.
 */

public class OfficialContract {
    public interface View{

        void showOfficialView(OfficialInfo info);

        void refreshCommentView(List<CommentInfo> commentInfoList);
    }

    public interface Presenter{

        void pullOfficialData(String id);

        void pullCommentData(String id);

        void addCommentData(String comment,String id);
    }
}
