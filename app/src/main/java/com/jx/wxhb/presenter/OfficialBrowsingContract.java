package com.jx.wxhb.presenter;

import com.jx.wxhb.model.OfficialInfo;

import java.util.List;

/**
 * Created by 徐俊 on 2017/3/13.
 */

public class OfficialBrowsingContract {
    public interface View{
        void refreshBrowsing(List<OfficialInfo>  officialInfoList);
    }

    public interface Presenter{
        void pullOfficialList();
    }
}
