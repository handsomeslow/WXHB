package com.jx.wxhb.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jx.wxhb.utils.CloudContentUtil;

import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/24.
 */

public class FunnyPresenter implements FunnyContract.Presenter {

    FunnyContract.View view;

    public FunnyPresenter(FunnyContract.View view) {
        this.view = view;
    }

    @Override
    public void pullOfficialData() {
        AVQuery<AVObject> query = new AVQuery<>(CloudContentUtil.PUSH_OFFICAIL);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list !=null && list.size()>0){
                    view.refreshListView();
                }
            }
        });
    }
}
