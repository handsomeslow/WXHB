package com.jx.wxhb.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jx.wxhb.model.OfficialInfo;
import com.jx.wxhb.utils.CloudContentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 徐俊 on 2017/3/13.
 */

public class OfficialBrowsingPresenter implements OfficialBrowsingContract.Presenter {
    OfficialBrowsingContract.View view;

    public OfficialBrowsingPresenter(OfficialBrowsingContract.View view) {
        this.view = view;
    }

    @Override
    public void pullOfficialList() {
        AVQuery<AVObject> officialQuery = new AVQuery<>(CloudContentUtil.PUSH_OFFICAIL);
        officialQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e!=null){
                    e.printStackTrace();
                    return;
                }
                List<OfficialInfo> officialInfoList = new ArrayList<OfficialInfo>();
                for (AVObject official:list){
                    OfficialInfo info = new OfficialInfo();
                    info.setId(official.getObjectId());
                    info.setName(official.getString(CloudContentUtil.PUSH_OFFICAIL_NAME));
                    info.setDesc(official.getString(CloudContentUtil.PUSH_OFFICAIL_DESC));
                    info.setImage(official.getString(CloudContentUtil.PUSH_OFFICAIL_IMG));
                    info.setOrganization(official.getString(CloudContentUtil.PUSH_OFFICAIL_COMPANY));
                    info.setTags(official.getString(CloudContentUtil.PUSH_OFFICAIL_CATEGORY));
                    info.setWxId(official.getString(CloudContentUtil.PUSH_OFFICAIL_ORIGIN_ID));
                    officialInfoList.add(info);
                }
                view.refreshBrowsing(officialInfoList);
            }
        });
    }
}
