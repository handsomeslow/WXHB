package com.jx.wxhb.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.jx.wxhb.model.LuckyGroupInfo;
import com.jx.wxhb.utils.CloudContentUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 徐俊 on 2017/3/5.
 */

public class LuckyGroupPresenter implements LuckyGroupContract.Presenter {

    private LuckyGroupContract.View view;

    public LuckyGroupPresenter(LuckyGroupContract.View view) {
        this.view = view;
    }

    @Override
    public void pullGroupList() {
        AVQuery<AVObject> groupQuery = new AVQuery<>(CloudContentUtil.LUCKY_GROUP_TABLE);
        groupQuery.include(CloudContentUtil.LUCKY_GROUP_OWNER);
        groupQuery.orderByDescending(CloudContentUtil.CREATE_DATE);
        groupQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list == null || list.size()<=0){
                    return;
                }
                List<LuckyGroupInfo> infoList = new ArrayList<LuckyGroupInfo>();
                for (AVObject avObject:list){
                    LuckyGroupInfo info = new LuckyGroupInfo();
                    info.setId(avObject.getObjectId());
                    info.setContent(avObject.getString(CloudContentUtil.LUCKY_GROUP_CONTENT));
                    info.setOwer(avObject.getAVUser(CloudContentUtil.LUCKY_GROUP_OWNER));
                    info.setPublishDate(avObject.getCreatedAt());
                    info.setPhotoList(avObject.getList(CloudContentUtil.LUCKY_GROUP_PHOTOS));
                    infoList.add(info);
                }
                view.refreshListsuccess(infoList);
            }
        });
    }

    @Override
    public void publishGrouInfo() {

    }

    @Override
    public void addComment(String content,final String id) {
        final AVObject group = AVObject.createWithoutData(CloudContentUtil.LUCKY_GROUP_TABLE, id);

        final AVObject comment = new AVObject(CloudContentUtil.LUCKY_GROUP_COMMENT_TABLE);
        comment.put(CloudContentUtil.COMMENT_CONTENT,content);
        comment.put(CloudContentUtil.COMMENT_OWNER, AVUser.getCurrentUser());
        comment.put(CloudContentUtil.LUCKY_GROUP_TABLE,group);
        AVObject.saveAllInBackground(Arrays.asList(comment), new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVRelation<AVObject> relation = group.getRelation(CloudContentUtil.COMMENT);
                relation.add(comment);
                group.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e !=null){
                            Log.d("jun", "addCommentData:"+e.getMessage());
                            return;
                        }
                        pullGroupList();
                    }
                });
            }
        });
    }
}
